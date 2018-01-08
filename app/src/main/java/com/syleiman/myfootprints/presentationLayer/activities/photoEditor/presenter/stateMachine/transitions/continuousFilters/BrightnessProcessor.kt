package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapEffectsProcessing
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView

/** Processor for Brightness effect */
class BrightnessProcessor(effectsProcessor : IBitmapEffectsProcessing) : ProcessorBase(effectsProcessor)
{
    private var brightnessFactor = 0             // Start brightness [-200, +200]

    /** Set start effect */
    override fun setStartEffect(sourceBitmap: Bitmap): Bitmap = effectsProcessor.brightness(sourceBitmap, brightnessFactor)

    /** Place effect on bitmap
     * @param effectData - Int [-200, +200] */
    override fun setEffect(sourceBitmap: Bitmap, effectData: Any): Bitmap
    {
        brightnessFactor =  effectData as Int             // Memorize brightness
        return effectsProcessor.brightness(sourceBitmap, brightnessFactor)
    }

    /** Show/Hide filter's controls */
    override fun setControlsVisibility(view: IPhotoEditorView, isVisible: Boolean)
    {
        if(isVisible)
            view.showControls(Filters.Brightness, brightnessFactor)
        else
            view.hideControls(Filters.Brightness)
    }

    /** True if set effect needed when start to use filter */
    override fun needSetEffectOnStart(): Boolean = false
}