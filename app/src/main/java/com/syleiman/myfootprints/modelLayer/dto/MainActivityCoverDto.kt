package com.syleiman.myfootprints.modelLayer.dto

import android.graphics.Bitmap

/** Cover for main activity  */
class MainActivityCoverDto {
    var totalFootprints: Int = 0

    /** File name of last photo (use when saveToPrivateArea)  */
    var lastPhotoFileName: String? = null

    /** Last photo as bitmap (use when loadAndInscribeQuick)  */
    var lastPhotoBitmap: Bitmap? = null

    /** Id of last footprint (null if no last footprint)  */
    var lastFootprintId: Long? = null
}
