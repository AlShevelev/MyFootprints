package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapEffectsProcessing

/** Base processor for all processors */
abstract class ProcessorBase(protected val effectsProcessor : IBitmapEffectsProcessing) : IContinuousFilterProcessor
{
    private lateinit var sourceBitmap: Bitmap
    private lateinit var lastResult: Bitmap

    /** Effect was set */
    final override var effectWasSet : Boolean = false
    private set

    /** Set source bitmap */
    final override fun init(sourceBitmap: Bitmap)
    {
        this.sourceBitmap = sourceBitmap            // Can't use constructor as damned TintProcessor has got a state
    }

    /** Set effect on start filtering */
    final override fun setEffectOnStart(): Bitmap
    {
        lastResult = setStartEffect(copy(sourceBitmap))
        effectWasSet = true
        return lastResult
    }

    private fun copy(bitmap : Bitmap) : Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    /** Set effect */
    final override fun setEffect(effectData : Any) : Bitmap
    {
        effectWasSet = true

        lastResult = setEffect(copy(sourceBitmap), effectData)
        return lastResult
    }

    /** Get result bitmap */
    final override fun getResult() : Bitmap = lastResult

    /** Set start effect */
    protected abstract fun setStartEffect(sourceBitmap: Bitmap) : Bitmap

    /** Place effect on bitmap */
    protected abstract fun setEffect(sourceBitmap: Bitmap, effectData : Any) : Bitmap
}