package com.syleiman.myfootprints.modelLayer.dto

import com.syleiman.myfootprints.modelLayer.dbEntities.DbFootprintGeo

/** Geographic information about footprint (place)  */
data class FootprintGeoDto(
        var id: Long,
        var latitude: Double,
        var longitude: Double,
        var countryName: String?,
        var countryCode: String?,
        var cityName: String?) {

    constructor(dbFootprintGeo: DbFootprintGeo) : this(
        dbFootprintGeo.id!!,
        dbFootprintGeo.latitude,
        dbFootprintGeo.longitude,
        dbFootprintGeo.countryName,
        dbFootprintGeo.countryCode,
        dbFootprintGeo.cityName)
}
