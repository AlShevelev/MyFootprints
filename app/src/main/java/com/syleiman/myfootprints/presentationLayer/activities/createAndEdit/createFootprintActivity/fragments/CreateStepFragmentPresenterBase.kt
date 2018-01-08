package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments

import android.app.Activity
import android.graphics.Bitmap
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.UniversalRepository
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.ICreateFootprintActivityPresenterExt

/**
 * Base presenter for "create step" fragments
 */
abstract class CreateStepFragmentPresenterBase<out T : ICreateStepFragmentCallbacksBase>
protected constructor(
        protected val fragmentCallbacks: T,
        protected val presentersRepository: UniversalRepository,             // Flexible link of presenters
        protected val model: CreateStepFragmentModel)
{
    /** Close button clicked  */
    fun clickOnCloseButton(withQuestion: Boolean)
    {
        if (withQuestion)
            fragmentCallbacks.showQuery(R.string.create_footprint_dialog_cancel_footprint)
            {
                presentersRepository.getEntity(ICreateFootprintActivityPresenterExt::class.java)!!.close()
            }
        else
            presentersRepository.getEntity(ICreateFootprintActivityPresenterExt::class.java)!!.close()
    }

    /** Help button clicked  */
    fun clickOnHelpButton() = fragmentCallbacks.showMessage(R.string.message_map_menu_help)

    /**  */
    fun getTwitterButtonState(): Boolean = model.isTwitterOn
    fun setTwitterButtonState(isOn : Boolean)
    {
        model.isTwitterOn = isOn
    }

    /**  */
    /**  */
    fun getInstagramButtonState(): Boolean= model.isInstagramOn
    fun setInstagramButtonState(isOn : Boolean)
    {
        model.isInstagramOn = isOn
    }

    /**  */
    /**  */
    fun getSaveButtonState(): Boolean = model.isSaveButtonEnabled
    fun setSaveButtonState(isEnabled : Boolean)
    {
        model.isSaveButtonEnabled = isEnabled
    }

    /**
     * Create footprint
     * @param result callback (true - success)
     */
    fun createFootprint(context: Activity, mapSnapshot: Bitmap?, result: Function1<Boolean, Unit>)
    {
        model.createFootprint(context, mapSnapshot, result)
    }
}