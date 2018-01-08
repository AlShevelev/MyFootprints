package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapEffectsProcessing
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.RGB
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView

/** Processor for ColorBalance effect */
class ColorBalanceProcessor(effectsProcessor : IBitmapEffectsProcessing) : ProcessorBase(effectsProcessor)
{
    private var rFactor : Double = 100.0             // [0; 150]
    private var gFactor : Double = 100.0
    private var bFactor : Double = 100.0

    /** Set start effect */
    override fun setStartEffect(sourceBitmap: Bitmap): Bitmap = effectsProcessor.colorFilter(sourceBitmap, rFactor, gFactor, bFactor)

    /** Place effect on bitmap
     * @param effectData - color as RGB<Double> */
    @Suppress("UNCHECKED_CAST")
    override fun setEffect(sourceBitmap: Bitmap, effectData: Any): Bitmap
    {
        val effectValue = effectData as RGB<Double>

        rFactor = effectValue.R ?: rFactor
        gFactor = effectValue.G ?: gFactor
        bFactor = effectValue.B ?: bFactor

        return effectsProcessor.colorFilter(sourceBitmap, rFactor, gFactor, bFactor)
    }

    /** Show/Hide filter's controls */
    override fun setControlsVisibility(view: IPhotoEditorView, isVisible: Boolean)
    {
        if(isVisible)
            view.showControls(Filters.ColorBalance, RGB(rFactor, gFactor, bFactor))
        else
            view.hideControls(Filters.ColorBalance)
    }

    /** True if set effect needed when start to use filter */
    override fun needSetEffectOnStart(): Boolean = false
}