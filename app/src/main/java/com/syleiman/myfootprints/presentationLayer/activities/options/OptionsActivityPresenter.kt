package com.syleiman.myfootprints.presentationLayer.activities.options

import javax.inject.Inject

/** Presenter for OptionsActivity  */
class OptionsActivityPresenter
@Inject
constructor(private val model: OptionsActivityModel)
{
    /** Get "Send data to social networks only via WiFi" option  */
    /** Update "Send data to social networks only via WiFi" option  */
    internal var syncOnlyViaWifiOptions: Boolean
        get() = model.syncOnlyViaWifiOptions
        set(isTurnOn)
        {
            model.syncOnlyViaWifiOptions = isTurnOn
        }
}
