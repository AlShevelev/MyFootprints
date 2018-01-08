package com.syleiman.myfootprints.presentationLayer.activities.options

import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService

import javax.inject.Inject

/** Model for OptionsActivity  */
class OptionsActivityModel
@Inject
constructor(private val options : IOptionsCacheService)
{
    /** Get "Send data to social networks only via WiFi" option  */
    /** Update "Send data to social networks only via WiFi" option  */
    internal var syncOnlyViaWifiOptions: Boolean
        get() = options.getSyncOnlyViaWifiOptions()
        set(isTurnOn) = options.setSyncOnlyViaWifiOptions(isTurnOn)
}
