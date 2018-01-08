package com.syleiman.myfootprints.modelLayer.dto

import com.syleiman.myfootprints.modelLayer.dbEntities.DbFootprint

import java.util.Date

/** One footprint  */
data class FootprintDto(
        var id: Long,
        var comment: String?,
        var creationDate: Date,
        var markerColor: Int,
        var photo: FootprintPhotoDto?,
        var geo: FootprintGeoDto?) {

    constructor(dbFootprint: DbFootprint) : this(
        dbFootprint.id!!,
        dbFootprint.comment!!,
        dbFootprint.creationDate!!,
        dbFootprint.markerColor,
        null,
        null)
}
