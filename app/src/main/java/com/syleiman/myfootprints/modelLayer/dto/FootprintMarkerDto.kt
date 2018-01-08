package com.syleiman.myfootprints.modelLayer.dto

/**
 * One footprint options on map
 */
data class FootprintMarkerDto(
        var footprintId: Long,
        var markerColor: Int,
        var latitude: Double,
        var longitude: Double,
        val footprintPhotoFileName: String,
        var description: String)
