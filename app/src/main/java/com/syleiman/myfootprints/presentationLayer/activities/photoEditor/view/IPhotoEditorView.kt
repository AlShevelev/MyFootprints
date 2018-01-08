package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view

import android.graphics.Bitmap
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters

/** Interface of PhotoEditorActivity for presenter */
interface IPhotoEditorView
{
    /** Show and hide progress */
    fun setProgressVisibility(isVisible : Boolean)

    /** Show and hide filters */
    fun setFiltersVisibility(isVisible : Boolean)

    /** Show controls for continuous filter
     * @param values - init values for filter */
    fun showControls(filter : Filters, values : Any?)

    /** Hide controls for continuous filter */
    fun hideControls(filter : Filters)

    /** Show and hide Ok and Cancel buttons */
    fun setOkCancelButtonsVisibility(isVisible : Boolean)

    /** Show and hide Undo and Accept buttons */
    fun setUndoAcceptButtonsVisibility(isVisible : Boolean)

    /** Update image by new bitmap */
    fun updateImage(image : Bitmap)

    /** Update image by new bitmap */
    fun updateCropImage(image : Bitmap)

    /** Show and hide image */
    fun setImageVisibility(isVisible : Boolean)

    /** Show and hide image */
    fun setCropImageVisibility(isVisible : Boolean)

    /** Show close dialog */
    fun showCloseDialog()

    /** User accepts editing
     * @param pathToBitmap - path to bitmap file on disk*/
    fun workAccepted(pathToBitmap : String)

    /** User cancels editing */
    fun workCanceled()

    /** Get cropped bitmap */
    fun getCroppedBitmap() : Bitmap
}