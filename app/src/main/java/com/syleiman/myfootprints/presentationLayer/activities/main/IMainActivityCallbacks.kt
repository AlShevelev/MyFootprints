package com.syleiman.myfootprints.presentationLayer.activities.main

import android.graphics.drawable.Drawable

import com.syleiman.myfootprints.presentationLayer.activities.IActivityBaseCallback

/** Activities interface for presenter  */
interface IMainActivityCallbacks : IActivityBaseCallback
{
    /**
     * Set cover image
     * @param image
     */
    fun setCover(image: Drawable)

    /**
     * Set total footprints label
     * @param totalFootprintsQuantity
     */
    fun setTotalFootprints(totalFootprintsQuantity: Int)

    /**
     * Sync was completed
     * @param needUpdate need update UI
     */
    fun syncCompleted(needUpdate: Boolean)
}
