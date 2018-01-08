package com.syleiman.myfootprints.presentationLayer.activities.myWorld.googleMap

import android.util.LongSparseArray
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.FootprintsGalleryActivity
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.result.FootprintsGalleryActivityResult
import com.syleiman.myfootprints.presentationLayer.activities.myWorld.MyWorldActivity
import com.syleiman.myfootprints.presentationLayer.customComponents.progress.MapProgressControl
import javax.inject.Inject

/** Map's logic is here  */
class MapController
@Inject
constructor(
    private val parentActivity: MyWorldActivity,
    private val footprints : IFootprintsService,
    private val bitmapService : IBitmapService,
    private val sysInfo : ISystemInformationService) : OnMapReadyCallback, GoogleMap.OnMapLoadedCallback
{
    private var progress: MapProgressControl? = null

    private var googleMap: GoogleMap? = null

    private val markers: LongSparseArray<MarkerBase> = LongSparseArray()            // FootprintId : Marker

    private var clusterManager: ClusterManager<MarkerBase>? = null
    private var markersRenderer: MarkersRenderer? = null

    /** Start loading map  */
    fun initMap(progress: MapProgressControl, parentLayoutId: Int)
    {
        this.progress = progress

        val mapFragment = SupportMapFragment.newInstance(GoogleMapOptions())
        parentActivity.supportFragmentManager.beginTransaction().add(parentLayoutId, mapFragment).commit()
        mapFragment.getMapAsync(this)
        progress.setText(App.getStringRes(R.string.my_world_map_loading))
    }

    /**  */
    override fun onMapReady(googleMap: GoogleMap)
    {
        this.googleMap = googleMap

        googleMap.uiSettings.isRotateGesturesEnabled = false          // turn off rotation and compass
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isZoomControlsEnabled = true

        clusterManager = ClusterManager<MarkerBase>(App.context, googleMap)
        markersRenderer = MarkersRenderer(parentActivity, bitmapService, sysInfo, googleMap, clusterManager!!)
        clusterManager!!.renderer = markersRenderer

        clusterManager!!.setOnClusterItemClickListener { markerBase ->  // Click on single marker
            FootprintsGalleryActivity.start(parentActivity, (markerBase as DefaultMarker).footprintId, false)            // Show footprint info
            true
        }

        clusterManager!!.setOnClusterClickListener {        // Click on cluster (to prevent apperiance of standard GM buttons)
            true
        }

        googleMap.setOnCameraMoveListener {         // Update clusters on zoom and move
            clusterManager!!.cluster()
        }
        googleMap.setOnMarkerClickListener(clusterManager)
        googleMap.setOnMapLoadedCallback(this)

        showMarkers { }
    }

    /**  */
    override fun onMapLoaded()
    {
        progress!!.visibility = View.GONE
    }

    /** Load and show markers  */
    @Suppress("UNUSED_VARIABLE")
    private fun showMarkers(complete: Function0<Unit>)
    {
        footprints.getAllMarkers { items ->
            if (items == null)
            {
                parentActivity.showMessage(R.string.message_box_cant_load_footprints)
                complete.invoke()
            }
            else
            {
                for ((footprintId, markerColor, latitude, longitude, description, footprintPhotoFileName) in items)
                    this.addMarker(
                        footprintId,
                        LatLng(latitude, longitude),
                        description,
                        footprintPhotoFileName)
                clusterManager!!.cluster()

                complete.invoke()
            }
        }

    }

    /** Add new marker  */
    private fun addMarker(footprintId: Long, location: LatLng, description: String, footprintPhotoFileName: String)
    {
        val marker = DefaultMarker(footprintId, description, footprintPhotoFileName, location)
        clusterManager!!.addItem(marker)
        markers.put(footprintId, marker)
    }

    /** Remove options  */
    private fun removeMarker(footprintId: Long)
    {
        val marker = markers.get(footprintId)

        if (marker != null)
        {
            markers.remove(footprintId)
            clusterManager!!.removeItem(marker)
        }
    }

    /** Move options  */
    private fun updateMarker(footprintId: Long, location: LatLng, description: String, footprintPhotoFileName: String)
    {
        removeMarker(footprintId)
        addMarker(footprintId, location, description, footprintPhotoFileName)
    }

    /** Process result from Gallery activity  */
    fun processGalleryResult(galleryResult: FootprintsGalleryActivityResult)
    {
        if (!galleryResult.deletedFootprintsIds.isEmpty())
            for (deletedFootprintId in galleryResult.deletedFootprintsIds)          // Remove footprints from map
                removeMarker(deletedFootprintId)

        if (!galleryResult.updatedFootprints.isEmpty())
            for (updateFootprintInfo in galleryResult.updatedFootprints)            // Update markers
                updateMarker(updateFootprintInfo.footprintId, updateFootprintInfo.location, updateFootprintInfo.description, updateFootprintInfo.photoFileName)

        if (!galleryResult.deletedFootprintsIds.isEmpty() || !galleryResult.updatedFootprints.isEmpty())
            clusterManager!!.cluster()
    }

    /** Reload all markers  */
    fun reloadMarkers()
    {
        if (googleMap == null)
            return

        progress!!.visibility = View.VISIBLE

        googleMap!!.clear()                  // Remove all markers
        markers.clear()
        clusterManager!!.clearItems()

        showMarkers {
            progress!!.visibility = View.GONE       // and loadAndInscribeQuick again
        }
    }
}
