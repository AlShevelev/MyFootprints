package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter

import android.graphics.Bitmap
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView

/** Interface of presenter for PhotoEditorActivity*/
interface IPhotoEditorPresenter
{
    /** Set start bitmap */
    fun setSource(bitmap : Bitmap)

    /** User tap on image */
    fun onTapOnImage()

    /** Back button pressed */
    fun onBackPressed()

    /** Yes button in close dialog */
    fun onYesCloseButton()

    /** No button in close dialog */
    fun onNoCloseButton()

    /** User choose one of the filters (instant or continuous)*/
    fun onChooseFilter(filter : Filters)

    /** Accept button (user ends editing and gets result) */
    fun onAcceptButton()

    /** User press Undo button */
    fun onUndoButton()

    /** Ok button in continuous filter */
    fun onOkContinuousButton()

    /** Cancel button in continuous filter */
    fun onCancelContinuousButton()

    /** User do some action on continuous filter control */
    fun onContinuousControlAction(actionInfo : Any)

    /** Get last stored bitmap */
    fun getLastBitmap() : Bitmap

    /** Cancel continuous operation */
    fun resetBeforeRestart()

    /** Set new view */
    fun initAfterRestart(view : IPhotoEditorView)

    /** */
    fun attachView(view : IPhotoEditorView)

    /** */
    fun detachView()
}