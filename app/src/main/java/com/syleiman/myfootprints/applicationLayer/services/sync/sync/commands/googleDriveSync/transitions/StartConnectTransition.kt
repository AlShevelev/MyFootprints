package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions

import android.util.Log

import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.Events
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.ISyncSmProcess
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.SyncContext
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.notifications.IForegroundManager

/** Start connect  */
class StartConnectTransition(
    options : IOptionsCacheService,
    sysInfo : SystemInformationService,
    localDb: ILocalDbService,
    private val syncContext: SyncContext,
    private val syncStateMachine: ISyncSmProcess,
    private val foregroundManager: IForegroundManager) : TransitionBase(options, sysInfo, localDb), ISyncSmProcess
{

    init
    {
        syncContext.attachCallbacks(this)
    }

    /** Do something useful  */
    override fun process()
    {
        if (!canUseInternet())  // No internet connection or can't use this connection type
        {
            Log.w(LogTags.SYNC_PROCESS, "[StartConnectTransition] No internet connection or can't use this connection type")
            syncStateMachine.process(Events.Error)
        }

        foregroundManager.updateProgress(0, 0, App.getStringRes(R.string.sync_connecting))
        syncContext.connect()
    }

    /** Process event  */
    override fun process(event: Events) =  syncStateMachine.process(event)          // Transfer connections events from context
}