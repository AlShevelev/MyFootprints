package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid

import com.syleiman.myfootprints.businessLayer.galleryService.IGalleryService
import com.syleiman.myfootprints.presentationLayer.externalIntents.camera.PhotoCapture
import com.syleiman.myfootprints.presentationLayer.externalIntents.gallery.PhotoFromGallery
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotosGridFragmentModel
@Inject
constructor(private val gallery  : IGalleryService, private val photoCapture: PhotoCapture, private val photoFromGallery: PhotoFromGallery)
{
    private var cachedPhotos: List<String>? = null            // Urls of photos

    /** */
    fun getPhotoCapture(): PhotoCapture = photoCapture

    /** */
    fun getPhotoFromGallery(): PhotoFromGallery = photoFromGallery

    /** Get list of photos urls on local device */
    fun getPhotosList(resultCallback: Function1<List<String>?, Unit>)
    {
        gallery.getAllPhotoUrls().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        { result ->
            cachedPhotos = result
            resultCallback.invoke(result)
        }
        ) { s ->
            s.printStackTrace()
            cachedPhotos = null
            resultCallback.invoke(null)
        }
    }

    /** Get url of photo by index (from 0) */
    fun getPhotoByIndex(index: Int): String?
    {
        if (cachedPhotos == null)
            return null
        return cachedPhotos!![index]
    }
}