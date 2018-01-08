package com.syleiman.myfootprints.businessLayer.sysInfoService

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.location.LocationManager
import android.net.ConnectivityManager

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService

import java.util.UUID
import javax.inject.Inject
import android.util.DisplayMetrics



/** Common information about system  */
class SystemInformationService
@Inject
constructor(private val optionsCache: IOptionsCacheService) : ISystemInformationService
{
    companion object Internet
    {
        /** Is internet enabled or in process of being enabled on device  */
        fun getInternetConnectionStatus(): InternetConnectionStatuses
        {
            val cm = App.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo

            if (activeNetwork == null || !activeNetwork.isConnected)        // No network
                return InternetConnectionStatuses.None

            when (activeNetwork.type)
            {
                ConnectivityManager.TYPE_MOBILE -> return InternetConnectionStatuses.Mobile
                ConnectivityManager.TYPE_WIFI -> return InternetConnectionStatuses.WiFi
                else -> return InternetConnectionStatuses.None
            }
        }
    }

    /** Is geolocation enabled on device  */
    override fun isGeoLocationEnabled(): Boolean
    {
        val locationManager = App.context.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /** Is Google Play Services available on device  */
    override fun isGooglePlayServicesAvailable(): Boolean
    {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(App.context) == ConnectionResult.SUCCESS
    }

    /** Get size of device screen  */
    override fun getScreenSize(context: Context): Point
    {
        val display = (context as Activity).windowManager.defaultDisplay
        val size = Point()               // Get size of screen
        display.getSize(size)

        return size
    }

    /** Get unique id of installiation  */
    override fun getInstallId(): String
    {
        var installId = optionsCache.getInstallId()
        if (installId == null)
        {
            val id = UUID.randomUUID()
            installId = (java.lang.Long.toString(id.mostSignificantBits, 36) + java.lang.Long.toString(id.leastSignificantBits, 36)).replace("-", "")
            optionsCache.setInstallId(installId)
        }
        return installId
    }

    /** */
    override fun dpToPx(dp: Int): Int
    {
        val displayMetrics = App.context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    /** */
    override fun pxToDp(px: Int): Int
    {
        val displayMetrics = App.context.resources.displayMetrics
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}