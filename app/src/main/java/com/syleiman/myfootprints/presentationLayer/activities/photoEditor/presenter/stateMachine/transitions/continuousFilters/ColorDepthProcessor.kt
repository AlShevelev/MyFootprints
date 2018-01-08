package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapEffectsProcessing
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView

/** Processor for ColorDepthProcessor effect */
class ColorDepthProcessor(effectsProcessor : IBitmapEffectsProcessing) : ProcessorBase(effectsProcessor)
{
    private var colorDepthFactor : Int = 32 // 16, 32, 64, 128

    private val possibleValues : Array<Int> = arrayOf(16, 32, 64, 128)

    /** Set start effect */
    override fun setStartEffect(sourceBitmap: Bitmap): Bitmap = effectsProcessor.colorDepth(sourceBitmap, colorDepthFactor)

    /** Place effect on bitmap */
    override fun setEffect(sourceBitmap: Bitmap, effectData: Any): Bitmap
    {
        val effectValue = effectData as Int

        if(!possibleValues.any({it==effectValue}))
            throw UnsupportedOperationException("This value is not supported: "+effectValue)

        return effectsProcessor.colorDepth(sourceBitmap, effectValue)
    }

    /** Show/Hide filter's controls */
    override fun setControlsVisibility(view: IPhotoEditorView, isVisible: Boolean)
    {
        if(isVisible)
            view.showControls(Filters.ColorDepth, colorDepthFactor)
        else
            view.hideControls(Filters.ColorDepth)
    }

    /** True if set effect needed when start to use filter */
    override fun needSetEffectOnStart(): Boolean = true
}