package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapEffectsProcessing
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.FlipInfo
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView

/** Processor for Flip effect */
class FlipProcessor(effectsProcessor : IBitmapEffectsProcessing) : ProcessorBase(effectsProcessor)
{
    private var flipH = false             // Horizontal
    private var flipV = false             // Vertical

    /** Set start effect */
    override fun setStartEffect(sourceBitmap: Bitmap): Bitmap = effectsProcessor.flip(sourceBitmap, flipH, flipV)

    /** Place effect on bitmap
     * @param effectData - FlipInfo */
    override fun setEffect(sourceBitmap: Bitmap, effectData: Any): Bitmap
    {
        val flipFactor =  effectData as FlipInfo             // Memorize flip data

        flipH = flipFactor.flipH ?: flipH
        flipV = flipFactor.flipV ?: flipV

        return effectsProcessor.flip(sourceBitmap, flipV, flipH)
    }

    /** Show/Hide filter's controls */
    override fun setControlsVisibility(view: IPhotoEditorView, isVisible: Boolean)
    {
        if(isVisible)
            view.showControls(Filters.Flip, null)
        else
            view.hideControls(Filters.Flip)
    }

    /** True if set effect needed when start to use filter */
    override fun needSetEffectOnStart(): Boolean = false
}