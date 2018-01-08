package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapEffectsProcessing
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView

/** Processor for Tint effect */
class TintProcessor(effectsProcessor : IBitmapEffectsProcessing) : ProcessorBase(effectsProcessor)
{
    private var tintColor = 0xFF1E8D24.toInt()             // Start color

    /** Set start effect */
    override fun setStartEffect(sourceBitmap: Bitmap): Bitmap = effectsProcessor.tint(sourceBitmap, tintColor)

    /** Place effect on bitmap
     * @param effectData - color as Int */
    override fun setEffect(sourceBitmap: Bitmap, effectData: Any): Bitmap
    {
        tintColor =  effectData as Int             // Memorize color
        return effectsProcessor.tint(sourceBitmap, tintColor)
    }

    /** Show/Hide filter's controls */
    override fun setControlsVisibility(view: IPhotoEditorView, isVisible: Boolean)
    {
        if(isVisible)
            view.showControls(Filters.Tint, tintColor)
        else
            view.hideControls(Filters.Tint)
    }

    /** True if set effect needed when start to use filter */
    override fun needSetEffectOnStart(): Boolean = true
}