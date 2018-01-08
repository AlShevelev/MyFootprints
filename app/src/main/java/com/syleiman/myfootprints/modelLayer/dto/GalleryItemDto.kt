package com.syleiman.myfootprints.modelLayer.dto

import java.util.Date

/** One photo from gallery  */
data class GalleryItemDto
(
    /** Uri to photos' file on device  */
    var uri: String,
    /** Date of photos' creation  */
    var createDate: Date
)
