package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions

import android.util.Log
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveApi
import com.google.android.gms.drive.DriveFolder
import com.google.android.gms.drive.MetadataChangeSet
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.Events
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.ISyncContextProcessing
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.ISyncSmProcess
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions.protobuf.SyncLogDtoConverter
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.notifications.IForegroundManager
import java.sql.SQLException
import java.util.*

/** Send records from our sync log  */
class SendLogRecordTransition(
    options : IOptionsCacheService,
    sysInfo : SystemInformationService,
    localDb: ILocalDbService,
    private val syncContext: ISyncContextProcessing,
    private val syncStateMachine: ISyncSmProcess,
    private val foregroundManager: IForegroundManager) : TransitionBase(options, sysInfo, localDb)
{
    private var currentIndex = -1
    private var currentLogRecord: SyncLogDto? = null

    /** Do something useful  */
    override fun process()
    {
        currentIndex++
        val totalToProcess = syncContext.getLogRecords().size

        foregroundManager.updateProgress(currentIndex + 1, totalToProcess, App.getStringRes(R.string.sync_send_footprints))

        if (currentIndex == totalToProcess)
            syncStateMachine.process(Events.ProcessCompleted)
        else
        {
            if (!canUseInternet())
            // No internet connection or can't use this connection type
            {
                Log.w(LogTags.SYNC_PROCESS, "[SendLogRecordTransition] No internet connection or can't use this connection type")
                syncStateMachine.process(Events.Error)
            } else if (!syncContext.isConnected())
            // Not connected to Google Drive
            {
                Log.w(LogTags.SYNC_PROCESS, "[SendLogRecordTransition] Not connected to Google Drive")
                syncStateMachine.process(Events.Error)
            } else
                Drive.DriveApi.newDriveContents(syncContext.getClient()).setResultCallback(driveContentsCallback)      // start context (like transaction for new file creation)
        }
    }

    /** Callback - context created and we can now create file  */
    private val driveContentsCallback = ResultCallback<DriveApi.DriveContentsResult> { result ->
        if (!result.status.isSuccess)
        {
            Log.w(LogTags.SYNC_PROCESS, "[SendLogRecordTransition] Can't get drive content")
            syncStateMachine.process(Events.Error)
        }

        object : Thread()
        {
            override fun run()
            {
                try
                {
                    currentLogRecord = localDb.syncLog().getRecord(syncContext.getLogRecords()[currentIndex])
                    Log.d(LogTags.SYNC_PROCESS, currentLogRecord!!.toLogString("SyncRecord"))

                    val driveContents = result.driveContents

                    driveContents.outputStream.use { outputStream ->
                        outputStream.write(logRecordsToBytes(currentLogRecord!!))
                        outputStream.flush()
                    }

                    val fileName = createFileName()

                    Log.d(LogTags.SYNC_PROCESS, "Try to send file: $fileName ...")

                    val changeSet = MetadataChangeSet.Builder().setTitle(fileName).setStarred(true).build()

                    Drive.DriveApi.getAppFolder(syncContext.getClient()).// create a file on root folder
                            createFile(syncContext.getClient(), changeSet, driveContents).setResultCallback(fileCallback)
                }
                catch (ex: Exception)
                {
                    ex.printStackTrace()
                    Log.w(LogTags.SYNC_PROCESS, "[SendLogRecordTransition] Error while send file")
                    syncStateMachine.process(Events.Error)
                }

            }
        }.start()
    }

    /** File creation result  */
    private val fileCallback = ResultCallback<DriveFolder.DriveFileResult> { result ->
        try
        {
            val status = result.status
            if (status.isSuccess)
            {
                Log.d(LogTags.SYNC_PROCESS, "... success")

                removeProcessedRecord(currentLogRecord!!.id)
                syncStateMachine.process(Events.ProcessOneItem)
            } else
            {
                val code = status.statusCode
                val message = status.statusMessage
                Log.w(LogTags.SYNC_PROCESS, "[SendLogRecordTransition] Create file failed. Code is: $code. Message is: $message")
                syncStateMachine.process(Events.Error)
            }
        } catch (ex: Exception)
        {
            ex.printStackTrace()
            Log.w(LogTags.SYNC_PROCESS, "[SendLogRecordTransition] Error while create file")
            syncStateMachine.process(Events.Error)
        }
    }

    /**  */
    private fun logRecordsToBytes(logRecord: SyncLogDto): ByteArray = SyncLogDtoConverter.toBytes(logRecord)

    /**  */
    private fun createFileName(): String = getInstallId() + "_" + UUID.randomUUID().toString() + ".fpt"

    /**  */
    @Throws(SQLException::class)
    private fun removeProcessedRecord(recordId: Long)
    {
        localDb.syncLog().removeLogRecord(recordId)
        Log.d("SYNC_SERVICE", "Remove from sync log")
    }
}