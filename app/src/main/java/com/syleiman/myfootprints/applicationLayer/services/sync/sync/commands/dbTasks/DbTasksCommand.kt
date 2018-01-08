package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.dbTasks

import android.util.Log
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.sysInfoService.InternetConnectionStatuses
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.businessLayer.tasksService.ITasksService
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncResult
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.SyncCommandBase
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.notifications.IForegroundManager
import java.sql.SQLException

/** Command for processing all tasks in Db  */
class DbTasksCommand(
    private val tasks : ITasksService,
    private val foregroundManager: IForegroundManager) : SyncCommandBase()
{

    /** Execute command  */
    override fun execute(): SyncResult
    {
        val syncResult = SyncResult()

        val internetStatus = SystemInformationService.getInternetConnectionStatus()
        if (internetStatus === InternetConnectionStatuses.None)         // No internet connection
        {
            Log.d(LogTags.SYNC_PROCESS, "Command. No Internet connection")
            return syncResult
        }

        try
        {
            val tasksFactory = tasks.getTasksFactory()
            val totalTasks = tasksFactory.init()

            Log.d(LogTags.SYNC_PROCESS, "Command. Task factory created, total tasks is: " + totalTasks)
            val progressTitle = App.getStringRes(R.string.sync_geocoding)

            for (i in 0..totalTasks - 1)
            {
                foregroundManager.updateProgress(i + 1, totalTasks, progressTitle)
                val executionResult = tasksFactory.getByIndex(i).execute()

                Log.d(LogTags.SYNC_PROCESS, String.format("Command. Task %1\$s executed. Result is: %2\$s", i, executionResult))

                if (!executionResult)      // fatal error - interrupting
                    return syncResult
                else
                    syncResult.footprintsWasChanged = true
            }
        }
        catch (ex: SQLException)
        {
            Log.e(LogTags.SYNC_PROCESS, "Command. SQLException:" + ex.message)
            ex.printStackTrace()
        }

        return syncResult
    }
}