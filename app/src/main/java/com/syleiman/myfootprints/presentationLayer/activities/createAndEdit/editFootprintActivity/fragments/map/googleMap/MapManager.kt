package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map.googleMap

import android.graphics.Bitmap
import android.support.v4.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map.EditStepMapFragmentPresenter
import com.syleiman.myfootprints.presentationLayer.customComponents.progress.MapProgressControl

/** Common maps logic is here (choosing controller, processing location updates  */
class MapManager
/**  */
(private val presenter: EditStepMapFragmentPresenter, private val progress: MapProgressControl)
{

    private var currentMapController: MapControllerBase? = null

    /** Create map and show it  */
    fun initMap(parentFragment: Fragment, parentLayoutId: Int)
    {
        val currentChooseLocation = presenter.lastLocation

        currentMapController = if (presenter.isGooglePlayServicesAvailable)
            NormalController(presenter, progress, currentChooseLocation) {this.onChooseLocationChanged(it)}
        else
            StubController(currentChooseLocation) {this.onChooseLocationChanged(it)}

        currentMapController!!.createControl(parentFragment, parentLayoutId)
    }

    /** Menu item action_location was chosen from menu  */
    fun processLocationMenuItem()
    {
        currentMapController!!.processLocationMenuItem(presenter.lastLocation)
    }

    /** When choose location changed (manually or by geolocation service)  */
    private fun onChooseLocationChanged(location: LatLng)
    {
        presenter.storeLocation(location)
    }

    /** Get map snapshot (null if map is not ready or error)  */
    fun getSnapshot(snapshotReady: (Bitmap?) -> Unit)
    {
        currentMapController!!.getSnapshot(snapshotReady)
    }
}