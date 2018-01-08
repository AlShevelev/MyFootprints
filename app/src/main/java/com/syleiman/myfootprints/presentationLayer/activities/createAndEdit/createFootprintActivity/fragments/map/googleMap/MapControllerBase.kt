package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.map.googleMap

import android.graphics.Bitmap
import android.support.v4.app.Fragment
import com.google.android.gms.maps.model.LatLng

/**
 * Base class for maps controllers
 */
abstract class MapControllerBase
/**  */
protected constructor(
        protected val startLocation: LatLng,
        protected val chooseLocationChangedCallback: (LatLng) -> Unit )        // When choose location changed (manually or by geolocation service)
{
    /** Create fragment and render its to parent fragment  */
    abstract fun createControl(parentFragment: Fragment, parentLayoutId: Int)

    /** Location was changed and need to be processed  */
    abstract fun processNewLocation(location: LatLng)

    /** Menu item action_location was chosen from menu  */
    abstract fun processLocationMenuItem(location: LatLng)

    /** Get map snapshot (null if map is not ready or error)  */
    abstract fun getSnapshot(snapshotReady: (Bitmap?) -> Unit)
}
