package com.syleiman.myfootprints.modelLayer.dbEntities

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.syleiman.myfootprints.modelLayer.dto.FootprintPhotoDto

/** One photo  */
@Table(name = "FootprintPhoto")
class DbFootprintPhoto() : Model()
{
    @Column(name = "PhotoFileName")
    var photoFileName: String? = null

    @Column(name = "MapSnapshotFileName")
    var mapSnapshotFileName: String? = null

    @Column(name = "Footprint", index = true)
    var footprint: DbFootprint? = null

    constructor(footprintPhotoDto: FootprintPhotoDto) : this()
    {
        photoFileName = footprintPhotoDto.photoFileName
        mapSnapshotFileName = footprintPhotoDto.mapSnapshotFileName
    }
}
