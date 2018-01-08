package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapEffectsProcessing
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView

/** Processor for Saturation effect */
class SaturationProcessor(effectsProcessor : IBitmapEffectsProcessing) : ProcessorBase(effectsProcessor)
{
    private var saturationFactor = 100             // [0, 200]

    /** Set start effect */
    override fun setStartEffect(sourceBitmap: Bitmap): Bitmap = effectsProcessor.saturation(sourceBitmap, saturationFactor)

    /** Place effect on bitmap
     * @param effectData - saturation as Int */
    override fun setEffect(sourceBitmap: Bitmap, effectData: Any): Bitmap
    {
        saturationFactor =  effectData as Int             // Memorize saturation
        return effectsProcessor.saturation(sourceBitmap, saturationFactor)
    }

    /** Show/Hide filter's controls */
    override fun setControlsVisibility(view: IPhotoEditorView, isVisible: Boolean)
    {
        if(isVisible)
            view.showControls(Filters.Saturation, saturationFactor)
        else
            view.hideControls(Filters.Saturation)
    }

    /** True if set effect needed when start to use filter */
    override fun needSetEffectOnStart(): Boolean = false
}