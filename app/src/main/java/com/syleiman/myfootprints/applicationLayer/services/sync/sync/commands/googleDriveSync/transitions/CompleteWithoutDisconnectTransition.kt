package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions

import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService

/** Complete working without disconnect from Google API  */
class CompleteWithoutDisconnectTransition(
    options : IOptionsCacheService,
    sysInfo : SystemInformationService,
    localDb: ILocalDbService,
    private val locker: Any) : TransitionBase(options, sysInfo, localDb)
{

    /** Do something useful  */
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    override fun process()
    {
        resetLockOnLogRecords()

        synchronized(locker) {
            (locker as Object).notify()
        }
    }
}
