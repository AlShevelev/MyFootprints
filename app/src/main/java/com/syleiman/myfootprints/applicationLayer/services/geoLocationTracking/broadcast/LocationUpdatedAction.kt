package com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast

import android.content.Intent
import android.location.Location

import com.syleiman.myfootprints.common.broadcast.ActionBase

/** Location was updated (broadcast)  */
class LocationUpdatedAction : ActionBase<IGeoLocationChannel>(Constants.LocationUpdatedActionId)
{

    /**
     * Add data from @dataToSend to @broadcastData
     * @param broadcastData
     * @param dataToSend
     * @return true - success
     */
    override fun initBroadcastData(broadcastData: Intent, vararg dataToSend: Any)
    {
        val location = dataToSend[0] as Location

        broadcastData.putExtra("Provider", location.provider)
        broadcastData.putExtra("Latitude", location.latitude)
        broadcastData.putExtra("Longitude", location.longitude)
        broadcastData.putExtra("Time", location.time)
    }

    /**
     * Process received intent - decode and call some method of  @channelProxy
     * @param broadcastData
     * @param channelProxy
     * @return true - success
     */
    override fun receive(broadcastData: Intent, channelProxy: IGeoLocationChannel): Boolean
    {
        val location = Location(broadcastData.getStringExtra("Provider"))
        location.latitude = broadcastData.getDoubleExtra("Latitude", java.lang.Double.MIN_VALUE)
        location.longitude = broadcastData.getDoubleExtra("Longitude", java.lang.Double.MIN_VALUE)
        location.time = broadcastData.getLongExtra("Time", java.lang.Long.MIN_VALUE)

        channelProxy.locationUpdated(location)

        return true
    }
}
