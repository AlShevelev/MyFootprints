package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map

import android.content.Context
import android.view.View

import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.presentationLayer.commonUIHelper.ICommonUIHelper
import com.syleiman.myfootprints.common.UniversalRepository
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.IEditFootprintActivityPresenterExt
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.EditStepFragmentModel
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.EditStepFragmentPresenterBase

import javax.inject.Inject

class EditStepMapFragmentPresenter
@Inject
constructor(
        fragmentCallbacks: IEditStepMapFragmentCallbacks,
        presentersRepository: UniversalRepository,
        model: EditStepFragmentModel,
        private val commonUI: ICommonUIHelper)
    : EditStepFragmentPresenterBase<IEditStepMapFragmentCallbacks>(
        fragmentCallbacks,
        presentersRepository,
        model), IEditStepMapFragmentPresenterExt, IEditStepMapFragmentPresenterMap
{
    init
    {
        presentersRepository.registerEntity(IEditStepMapFragmentPresenterExt::class.java, this)
    }

    /** When map showed  */
    fun onShow(view : View, context : Context)
    {
        commonUI.closeSoftKeyboard(view, context)
    }

    /** Switch to photo  */
    fun switchToPhoto() = presentersRepository.getEntity(IEditFootprintActivityPresenterExt::class.java)!!.switchToEditStepPhotoFromMap()     // Switch to photo

    /**  */
    val isInternetEnabled: Boolean
        get() = model.isInternetEnabled

    /**  */
    val lastLocation: LatLng
        get() = model.location

    /**  */
    val isGooglePlayServicesAvailable: Boolean
        get() = model.isGooglePlayServicesAvailable

    /** Store footprint location  */
    fun storeLocation(location: LatLng)
    {
        model.location = location
    }

    /** Save footprint  */
    override fun save() = fragmentCallbacks.save()

    /** Check if map's snapshot needed  */
    override fun isNeedSnapshot(): Boolean = model.isNeedSnapshot
}
