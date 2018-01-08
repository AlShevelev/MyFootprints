package com.syleiman.myfootprints.presentationLayer.customComponents.progress

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.StringsHelper

/**
 * Show progressBar and location while map loaded
 */
class MapProgressControl : LinearLayout
{
    private var locationLabel: TextView? = null

    constructor(context: Context) : super(context)
    {
        initComponent()
    }

    /**  */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        initComponent()
    }

    /**   */
    private fun initComponent()
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cc_map_progress, this)

        locationLabel = findViewById(R.id.locationLabel) as TextView
    }

    /**  */
    fun setCurrentLocation(location: LatLng)
    {
        val locationText = String.format(App.getStringRes(R.string.message_map_no_gps_location), StringsHelper.LocationToLocaleString(location))
        locationLabel!!.text = locationText
    }

    /**  */
    fun setText(text: String)
    {
        locationLabel!!.text = text
    }
}
