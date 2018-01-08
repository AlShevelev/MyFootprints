package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map.googleMap

import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.view.View
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map.IEditStepMapFragmentPresenterMap
import com.syleiman.myfootprints.presentationLayer.customComponents.progress.MapProgressControl

/** In case if GPServ is available and we can use map  */
class NormalController(
    private val presenter: IEditStepMapFragmentPresenterMap,
    private val progress: MapProgressControl,
    startLocation: LatLng,
    chooseLocationChangedCallback: (LatLng) -> Unit)
        : MapControllerBase(
            startLocation,
            chooseLocationChangedCallback),
        OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMapLongClickListener
{
    companion object
    {
        private val startZoomFactor = 18f            // Zoom factor on show map
    }

    private var googleMap: GoogleMap? = null

    private var marker: Marker? = null

    private var isLoaded = false

    /** Create fragment and render its to parent fragment  */
    override fun createControl(parentFragment: Fragment, parentLayoutId: Int)
    {
        val mapFragment = SupportMapFragment.newInstance(GoogleMapOptions())
        parentFragment.childFragmentManager.beginTransaction().add(parentLayoutId, mapFragment).commit()
        mapFragment.getMapAsync(this)
    }

    /** When map control is ready (but map is not loaded)  */
    override fun onMapReady(googleMap: GoogleMap)
    {
        this.googleMap = googleMap

        googleMap.setOnMapLoadedCallback(this)
        googleMap.setOnMapLongClickListener(this)

        googleMap.uiSettings.isRotateGesturesEnabled = false          // turn off rotation and compass
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isZoomControlsEnabled = true

        progress.setCurrentLocation(startLocation)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, startZoomFactor))      // Move to our last location

        marker = googleMap.addMarker(MarkerOptions().// and add options
                position(startLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).anchor(0.5f, 1.0f))
    }

    /** When map is loaded  */
    override fun onMapLoaded()
    {
        progress.visibility = View.GONE
        isLoaded = true
    }

    /** Long click on map - set options  */
    override fun onMapLongClick(location: LatLng)
    {
        marker!!.position = location
        chooseLocationChangedCallback.invoke(location)
    }

    /** Menu item action_location was chosen from menu  */
    override fun processLocationMenuItem(location: LatLng)
    {
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, startZoomFactor))    // Move to our last geolocation
    }

    /** Get map snapshot (null if map is not ready or error)  */
    override fun getSnapshot(snapshotReady: (Bitmap?) -> Unit)
    {
        if (!isLoaded)
            snapshotReady.invoke(null)            // Map is not loaded

        else if (!presenter.isNeedSnapshot())
            snapshotReady.invoke(null)            // Snapshot is not needed
        else
            googleMap!!.snapshot { t -> snapshotReady.invoke(t) }
    }
}