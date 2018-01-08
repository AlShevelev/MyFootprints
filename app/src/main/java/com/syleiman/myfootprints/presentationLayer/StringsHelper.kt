package com.syleiman.myfootprints.presentationLayer

import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.common.LogTags
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/** Common strings functions for UI purpose  */
object StringsHelper
{
    /** Convert date and time to string using device locale  */
    fun DateTimeToLocaleString(dateTime: Date): String
    {
        val timeFormat = android.text.format.DateFormat.getTimeFormat(App.context)
        return DateToLocaleString(dateTime) + " " + timeFormat.format(dateTime)
    }

    /** Convert date to string using device locale  */
    fun DateToLocaleString(dateTime: Date): String
    {
        val dateFormat = android.text.format.DateFormat.getDateFormat(App.context)
        return dateFormat.format(dateTime)
    }

    /** Convert date to string using device locale  */
    fun LocationToLocaleString(location: LatLng): String
    {
        LogTags.GD_AUTH
        val df = DecimalFormat.getNumberInstance() as DecimalFormat
        df.maximumFractionDigits = 5
        return String.format(App.getStringRes(R.string.common_location), df.format(location.latitude), df.format(location.longitude))
    }

    /** Create Url for Google map from location and zoom factor  */
    fun GetGoogleMapUrl(location: LatLng, zoomFactor: Int): String
    {
        val df = DecimalFormat.getNumberInstance() as DecimalFormat
        df.maximumFractionDigits = 8
        df.decimalFormatSymbols = DecimalFormatSymbols(Locale.US)
        return String.format(App.getStringRes(R.string.google_map_url), df.format(location.latitude), df.format(location.longitude), zoomFactor)
    }
}