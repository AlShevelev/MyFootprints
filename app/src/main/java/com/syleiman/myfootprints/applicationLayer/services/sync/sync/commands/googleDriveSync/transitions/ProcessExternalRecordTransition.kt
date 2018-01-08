package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions

import android.util.Log
import com.google.android.gms.drive.DriveFile
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncResult
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.Events
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.ISyncContextProcessing
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.ISyncSmProcess
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions.protobuf.SyncLogDtoConverter
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.notifications.IForegroundManager
import java.io.BufferedInputStream
import java.io.IOException
import java.sql.SQLException
import java.util.*

/** Process external sync records  */
class ProcessExternalRecordTransition(
    private val footprints : IFootprintsService,
    options : IOptionsCacheService,
    sysInfo : SystemInformationService,
    localDb: ILocalDbService,
    private val syncContext: ISyncContextProcessing,
    private val syncStateMachine: ISyncSmProcess,
    private val foregroundManager: IForegroundManager,
    private val syncResult: SyncResult) : TransitionBase(options, sysInfo, localDb)
{
    private var currentIndex = -1

    /** Do something useful  */
    override fun process()
    {
        currentIndex++
        val totalToProcess = syncContext.getExternalFiles().size

        foregroundManager.updateProgress(currentIndex + 1, totalToProcess, App.getStringRes(R.string.sync_load_footprints))

        if (currentIndex == totalToProcess)
            syncStateMachine.process(Events.ProcessCompleted)
        else
        {
            if (!canUseInternet())
            // No internet connection or can't use this connection type
            {
                Log.w(LogTags.SYNC_PROCESS, "[ProcessExternalRecordTransition] No internet connection or can't use this connection type")
                syncStateMachine.process(Events.Error)
            }
            else if (!syncContext.isConnected())            // Not connected to Google Drive
            {
                Log.w(LogTags.SYNC_PROCESS, "[ProcessExternalRecordTransition] Not connected to Google Drive")
                syncStateMachine.process(Events.Error)
            }
            else
                receiveRecord()             // Process external records
        }
    }

    /** Process external records */
    private fun receiveRecord()
    {
        val processedRecord = syncContext.getExternalFiles()[currentIndex]
        val file = processedRecord.driveId.asDriveFile()

        file.open(syncContext.getClient(), DriveFile.MODE_READ_ONLY, null).setResultCallback({ driveContentsResult ->
            if (!driveContentsResult.status.isSuccess)
            {
                Log.w(LogTags.SYNC_PROCESS, "[ProcessExternalRecordTransition] Open file is filed")
                syncStateMachine.process(Events.Error)
            }

            val driveContents = driveContentsResult.driveContents

            var hasError = false
            val buffer = ByteArray(processedRecord.fileSize)

            try
            {
                BufferedInputStream(driveContents.inputStream).use { stream ->
                    var bytesRead = stream.read(buffer)
                    while (bytesRead != -1)
                        bytesRead = stream.read(buffer, bytesRead, buffer.size - bytesRead)

                    this.processReceivedRecord(buffer)          // Process record
                    this.storeLastProcessedRecordCreateDate(processedRecord.createDate)       // Store processed date
                }
            } catch (e: Exception)
            {
                e.printStackTrace()
                hasError = true
            }

            driveContents.discard(syncContext.getClient())         // Close content

            if (hasError)
            {
                Log.w(LogTags.SYNC_PROCESS, "[ProcessExternalRecordTransition] Error while process file")
                syncStateMachine.process(Events.Error)
            }
            else
                syncStateMachine.process(Events.ProcessOneItem)
        })
    }

    /** Use data from record  */
    @Throws(IOException::class, SQLException::class)
    private fun processReceivedRecord(recordData: ByteArray)
    {
        val syncRecord = SyncLogDtoConverter.fromBytes(recordData)
        val processor = footprints.getExpUpdateProcessor(syncRecord!!)
        processor.process()

        syncResult.footprintsWasChanged = true

        Log.d("SYNC_SERVICE", "Ext file processed")
    }

    /** Store creation date of last processed record */
    private fun storeLastProcessedRecordCreateDate(date: Date)
    {
        options.setExternalLastSyncDate(date)
    }
}