package com.syleiman.myfootprints.modelLayer.dto

import java.util.Date

/** One footprint in gallery  */
data class FootprintForGridDto(var footprintId: Long, var createDateTime: Date, var photoFileName: String, var cityName: String?, var countryName: String?)
