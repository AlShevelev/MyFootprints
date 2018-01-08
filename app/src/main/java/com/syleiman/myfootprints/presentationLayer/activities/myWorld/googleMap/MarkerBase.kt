package com.syleiman.myfootprints.presentationLayer.activities.myWorld.googleMap

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/** Base class for all kinds of markers in cluster  */
internal abstract class MarkerBase(protected val innerPosition: LatLng) : ClusterItem
{
    override fun getPosition(): LatLng
    {
        return innerPosition
    }
}