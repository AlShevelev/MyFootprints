package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapEffectsProcessing
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView

/** Processor for Contrast effect */
class ContrastProcessor(effectsProcessor : IBitmapEffectsProcessing) : ProcessorBase(effectsProcessor)
{
    private var contrastFactor = 0.0             // Start contrast [-50, +50]

    /** Set start effect */
    override fun setStartEffect(sourceBitmap: Bitmap): Bitmap = effectsProcessor.contrast(sourceBitmap, contrastFactor)

    /** Place effect on bitmap
     * @param effectData - color as Double [-100, +100] */
    override fun setEffect(sourceBitmap: Bitmap, effectData: Any): Bitmap
    {
        contrastFactor =  effectData as Double             // Memorize contrast
        return effectsProcessor.contrast(sourceBitmap, contrastFactor)
    }

    /** Show/Hide filter's controls */
    override fun setControlsVisibility(view: IPhotoEditorView, isVisible: Boolean)
    {
        if(isVisible)
            view.showControls(Filters.Contrast, contrastFactor)
        else
            view.hideControls(Filters.Contrast)
    }

    /** True if set effect needed when start to use filter */
    override fun needSetEffectOnStart(): Boolean = false
}