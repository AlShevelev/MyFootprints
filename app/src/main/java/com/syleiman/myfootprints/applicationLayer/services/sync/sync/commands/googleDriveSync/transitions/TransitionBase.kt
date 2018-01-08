package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions

import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.sysInfoService.InternetConnectionStatuses
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService

import java.sql.SQLException

/** Base class for all transitions  */
abstract class TransitionBase protected
constructor(
    protected val options : IOptionsCacheService,
    protected val sysInfo : SystemInformationService,
    protected val localDb: ILocalDbService) : ITransition
{
    /** Check: 1) Is connect to Internet; 2) Can use this type of connection (Mobile, for example)  */
    protected fun canUseInternet(): Boolean
    {
        val internetStatus = SystemInformationService.getInternetConnectionStatus()

        if (internetStatus === InternetConnectionStatuses.None)
            return false
        if (options.getSyncOnlyViaWifiOptions() && internetStatus === InternetConnectionStatuses.Mobile)
            return false           // User forbad use mobile network

        return true
    }

    /** Get id of current installiation  */
    protected fun getInstallId(): String = sysInfo.getInstallId()

    /** Reset locks on log records (in case of error when not all records were processed)  */
    protected fun resetLockOnLogRecords()
    {
        try
        {
            localDb.syncLog().resetAllLocks()
        }
        catch (e: SQLException)
        {
            e.printStackTrace()
        }
    }
}