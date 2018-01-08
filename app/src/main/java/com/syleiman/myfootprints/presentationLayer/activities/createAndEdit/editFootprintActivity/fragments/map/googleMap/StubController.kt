package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map.googleMap

import android.graphics.Bitmap
import android.support.v4.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.presentationLayer.customComponents.MapStubFragment

/** In case if GPServ is unavailable */
class StubController(
    startLocation: LatLng,
    chooseLocationChangedCallback: (LatLng) -> Unit)
    : MapControllerBase(startLocation, chooseLocationChangedCallback)
{
    private var fragment: MapStubFragment? = null

    /** Create fragment and render its to parent fragment  */
    override fun createControl(parentFragment: Fragment, parentLayoutId: Int)
    {
        fragment = MapStubFragment()
        parentFragment.childFragmentManager.beginTransaction().add(parentLayoutId, fragment).commit()
        fragment!!.setLocation(startLocation)
    }

    /** Menu item action_location was chosen from menu  */
    override fun processLocationMenuItem(location: LatLng)
    {
        // do nothing
    }

    /** Get map snapshot (null if map is not ready or error)  */
    override fun getSnapshot(snapshotReady: (Bitmap?) -> Unit)
    {
        snapshotReady.invoke(null)
    }
}
