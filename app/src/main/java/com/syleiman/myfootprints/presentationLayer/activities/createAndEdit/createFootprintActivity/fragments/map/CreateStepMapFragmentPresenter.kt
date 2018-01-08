package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.map

import android.content.Context
import android.location.Location
import android.view.View

import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast.GeoLocationReceiver
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast.IGeoLocationChannel
import com.syleiman.myfootprints.presentationLayer.commonUIHelper.ICommonUIHelper
import com.syleiman.myfootprints.common.UniversalRepository
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.ICreateFootprintActivityPresenterExt
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.CreateStepFragmentModel
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.CreateStepFragmentPresenterBase

import javax.inject.Inject

class CreateStepMapFragmentPresenter
@Inject
constructor(
        fragmentCallbacks: ICreateStepMapFragmentCallbacks,
        presentersRepository: UniversalRepository,
        model: CreateStepFragmentModel,
        private val commonUI: ICommonUIHelper)
    : CreateStepFragmentPresenterBase<ICreateStepMapFragmentCallbacks>(
        fragmentCallbacks,
        presentersRepository,
        model), ICreateStepMapFragmentPresenterExt
{
    init
    {
        presentersRepository.registerEntity(ICreateStepMapFragmentPresenterExt::class.java, this)
    }

    /** When map showed  */
    fun onShow(view : View, context : Context)
    {
        commonUI.closeSoftKeyboard(view, context)
    }

    /** Switch to photo  */
    fun switchToPhoto() = presentersRepository.getEntity(ICreateFootprintActivityPresenterExt::class.java)!!.switchToCreateStepPhotoFromMap()     // Switch to photo

    /**  */
    val isInternetEnabled: Boolean
        get() = model.isInternetEnabled

    /**  */
    val lastLocation: Location
        get() = model.lastLocation

    /**  */
    val isGooglePlayServicesAvailable: Boolean
        get() = model.isGooglePlayServicesAvailable

    /**  */
    fun getLocationsReceiver(channel: IGeoLocationChannel): GeoLocationReceiver = model.getLocationsReceiver(channel)

    /** Store footprint location  */
    fun storeLocation(location: LatLng) = model.storeLocation(location)

    /** Save footprint  */
    override fun save() = fragmentCallbacks.save()
}
