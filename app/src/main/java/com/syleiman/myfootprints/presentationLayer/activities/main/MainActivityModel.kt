package com.syleiman.myfootprints.presentationLayer.activities.main

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.ISyncResultChannel
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncCompletedReceiver
import com.syleiman.myfootprints.businessLayer.mainActivityCoverService.IMainActivityCoverService
import javax.inject.Inject

class MainActivityModel
@Inject
constructor(private val mainActivityCover : IMainActivityCoverService)
{
    private var syncCompletedReceiver: SyncCompletedReceiver? = null

    /**
     * Get last photo
     * @param resultCallback last photo or null
     */
    fun getLastFootprint(resultCallback: (Drawable?, Int) -> Unit)
    {
        mainActivityCover.getCover { cover ->
            if (cover == null)            // Error
                resultCallback.invoke(null, 0)
            else
                resultCallback.invoke(BitmapDrawable(App.context.resources, cover.lastPhotoBitmap), cover.totalFootprints)
        }
    }

    /** Start receive sync events  */
    fun startReceiveSyncStatus(syncResultChannel: ISyncResultChannel)
    {
        syncCompletedReceiver = SyncCompletedReceiver(syncResultChannel)
    }

    /** Start receive sync events  */
    fun stopReceiveSyncStatus()
    {
        if (syncCompletedReceiver != null)
            syncCompletedReceiver!!.unregister()
    }
}
