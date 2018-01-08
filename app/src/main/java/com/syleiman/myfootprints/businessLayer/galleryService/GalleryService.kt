package com.syleiman.myfootprints.businessLayer.galleryService

import android.annotation.SuppressLint
import android.provider.MediaStore
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.modelLayer.dto.GalleryItemDto
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

/** Working with photos in phone's gallery  */
class GalleryService
@Inject
constructor() : IGalleryService
{
    /** Get url of all photos on phone sorted by creation date (in descending order)  */
    override fun getAllPhotoUrls(): Observable<List<String>>
    {
        return getAllPhotos().map { items ->
            items.sortedWith(GalleryItemsDateReversComparator()).map({ it.uri })
        }
    }

    /** Sort items by date in descending order */
    private class GalleryItemsDateReversComparator : Comparator<GalleryItemDto>
    {
        override fun compare(item1: GalleryItemDto, item2: GalleryItemDto): Int
        {
            return -item1.createDate.compareTo(item2.createDate)
        }
    }

    /** Get all photos from local device  */
    private fun getAllPhotos(): Observable<List<GalleryItemDto>> {
        return Observable.create { sub ->
            try {
                val galleryItems = getGalleryItems()

                if (galleryItems == null)
                    sub.onError(IllegalArgumentException("List of photos is empty"))
                else {
                    sub.onNext(galleryItems)
                    sub.onComplete()
                }
            } catch (ex: Exception) {
                sub.onError(ex)
            }
        }
    }

    /**
     * Get all photos from device
     * @return list or photos or null in case of error
     */
    @SuppressLint("Recycle")
    private fun getGalleryItems(): List<GalleryItemDto>?
    {
        val projection = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED)

        App.context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)!!.use { cursor ->
            val result = ArrayList<GalleryItemDto>(cursor.count)

            if (cursor.moveToFirst()) {
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                do {
                    val data = cursor.getString(dataColumn)
                    val dateAdded = cursor.getString(dateAddedColumn)
                    result.add(GalleryItemDto(FileSingle.fromPath(data).getUrl(), Date(dateAdded.toLong())))
                } while (cursor.moveToNext())
            }

            return result
        }
    }
}