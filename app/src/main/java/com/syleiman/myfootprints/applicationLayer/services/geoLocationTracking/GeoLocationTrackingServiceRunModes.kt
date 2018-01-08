package com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking

/**
 * GeoLocationTrackingService run modes
 */
enum class GeoLocationTrackingServiceRunModes(var value : Int)
{
    None(0),
    Active(1),
    Passive(2);

    companion object Create
    {
        fun from(findValue: Int): GeoLocationTrackingServiceRunModes = GeoLocationTrackingServiceRunModes.values().first { it.value == findValue }
    }
}
