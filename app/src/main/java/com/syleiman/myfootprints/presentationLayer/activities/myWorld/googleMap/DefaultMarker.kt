package com.syleiman.myfootprints.presentationLayer.activities.myWorld.googleMap

import com.google.android.gms.maps.model.LatLng

/** Marker on map  */
internal class DefaultMarker(
        val footprintId: Long,
        val description: String,
        val footprintPhotoFileName: String,
        position: LatLng) : MarkerBase(position)
