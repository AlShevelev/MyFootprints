package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync

import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncResult
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.SyncCommandInThread
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.notifications.IForegroundManager

/** Sync data using GD  */
class GoogleDriveSyncCommand(
    private val footprints : IFootprintsService,
    private val options : IOptionsCacheService,
    private val sysInfo : SystemInformationService,
    private val localDb: ILocalDbService,
    private val foregroundManager: IForegroundManager) : SyncCommandInThread()
{
    private var syncSm: SyncSm? = null

    /**
     * Do useful job here
     * @param locker Don't forget call @locker.notify() !
     */
    override fun doJob(locker: Any, syncResult: SyncResult)
    {
        syncSm = SyncSm(footprints, options, sysInfo, localDb, locker, foregroundManager, syncResult)
        syncSm!!.process(Events.StartConnect)
    }
}
