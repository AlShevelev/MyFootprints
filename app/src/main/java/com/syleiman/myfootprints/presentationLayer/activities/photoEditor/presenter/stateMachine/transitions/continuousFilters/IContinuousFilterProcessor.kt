package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters

import android.graphics.Bitmap
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView

/** Interface for continuous filters logic */
interface IContinuousFilterProcessor
{
    /** Show/Hide filter's controls */
    fun setControlsVisibility(view : IPhotoEditorView, isVisible : Boolean)

    /** Set source bitmap */
    fun init(sourceBitmap: Bitmap)

    /** Set effect */
    fun setEffect(effectData : Any) : Bitmap

    /** Set effect on start filtering */
    fun setEffectOnStart() : Bitmap

    /** True if set effect needed when start to use filter */
    fun needSetEffectOnStart() : Boolean

    /** Get result bitmap */
    fun getResult() : Bitmap

    /** Effect was set */
    val effectWasSet : Boolean
}
