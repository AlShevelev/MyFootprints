package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions

import android.util.Log
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveApi
import com.google.android.gms.drive.query.Filters
import com.google.android.gms.drive.query.Query
import com.google.android.gms.drive.query.SearchableField
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.common.debug.TimeTracing
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.Events
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.ExternalLogRecord
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.ISyncContextProcessing
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.ISyncSmProcess
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.notifications.IForegroundManager
import java.sql.SQLException
import java.util.*

/** Get data from local sync log and query external files  */
class PrepareDataItemsTransition(
    options : IOptionsCacheService,
    sysInfo : SystemInformationService,
    localDb: ILocalDbService,
    private val syncContext: ISyncContextProcessing,
    private val syncStateMachine: ISyncSmProcess,
    private val foregroundManager: IForegroundManager) : TransitionBase(options, sysInfo, localDb)
{

    /** Do something useful  */
    override fun process()
    {
        Log.d(LogTags.SYNC_PROCESS, "Start prepare data items")
        if (!canUseInternet())        // No internet connection or can't use this connection type
        {
            Log.w(LogTags.SYNC_PROCESS, "[PrepareDataItemsTransition] No internet connection or can't use this connection type")
            syncStateMachine.process(Events.Error)
            return
        }

        if (!syncContext.isConnected())        // Not connected to Google Drive
        {
            Log.w(LogTags.SYNC_PROCESS, "[PrepareDataItemsTransition] Not connected to Google Drive")
            syncStateMachine.process(Events.Error)
            return
        }

        foregroundManager.updateProgress(0, 0, App.getStringRes(R.string.sync_get_list_files))

        TimeTracing(LogTags.SYNC_PROCESS, "resetLockOnLogRecords").use {
            resetLockOnLogRecords()        // Reset lock on log records (if service was killed by OS and locks was not reseted)
        }

        Log.d(LogTags.SYNC_PROCESS, "Start get log records")
        if (!getLogRecords())        // Get local log records and set them to context
        {
            Log.w(LogTags.SYNC_PROCESS, "[PrepareDataItemsTransition] Can't get log record")
            syncStateMachine.process(Events.Error)
            return
        }

        Drive.DriveApi.requestSync(syncContext.getClient()).setResultCallback(requestSyncCallback)         // Without sync we can lose some files from GD
    }

    /** Callback for request sync  */
    private val requestSyncCallback = ResultCallback<Status> { result ->
        if (!result.status.isSuccess)
        {
            Log.w(LogTags.SYNC_PROCESS, "[PrepareDataItemsTransition] Request sync is unsuccess")
            syncStateMachine.process(Events.Error)
        } else
        {
            val lastProcessedDate = getLastProcessedFileDate()
            val query: Query?

            if (lastProcessedDate != null)
                query = Query.Builder().// all new files from other devises
                        addFilter(
                                Filters.and(
                                        Filters.not(Filters.contains(SearchableField.TITLE, getInstallId())),
                                        Filters.greaterThan(SearchableField.MODIFIED_DATE, getLastProcessedFileDate()))).build()
            else
                query = Query.Builder().// all new files from other devises
                        addFilter(Filters.not(Filters.contains(SearchableField.TITLE, getInstallId()))).build()

            Drive.DriveApi.getAppFolder(syncContext.getClient()).queryChildren(syncContext.getClient(), query).setResultCallback(metadataCallback)
        }
    }

    /** Callback for files query  */
    private val metadataCallback = ResultCallback<DriveApi.MetadataBufferResult> { result ->
        try
        {
            if (!result.status.isSuccess)
            {
                Log.w(LogTags.SYNC_PROCESS, "[PrepareDataItemsTransition] Query files is unsuccess")
                syncStateMachine.process(Events.Error)
            } else
            {
                val queryResult = result.metadataBuffer

                val files = ArrayList<ExternalLogRecord>(queryResult.count)

                Log.d(LogTags.SYNC_PROCESS, "Total files to loadAndInscribeQuick: " + queryResult.count)

                for (metadata in queryResult)
                {
                    Log.d(LogTags.SYNC_PROCESS, "Ext file. Id: " + metadata.title + ";")
                    files.add(ExternalLogRecord(metadata.driveId, metadata.createdDate, metadata.fileSize.toInt()))
                }

                /*
                    files = Stream.of(files).
                            sorted((f1, f2) -> f1.createDate.compareTo(f2.createDate)).         // Sort files by CreateDate here
                            collect(Collectors.toList());
*/

                Collections.sort(files) { f1, f2 -> f1.createDate.compareTo(f2.createDate) }        // Sort files by CreateDate here

                syncContext.setExternalFiles(files)

                syncStateMachine.process(Events.ProcessCompleted)
            }
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()

            Log.w(LogTags.SYNC_PROCESS, "[PrepareDataItemsTransition] Exception while process files")
            syncStateMachine.process(Events.Error)
        }
    }

    /** Get creation date of last processed file  */
    private fun getLastProcessedFileDate(): Date? = options.getExternalLastSyncDate()

    /**
     * Get new records from local sync log and set them to context
     * @return true - success
     */
    private fun getLogRecords(): Boolean
    {
        try
        {
            val logRecordsIds: List<Long>?
            logRecordsIds = localDb.syncLog().getDataToProcess()

            syncContext.setLogRecords(logRecordsIds)
            return true
        }
        catch (e: SQLException)
        {
            e.printStackTrace()
            return false
        }
    }
}