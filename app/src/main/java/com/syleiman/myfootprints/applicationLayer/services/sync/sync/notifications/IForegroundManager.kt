package com.syleiman.myfootprints.applicationLayer.services.sync.sync.notifications

/** Facade for service's foreground actions  */
interface IForegroundManager
{
    /** Turn on foreground mode for service  */
    fun startForegroundMode()

    /** Turn off foreground mode for service  */
    fun stopForegroundMode()

    /**
     * Update progressBar on foreground panel
     * @param value current progressBar value (<= @maxValue)
     * @param maxValue max progressBar value
     */
    fun updateProgress(value: Int, maxValue: Int, text: String)
}
