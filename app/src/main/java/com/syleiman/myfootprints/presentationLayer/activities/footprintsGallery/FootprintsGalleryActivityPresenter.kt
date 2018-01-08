package com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery

import android.app.Activity

import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.modelLayer.dto.FootprintForGalleryDto
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.EditFootprintActivity
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.result.FootprintsGalleryActivityResult

import javax.inject.Inject

class FootprintsGalleryActivityPresenter
@Inject
constructor(private val activityCallbacks: IFootprintsGalleryActivityCallbacks, private val model: FootprintsGalleryActivityModel) : IFootprintsGalleryActivityPresenterPages
{
    /**
     * Init collection
     * @param startFootprintId
     */
    fun init(startFootprintId: Long, normalSortOrder: Boolean)
    {
        model.init(startFootprintId, normalSortOrder) { isSuccess, currentPosition ->
            activityCallbacks.initCompleted(isSuccess, currentPosition)
        }
    }

    /**
     * Get footprint dto by it's position
     * @param position (from 0)
     */
    override fun getInPosition(position: Int): FootprintForGalleryDto = model.getInPosition(position)

    /** Get total pages  */
    override val totalPages: Int
        get() = model.totalPages

    /**
     * Delete footprint
     * @param currentItemIndex index of deleted footprint
     */
    fun deleteFootprint(currentItemIndex: Int)
    {
        activityCallbacks.showQueryDialog(R.string.gallery_activity_delete_query, R.string.yes_capitalize, R.string.no_capitalize) {
            activityCallbacks.showFullScreenProgress()
            model.deleteFootprint(currentItemIndex) { newPageIndex ->
                activityCallbacks.hideFullScreenProgress()

                if (newPageIndex == null)
                // Fail
                    activityCallbacks.showMessage(R.string.message_box_cant_delete_footprint)
                else if (newPageIndex == -1)
                // No footprints
                    activityCallbacks.close()
                else
                    activityCallbacks.pageDeleted(newPageIndex)            // Success
            }
        }
    }

    /**
     * Start footprint editing
     * @param currentItemIndex index of deleted footprint
     */
    fun startEditFootprint(activity: Activity, currentItemIndex: Int)
    {
        val footprintId = model.getInPosition(currentItemIndex).footprintId
        EditFootprintActivity.start(activity, footprintId)
    }

    /** Called when footprint editing was completed  */
    fun completeEditFootprint(resultCode: Int, position: Int)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            model.updateAfterEdit(position)
            activityCallbacks.redrawAfterUpdate()
        }
    }

    /**  */
    val activityResult: FootprintsGalleryActivityResult
        get() = model.createActivityResult()

    /** Sent footprint to external systems  */
    fun sendToExternal(context: Activity, position: Int)
    {
        if (!model.sendToExternal(context, position))
            activityCallbacks.showMessage(R.string.message_box_cant_send_footprint)
    }
}