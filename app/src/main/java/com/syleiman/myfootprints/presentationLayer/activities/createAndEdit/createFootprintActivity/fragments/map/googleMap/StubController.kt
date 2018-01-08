package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.map.googleMap

import android.graphics.Bitmap
import android.support.v4.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.presentationLayer.customComponents.MapStubFragment

/** In case if GPServ is unavailable */
class StubController
/**  */
(startLocation: LatLng, chooseLocationChangedCallback: Function1<LatLng, Unit>) : MapControllerBase(startLocation, chooseLocationChangedCallback)
{
    private var fragment: MapStubFragment? = null

    /** Create fragment and render its to parent fragment  */
    override fun createControl(parentFragment: Fragment, parentLayoutId: Int)
    {
        fragment = MapStubFragment()
        parentFragment.childFragmentManager.beginTransaction().add(parentLayoutId, fragment).commit()
        fragment!!.setLocation(startLocation)
    }

    /** Location was changed and need to be processed  */
    override fun processNewLocation(location: LatLng)
    {
        fragment!!.setLocation(location)
        chooseLocationChangedCallback.invoke(location)
    }

    /** Menu item action_location was chosen from menu  */
    override fun processLocationMenuItem(location: LatLng)
    {
        // do nothing
    }

    /** Get map snapshot (null if map is not ready or error)  */
    override fun getSnapshot(snapshotReady: (Bitmap?) -> Unit) = snapshotReady.invoke(null)
}
