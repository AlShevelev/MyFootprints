package com.syleiman.myfootprints.modelLayer.dbEntities

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.syleiman.myfootprints.modelLayer.dto.FootprintGeoDto

/** Geographic information about footprint (place)  */
@Table(name = "FootprintGeo")
class DbFootprintGeo() : Model()
{
    @Column(name = "Latitude")
    var latitude: Double = 0.toDouble()

    @Column(name = "Longitude")
    var longitude: Double = 0.toDouble()

    @Column(name = "CountryName")
    var countryName: String? = null

    @Column(name = "CountryCode")
    var countryCode: String? = null

    @Column(name = "CityName")
    var cityName: String? = null

    @Column(name = "Footprint", index = true)
    var footprint: DbFootprint? = null

    constructor(footprintGeoDto: FootprintGeoDto) : this()
    {
        latitude = footprintGeoDto.latitude
        longitude = footprintGeoDto.longitude
        countryName = footprintGeoDto.countryName
        countryCode = footprintGeoDto.countryCode
        cityName = footprintGeoDto.cityName
    }
}
