package com.syleiman.myfootprints.businessLayer.galleryService

import io.reactivex.Observable


/** Working with photos in phone's gallery  */
interface IGalleryService
{
    /** Get urls of all photos on phone sorted by creation date (in descending order)  */
    fun getAllPhotoUrls(): Observable<List<String>>
}
