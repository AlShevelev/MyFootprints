package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid

import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.common.UniversalRepository
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.common.iif
import com.syleiman.myfootprints.common.letNull
import com.syleiman.myfootprints.modelLayer.enums.BitmapsQuality
import com.syleiman.myfootprints.presentationLayer.activities.InternalActivitiesCodes
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.ICreateFootprintActivityPresenterExt
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.IEditFootprintActivityPresenterExt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class PhotosGridFragmentPresenter
@Inject
constructor(
        private val fragmentCallbacks: IPhotosGridFragmentCallbacks,
        private val presentersRepository: UniversalRepository,  // Flexible link of presenters
        private val model: PhotosGridFragmentModel,
        private val bitmapService : IBitmapService,
        private val systemInformation : ISystemInformationService,
        private val activityCode: InternalActivitiesCodes) : IPhotosGridFragmentPresenterExt, IPhotosGridFragmentPresenterGridClicks
{
    private var canRefresh = true

    init
    {
        presentersRepository.registerEntity(IPhotosGridFragmentPresenterExt::class.java, this)
    }

    /** Init view  */
    fun init() = refreshGrid()

    /** Refresh photos grid  */
    fun refreshGrid()
    {
        if (!canRefresh)
            return

        canRefresh = false
        fragmentCallbacks.showProgress()
        model.getPhotosList { result ->
            if (result == null)
                fragmentCallbacks.showGrid(ArrayList<String>())      // Show empty list in case of error
            else
                fragmentCallbacks.showGrid(result)
            canRefresh = true
        }
    }

    /**
     * Click on photo
     * @param indexOfPhoto index of photo in the list
     */
    override fun clickOnPhoto(indexOfPhoto: Int)
    {
        val photoUrl = model.getPhotoByIndex(indexOfPhoto)
        processPhoto(photoUrl, false)
    }

    private fun processPhoto(photoUrl: String?, isPhotoFromCamera: Boolean)
    {
        fragmentCallbacks.setProgressFullScreenVisibility(true)

        if (photoUrl == null)
        {
            fragmentCallbacks.showMessage(R.string.message_box_cant_read_this_photo)
            fragmentCallbacks.setProgressFullScreenVisibility(false)
        }
        else
        {
            // Resize bitmap
            val screenSize = systemInformation.getScreenSize((fragmentCallbacks as Fragment).activity)
            val file = isPhotoFromCamera.iif({FileSingle.fromUrl(photoUrl)} , {FileSingle.withRandomName(FileSingle.TEMP_FILES_PREFIX, "jpg").inShared()})

            bitmapService.loadAndInscribeStrictRx(FileSingle.fromUrl(photoUrl), screenSize.x, screenSize.y).
                doOnSuccess { resizedBitmap -> bitmapService.save(resizedBitmap, file, BitmapsQuality.High, Bitmap.CompressFormat.JPEG) }.
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe({
                    when (activityCode)
                    {
                        InternalActivitiesCodes.CreateFootprint -> presentersRepository.getEntity(ICreateFootprintActivityPresenterExt::class.java)!!.switchToCreateStepPhotoFromPhotoGrid()
                        InternalActivitiesCodes.EditFootprint -> presentersRepository.getEntity(IEditFootprintActivityPresenterExt::class.java)!!.switchToEditStepPhotoFromPhotoGrid()
                        else -> throw UnsupportedOperationException("This code is not supported: "+ activityCode)
                    }

                    val photoInfo = ChosenPhotoInfo(file, isPhotoFromCamera)

                    val createStepPresenter = presentersRepository.getEntity(ICreateEditStepPhotoFragmentPresenter::class.java)

                    createStepPresenter.letNull(
                        {it!!.setPhoto(photoInfo)},           // vvvvvv First call - presenter not exists yet - store url of photo as temp value
                        {presentersRepository.setTempValue(ICreateEditStepPhotoFragmentPresenter::class.java, photoInfo)})

                    fragmentCallbacks.setProgressFullScreenVisibility(false)
                }, { s ->
                    s.printStackTrace()    // Error
                    fragmentCallbacks.setProgressFullScreenVisibility(false)
                })
        }
    }

    /** Click on photo camera  */
    override fun startCapturePhoto()
    {
        val result = model.getPhotoCapture().startCapturePhoto()
        if (!result)
            fragmentCallbacks.showMessage(R.string.message_box_cant_capture_photo)
    }

    /** Complete capture photo from camera  */
    fun completeCapturePhoto() = processPhoto(model.getPhotoCapture().capturedFileUrl, true)

    /** Click on gallery  */
    override fun startGetPhotoFromGallery()
    {
        val result = model.getPhotoFromGallery().startGetPhoto()
        if (!result)
            fragmentCallbacks.showMessage(R.string.message_box_cant_access_gallery)
    }

    /** Complete getting photo from gallery  */
    fun completeGetPhotoFromGallery(data: Intent) = processPhoto(model.getPhotoFromGallery().decodeFileUrl(data), false)
}