package com.syleiman.myfootprints.modelLayer.dbEntities

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.syleiman.myfootprints.modelLayer.dto.FootprintDto

import java.util.Date
import java.util.UUID

/** One footprint  */
@Table(name = "Footprint")
class DbFootprint() : Model()
{
    /** GUID for replication purpose   */
    @Column(name = "Uid", index = true)
    var uid: String = UUID.randomUUID().toString()

    @Column(name = "Comment")
    var comment: String? = null

    @Column(name = "CreationDate", index = true)
    var creationDate: Date? = null

    @Column(name = "MarkerColor")
    var markerColor: Int = 0

    init
    {
        uid = UUID.randomUUID().toString()
    }

    constructor(footprintDto: FootprintDto) : this()
    {
        comment = footprintDto.comment
        creationDate = footprintDto.creationDate
        markerColor = footprintDto.markerColor
    }

    /** Get all footprints geo information for this footprint  */
    val footprintsGeo: List<DbFootprintGeo>
        get() = getMany(DbFootprintGeo::class.java, "Footprint")

    /** Get all photos for this footprint  */
    val photos: List<DbFootprintPhoto>
        get() = getMany(DbFootprintPhoto::class.java, "Footprint")
}
