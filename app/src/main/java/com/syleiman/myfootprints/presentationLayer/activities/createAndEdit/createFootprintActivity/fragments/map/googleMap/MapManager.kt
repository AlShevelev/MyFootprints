package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.map.googleMap

import android.graphics.Bitmap
import android.location.Location
import android.support.v4.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast.GeoLocationReceiver
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast.IGeoLocationChannel
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.map.CreateStepMapFragmentPresenter
import com.syleiman.myfootprints.presentationLayer.customComponents.progress.MapProgressControl

/** Common maps logic is here (choosing controller, tracking location)  */
class MapManager(
        private val presenter: CreateStepMapFragmentPresenter,
        private val progress: MapProgressControl) : IGeoLocationChannel
{
    private var currentMapController: MapControllerBase? = null
    private var geoLocationReceiver: GeoLocationReceiver? = null

    /** Create map and show it  */
    fun initMap(parentFragment: Fragment, parentLayoutId: Int)
    {
        val currentChooseLocation = convertLocation(presenter.lastLocation)

        currentMapController = if (presenter.isGooglePlayServicesAvailable)
            NormalController(progress, currentChooseLocation, {this.onChooseLocationChanged(it)})
        else
            StubController(currentChooseLocation, {this.onChooseLocationChanged(it)})

        currentMapController!!.createControl(parentFragment, parentLayoutId)

        geoLocationReceiver = presenter.getLocationsReceiver(this)
    }

    /** Dispose used resources  */
    fun clear() = geoLocationReceiver!!.unregister()

    /** New location received from geolocation service  */
    override fun locationUpdated(location: Location) = currentMapController!!.processNewLocation(convertLocation(location))

    /** Menu item action_location was chosen from menu  */
    fun processLocationMenuItem() = currentMapController!!.processLocationMenuItem(convertLocation(presenter.lastLocation))

    /** Convert location to map's format  */
    private fun convertLocation(lastLocation: Location): LatLng = LatLng(lastLocation.latitude, lastLocation.longitude)

    /** When choose location changed (manually or by geolocation service)  */
    private fun onChooseLocationChanged(location: LatLng) = presenter.storeLocation(location)

    /** Get map snapshot (null if map is not ready or error)  */
    fun getSnapshot(snapshotReady: (Bitmap?)->Unit) = currentMapController!!.getSnapshot(snapshotReady)
}