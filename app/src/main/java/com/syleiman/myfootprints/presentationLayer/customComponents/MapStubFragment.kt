package com.syleiman.myfootprints.presentationLayer.customComponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.FragmentBase
import com.syleiman.myfootprints.presentationLayer.StringsHelper
import kotlinx.android.synthetic.main.fr_create_footprint_map_stub.*


/** Map fragment if GP Services unavailable */
class MapStubFragment : FragmentBase()
{
    private var locationText: String? = null

    /**  */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater!!.inflate(R.layout.fr_create_footprint_map_stub, container, false)
    }

    /**  */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        if (locationText != null)
        {
            locationTextLabel.visibility = View.VISIBLE
            locationTextLabel.text = locationText
        }
    }

    /**  */
    fun setLocation(location: LatLng)
    {
        locationText = String.format(App.getStringRes(R.string.message_map_no_gps_location), StringsHelper.LocationToLocaleString(location))
    }
}
