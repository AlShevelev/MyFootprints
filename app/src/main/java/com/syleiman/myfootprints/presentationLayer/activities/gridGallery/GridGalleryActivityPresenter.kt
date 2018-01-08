package com.syleiman.myfootprints.presentationLayer.activities.gridGallery

import android.app.Activity
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.ISyncResultChannel
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncResult
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.FootprintsGalleryActivity
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.result.FootprintsGalleryActivityResult
import javax.inject.Inject

class GridGalleryActivityPresenter
@Inject
constructor(private val activityCallbacks: IGridGalleryActivityCallbacks, private val model: GridGalleryActivityModel)
{
    /** When activity started  */
    fun init()
    {
        refreshGrid(true)
        model.startReceiveSyncStatus(object : ISyncResultChannel
        {                   // Start receive events from sync
            override fun syncCompleted(result: SyncResult) = activityCallbacks.syncCompleted(result.footprintsWasChanged)
        })
    }

    /** Called from activity when it destroyed finally  */
    fun onDestroyUi()
    {
        model.stopReceiveSyncStatus()          // Stop receive events from sync
    }

    /**  */
    fun refreshGrid(fromStart: Boolean)
    {
        activityCallbacks.showProgress()
        model.initGridData { result ->
            if (result)
            {
                val totalFootprints = model.totalItems
                if (totalFootprints != 0)
                {
                    if (fromStart)
                        activityCallbacks.showGrid(0)
                    else
                    {
                        val lastClickedPosition = model.lastClickedPosition
                        if (lastClickedPosition < totalFootprints)
                            activityCallbacks.showGrid(lastClickedPosition)
                        else
                            activityCallbacks.showGrid(totalFootprints - 1)
                    }
                } else
                    activityCallbacks.showNoFootprints()
            } else
            {
                activityCallbacks.hideProgress()
                activityCallbacks.showMessage(R.string.message_box_cant_load_footprints)
            }
        }
    }

    /**  */
    fun getDataItemByPosition(position: Int): GridDataItem = model.getDataItemByPosition(position)

    /**  */
    val totalItems: Int
        get() = model.totalItems

    /** Click on item  */
    fun clickOnItem(activity: Activity, position: Int)
    {
        val footprintId = model.getDataItemIdByPosition(position)
        FootprintsGalleryActivity.start(activity, footprintId, false)
    }

    /** Process result from Gallery activity  */
    fun processGalleryResult(galleryResult: FootprintsGalleryActivityResult)
    {
        if (galleryResult.hasData())
            refreshGrid(false)
    }
}