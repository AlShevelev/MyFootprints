package com.syleiman.myfootprints.modelLayer.dbEntities

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

/** Geographic location */
@Table(name = "Location")
class DbLocation : Model()
{
    @Column(name = "Latitude")
    var latitude: Double = 0.toDouble()

    @Column(name = "Longitude")
    var longitude: Double = 0.toDouble()

    @Column(name = "Time")
    var time: Long = 0

    /** Last location - for service purpose  */
    @Column(name = "IsLast")
    var isLast: Boolean = false
}
