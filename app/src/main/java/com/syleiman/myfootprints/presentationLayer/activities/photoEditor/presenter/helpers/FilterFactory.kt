package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.helpers

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapEffectsProcessing
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters.*

/** Helper class for creating filters */
class FilterFactory(private val bitmapService : IBitmapService)
{
    val  effectsProcessor : IBitmapEffectsProcessing = bitmapService.getBitmapEffectsProcessing()
    val tintProcessor : IContinuousFilterProcessor = TintProcessor(effectsProcessor)            // This processor has got a state

    /** Get action for processing bitmap in instant filter*/
    fun getAction(filter : Filters) : (Bitmap) -> Bitmap
    {
        bitmapService.getBitmapEffectsProcessing()
        when(filter)
        {
            Filters.Emboss -> return { effectsProcessor.emboss(it)}
            Filters.GaussianBlur -> return { effectsProcessor.gaussian(it) }
            Filters.Grayscale -> return { effectsProcessor.grayscale(it) }
            Filters.Invert -> return { effectsProcessor.invert(it) }
            Filters.Noise -> return { effectsProcessor.noise(it) }
            Filters.Sepia -> return { effectsProcessor.sepia(it) }
            Filters.Sharpen -> return { effectsProcessor.sharpen(it) }
            Filters.Sketch -> return { effectsProcessor.sketch(effectsProcessor.grayscale(it)) }
            Filters.Vignette -> return { effectsProcessor.vignette(it) }

            else -> throw UnsupportedOperationException("This filter is not supported: "+filter)
        }
    }

    /** Get processor for continuous filter */
    fun getContinuousProcessor(filter : Filters) : IContinuousFilterProcessor
    {
        when(filter)
        {
            Filters.Boost -> return BoostProcessor(effectsProcessor)
            Filters.Brightness -> return BrightnessProcessor(effectsProcessor)
            Filters.ColorDepth -> return ColorDepthProcessor(effectsProcessor)
            Filters.ColorBalance -> return ColorBalanceProcessor(effectsProcessor)
            Filters.Contrast -> return ContrastProcessor(effectsProcessor)
            Filters.Flip -> return FlipProcessor(effectsProcessor)
            Filters.Gamma -> return GammaProcessor(effectsProcessor)
            Filters.Hue -> return HueProcessor(effectsProcessor)
            Filters.Saturation -> return SaturationProcessor(effectsProcessor)
            Filters.Tint -> return tintProcessor

            else -> throw UnsupportedOperationException("This filter is not supported: "+filter)
        }
    }
}
