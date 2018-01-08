package com.syleiman.myfootprints.modelLayer.dto

import android.graphics.Bitmap

import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.common.files.IFileSingleOperation

/** Data for saveToPrivateArea footprint  */
data class FootprintSaveDto(
    var footprintId: Long?,                 // Id of footprint
    var photoFile: IFileSingleOperation,    // File with photo
    var isPhotoFromCamera: Boolean,         // Is it photo from Camera or other source
    var comment: String? = null,            // Text of comment
    var location: LatLng?,                  // Location
    var isTwitterTurnOn: Boolean,           // Is send to Twitter turn on
    var isInstagramTurnOn: Boolean,         // isInstagramTurnOn
    var mapSnapshot: Bitmap?) {             // Snapshot of map (can be null)

    constructor(
        photoFile: IFileSingleOperation,
        isPhotoFromCamera: Boolean,
        comment: String? = null,
        location: LatLng?,
        isTwitterTurnOn: Boolean,
        isInstagramTurnOn: Boolean,
        mapSnapshot: Bitmap?) : this(
            null,
        photoFile,
            isPhotoFromCamera,
            comment,
            location,
            isTwitterTurnOn,
            isInstagramTurnOn,
            mapSnapshot)
}
