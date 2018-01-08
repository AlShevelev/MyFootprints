package com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.common.LogTags

import javax.inject.Inject

/** Service for getting current geo-position */
class GeoLocationTrackingService : Service()
{
    @Inject internal lateinit var modesFacade: TrackingModesFacade

    companion object
    {
        /** Start service in active mode  */
        fun startInActiveMode()
        {
            val intent = Intent(App.context, GeoLocationTrackingService::class.java)
            intent.putExtra("Mode", GeoLocationTrackingServiceRunModes.Active.value)
            App.context.startService(intent)
        }

        /** Start service in passive mode  */
        fun startInPassiveMode()
        {
            val intent = Intent(App.context, GeoLocationTrackingService::class.java)
            intent.putExtra("Mode", GeoLocationTrackingServiceRunModes.Passive.value)
            App.context.startService(intent)
        }

        /** Stop service  */
        fun stop()
        {
            val intent = Intent(App.context, GeoLocationTrackingService::class.java)
            App.context.stopService(intent)
        }
    }

    override fun onCreate()
    {
        App.application.getServicesComponent().inject(this)

        Log.d(LogTags.GEOLOCATION, "Service started")

        super.onCreate()
    }

    /**  */
    override fun onBind(intent: Intent): IBinder? = null

    /**  */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        val mode = GeoLocationTrackingServiceRunModes.from(intent.getIntExtra("Mode", -1))

        Log.d(LogTags.GEOLOCATION, "Try to switch to: " + mode)

        modesFacade.switchMode(mode)

        return Service.START_NOT_STICKY
    }

    /**  */
    override fun onDestroy()
    {
        Log.d(LogTags.GEOLOCATION, "Service destroyed")

        modesFacade.stop()
        App.application.releaseServicesComponent()

        super.onDestroy()
    }
}