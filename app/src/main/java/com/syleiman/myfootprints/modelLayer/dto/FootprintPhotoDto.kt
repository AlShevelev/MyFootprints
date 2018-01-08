package com.syleiman.myfootprints.modelLayer.dto

import com.syleiman.myfootprints.modelLayer.dbEntities.DbFootprintPhoto

/** Photo in footprint  */
data class FootprintPhotoDto(var id: Long, var photoFileName: String?, var mapSnapshotFileName: String?) {
    constructor(dbFootprintPhoto: DbFootprintPhoto) : this(dbFootprintPhoto.id!!, dbFootprintPhoto.photoFileName, dbFootprintPhoto.mapSnapshotFileName)
}
