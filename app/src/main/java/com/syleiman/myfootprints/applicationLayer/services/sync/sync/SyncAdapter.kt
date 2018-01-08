package com.syleiman.myfootprints.applicationLayer.services.sync.sync

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import android.util.Log

import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.sysInfoService.InternetConnectionStatuses
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService
import com.syleiman.myfootprints.businessLayer.tasksService.ITasksService
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncCompletedSender
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.dbTasks.DbTasksCommand
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.SyncCommandsCollection
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.GoogleDriveSyncCommand
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.notifications.IForegroundManager

import javax.inject.Inject

/** Handle the transfer of data between a server and an app, using the Android sync adapter framework.  */
class SyncAdapter
/**  */
(private val foregroundManager: IForegroundManager, context: Context, autoInitialize: Boolean) : AbstractThreadedSyncAdapter(context, autoInitialize)
{
    @Inject internal lateinit var footprints : IFootprintsService
    @Inject internal lateinit var options : IOptionsCacheService
    @Inject internal lateinit var sysInfo : SystemInformationService
    @Inject internal lateinit var tasks : ITasksService
    @Inject internal lateinit var localDb: ILocalDbService

    init
    {
        Log.d(LogTags.SYNC_PROCESS, "create SyncAdapter")
    }

    /**  */
    override fun onPerformSync(account: Account, extras: Bundle, authority: String, provider: ContentProviderClient, syncResult: SyncResult)
    {
        try
        {
            Log.d(LogTags.SYNC_PROCESS, "Start sync")

            App.application.getServicesComponent().inject(this)

            val internetStatus = SystemInformationService.getInternetConnectionStatus()

            if (internetStatus === InternetConnectionStatuses.None)
                Log.d(LogTags.SYNC_PROCESS, "No Internet connection")           // No Internet connection
            else if (options.getSyncOnlyViaWifiOptions() && internetStatus === InternetConnectionStatuses.Mobile)
                Log.d(LogTags.SYNC_PROCESS, "User forbad use mobile network")           // User forbad use mobile network
            else
                processSync()
        }
        catch (ex : RuntimeException)
        {
            ex.printStackTrace()
        }
        finally
        {
            App.application.releaseServicesComponent()
        }
    }

    /**  */
    private fun processSync()
    {
        foregroundManager.startForegroundMode()

        Log.d(LogTags.SYNC_PROCESS, "Start")

        val commands = SyncCommandsCollection()
        commands.add(DbTasksCommand(tasks, foregroundManager))              // Create commands
        commands.add(GoogleDriveSyncCommand(footprints, options, sysInfo, localDb, foregroundManager))

        val syncResults = commands.execute()           // Execute

        val totalResult = com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncResult.Merge(syncResults[0], syncResults[1])
        SyncCompletedSender().syncCompleted(totalResult)            // Send broadcast result

        Log.d(LogTags.SYNC_PROCESS, "Complete")

        foregroundManager.stopForegroundMode()
    }
}