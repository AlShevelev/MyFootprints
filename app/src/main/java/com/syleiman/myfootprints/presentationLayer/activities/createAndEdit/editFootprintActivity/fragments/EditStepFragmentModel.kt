package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments

import android.app.Activity
import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.businessLayer.sysInfoService.InternetConnectionStatuses
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.modelLayer.dto.FootprintSaveDto
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.ChosenPhotoInfo
import javax.inject.Inject

/** Model for footprint's editing (shared between photo and map)  */
class EditStepFragmentModel
@Inject
constructor(
    private val footprints : IFootprintsService,
    private val sysInfo : SystemInformationService)
{
    private var footprintId: Long = 0

    private lateinit var photoInfo: ChosenPhotoInfo         // Chosen photo

    var isTwitterOn: Boolean = false         // Is Twitter button turned on

    var isInstagramOn: Boolean = false         // Is Instagram button turned on

    var isSaveButtonEnabled: Boolean = false         // Is Instagram button turned on

    var comment: String? = null
        private set

    var currentPhoto: ChosenPhotoInfo
        get() = photoInfo
        set(photoInfo)
        {
            this.photoInfo = photoInfo
            isPhotoUpdated = true
        }

    private lateinit var footprintLocation: LatLng
    var location: LatLng
        get() = footprintLocation
        set(location)
        {
            footprintLocation = location
            isLocationUpdated = true
        }

    /** Check if map's snapshot needed  */
    val isNeedSnapshot: Boolean
        get() = !hasMapSnapshot || isLocationUpdated

    private var isInternetChecked = false         // Internet connection was checked

    private var isCommentUpdated = false
    private var isPhotoUpdated = false
    private var isLocationUpdated = false

    private var hasMapSnapshot: Boolean = false            // true if loaded footprint has map snapshot

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
    fun setComment(comment: String?, saveButtonStateCallback: Function1<Boolean, Unit>)
    {
        isCommentUpdated = true
        this.comment = comment
        isSaveButtonEnabled = (comment?.length ?: 0) > 0
        saveButtonStateCallback.invoke(isSaveButtonEnabled)
    }

    /**  */
    val isGooglePlayServicesAvailable: Boolean
        get() = sysInfo.isGooglePlayServicesAvailable()

    @Suppress("UNUSED_VARIABLE")
        /**
     * Load footprint from Db
     * @return true - success
     */
    fun loadFootprint(footprintId: Long): Boolean
    {
        val (id, comment1, creationDate, markerColor, photo, geo) = footprints.getById(footprintId) ?: return false

        this.footprintId = footprintId
        this.photoInfo = ChosenPhotoInfo(FileSingle.withName(photo!!.photoFileName!!).inPrivate(), false)
        this.comment = comment1
        this.footprintLocation = LatLng(geo!!.latitude, geo.longitude)

        this.hasMapSnapshot = photo.mapSnapshotFileName != null

        return true
    }

    /**
     * Update footprint
     * @param result callback (true - success; false - error or no update needed)
     */
    fun updateFootprint(context: Activity, mapSnapshot: Bitmap?, result: Function1<Boolean, Unit>)
    {
        if (isCommentUpdated || isLocationUpdated || isPhotoUpdated)
        {
            val footprint = FootprintSaveDto(
                footprintId,
                photoInfo.photo,
                photoInfo.isPhotoFromCamera,
                comment,
                footprintLocation,
                isTwitterOn,
                isInstagramOn,
                mapSnapshot)

            footprints.updateFootprint(context, footprint, isPhotoUpdated, isCommentUpdated, isLocationUpdated, result)
        } else
            result.invoke(false)
    }
}
