package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapEffectsProcessing
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.BoostInfo
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.ColorComponents
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView

/** Processor for Boost effect */
class BoostProcessor(effectsProcessor : IBitmapEffectsProcessing) : ProcessorBase(effectsProcessor)
{
    private var boostInfo = BoostInfo(ColorComponents.R, 100f)      // Start boost info

    /** Set start effect */
    override fun setStartEffect(sourceBitmap: Bitmap): Bitmap = effectsProcessor.boost(sourceBitmap, boostInfo.colorComponent.value, boostInfo.percent)

    /** Place effect on bitmap
     * @param effectData - color as Int */
    override fun setEffect(sourceBitmap: Bitmap, effectData: Any): Bitmap
    {
        boostInfo =  effectData as BoostInfo            // Memorize boost info
        return effectsProcessor.boost(sourceBitmap, boostInfo.colorComponent.value, boostInfo.percent)
    }

    /** Show/Hide filter's controls */
    override fun setControlsVisibility(view: IPhotoEditorView, isVisible: Boolean)
    {
        if(isVisible)
            view.showControls(Filters.Boost, boostInfo)
        else
            view.hideControls(Filters.Boost)
    }

    /** True if set effect needed when start to use filter */
    override fun needSetEffectOnStart(): Boolean = true
}