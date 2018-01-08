package com.syleiman.myfootprints.presentationLayer.activities.myWorld

import com.syleiman.myfootprints.presentationLayer.activities.IActivityBaseCallback

/** Interface to inject in presenter  */
interface IMyWorldActivityCallbacks : IActivityBaseCallback
{
    /**
     * Sync was completed
     * @param needUpdate need update UI
     */
    fun syncCompleted(needUpdate: Boolean)
}
