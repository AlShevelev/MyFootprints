package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments

import android.app.Activity
import android.graphics.Bitmap
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast.GeoLocationReceiver
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast.IGeoLocationChannel
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.businessLayer.geoLocationService.IGeoLocationService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.businessLayer.sysInfoService.InternetConnectionStatuses
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.modelLayer.dto.FootprintSaveDto
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.ChosenPhotoInfo
import javax.inject.Inject

/** Model for footprint's creation (shared between photo and map) */
class CreateStepFragmentModel
@Inject
constructor(
    private val geoLocation: IGeoLocationService,
    private val sysInfo : ISystemInformationService,
    private val footprints : IFootprintsService)
{
    private var photoInfo: ChosenPhotoInfo? = null         // Chosen photo

    var isTwitterOn: Boolean = false         // Is Twitter button turned on

    var isInstagramOn: Boolean = false         // Is Instagram button turned on

    var isSaveButtonEnabled: Boolean = false         // Is Instagram button turned on

    private var isInternetChecked = false         // Internet connection was checked

    private var footprintComment: String? = null
    private var footprintLocation: LatLng? = null

    /** */
    fun getCurrentPhoto() : ChosenPhotoInfo?
    {
        return photoInfo
    }

    /** */
    fun setCurrentPhoto(photoInfo: ChosenPhotoInfo)
    {
        this.photoInfo = photoInfo
    }

    /** Check is internet connection is established. Only once to avoid user's negative  */
    val isInternetEnabled: Boolean
        get()
        {
            if (isInternetChecked)
                return true

            val result = SystemInformationService.getInternetConnectionStatus() !== InternetConnectionStatuses.None
            isInternetChecked = true
            return result
        }

    /**
     * Memorize comment
     * @param saveButtonStateCallback - true - Save button enabled
     */
    fun storeComment(comment: String?, saveButtonStateCallback: Function1<Boolean, Unit>)
    {
        footprintComment = comment
        isSaveButtonEnabled = (comment?.length ?: 0) > 0
        saveButtonStateCallback.invoke(isSaveButtonEnabled)
    }

    /** Store footprint location  */
    fun storeLocation(location: LatLng)
    {
        footprintLocation = location
    }

    /**  */
    val lastLocation: Location
        get() = geoLocation.getLastLocation()

    /**  */
    val isGooglePlayServicesAvailable: Boolean
        get() = sysInfo.isGooglePlayServicesAvailable()

    /**  */
    fun getLocationsReceiver(channel: IGeoLocationChannel): GeoLocationReceiver
    {
        return geoLocation.getLocationsReceiver(channel)
    }

    /**
     * Create footprint
     * @param result callback (true - success)
     */
    fun createFootprint(context: Activity, mapSnapshot: Bitmap?, result: Function1<Boolean, Unit>)
    {
        val footprint = FootprintSaveDto(
            photoInfo!!.photo,
            photoInfo!!.isPhotoFromCamera,
            footprintComment,
            if (footprintLocation != null) footprintLocation else convertLocation(geoLocation.getLastLocation()),
            isTwitterOn,
            isInstagramOn,
            mapSnapshot)

        footprints.createFootprint(context, footprint, result)
    }

    private fun convertLocation(location: Location): LatLng = LatLng(location.latitude, location.longitude)
}