package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments

import android.app.Activity
import android.graphics.Bitmap
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.UniversalRepository
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.IEditFootprintActivityPresenterExt

/** Base presenter for "edit step" fragments  */
open class EditStepFragmentPresenterBase<out T : IEditStepFragmentCallbacksBase> protected constructor(
        protected val fragmentCallbacks: T,
        protected val presentersRepository: UniversalRepository,             // Flexible link of presenters
        protected val model: EditStepFragmentModel)
{

    /** Close button clicked  */
    fun clickOnCloseButton(withQuestion: Boolean, saved: Boolean)
    {
        if (withQuestion)
            fragmentCallbacks.showQuery(R.string.edit_footprint_back_to_gallery) {
                presentersRepository.getEntity(IEditFootprintActivityPresenterExt::class.java)!!.close(saved)
            }
        else
            presentersRepository.getEntity(IEditFootprintActivityPresenterExt::class.java)!!.close(saved)
    }

    /** Help button clicked  */
    fun clickOnHelpButton() = fragmentCallbacks.showMessage(R.string.edit_footprint_back_help)

    /**  */
    fun getTwitterButtonState(): Boolean = model.isTwitterOn
    fun setTwitterButtonState(isOn : Boolean)
    {
        model.isTwitterOn = isOn
    }

    /**  */
    fun getInstagramButtonState(): Boolean = model.isInstagramOn
    fun setInstagramButtonState(isOn : Boolean)
    {
        model.isInstagramOn = isOn
    }

    /**  */
    fun getSaveButtonState(): Boolean= model.isSaveButtonEnabled
    fun setSaveButtonState(isEnabled : Boolean)
    {
        model.isSaveButtonEnabled = isEnabled
    }

    /**
     * Update footprint
     * @param result callback (true - success)
     */
    fun updateFootprint(context: Activity, mapSnapshot: Bitmap?, result: Function1<Boolean, Unit>)
    {
        model.updateFootprint(context, mapSnapshot, result)
    }

    /**
     * Load footprint and init model
     * @return true - success
     */
    protected fun loadFootprint(footprintId: Long): Boolean
    {
        return model.loadFootprint(footprintId)
    }
}
