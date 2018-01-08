package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapEffectsProcessing
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView

/** Processor for Hue effect */
class HueProcessor(effectsProcessor : IBitmapEffectsProcessing) : ProcessorBase(effectsProcessor)
{
    private var hueFactor : Float = 180f             // Start hue factor [0; 360]

    /** Set start effect */
    override fun setStartEffect(sourceBitmap: Bitmap): Bitmap = effectsProcessor.hue(sourceBitmap, hueFactor)

    /** Place effect on bitmap
     * @param effectData - hue factor as Float [0; 360] */
    override fun setEffect(sourceBitmap: Bitmap, effectData: Any): Bitmap
    {
        hueFactor =  effectData as Float             // Memorize hue factor
        return effectsProcessor.hue(sourceBitmap, hueFactor)
    }

    /** Show/Hide filter's controls */
    override fun setControlsVisibility(view: IPhotoEditorView, isVisible: Boolean)
    {
        if(isVisible)
            view.showControls(Filters.Hue, hueFactor)
        else
            view.hideControls(Filters.Hue)
    }

    /** True if set effect needed when start to use filter */
    override fun needSetEffectOnStart(): Boolean = true
}