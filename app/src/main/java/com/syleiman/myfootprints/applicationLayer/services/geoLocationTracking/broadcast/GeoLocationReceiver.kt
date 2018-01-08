package com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast

import com.syleiman.myfootprints.common.broadcast.ActionsCollection
import com.syleiman.myfootprints.common.broadcast.ReceiverBase

/**
 * Broadcast receiver
 */
class GeoLocationReceiver(channelProxy: IGeoLocationChannel) : ReceiverBase<IGeoLocationChannel>(Constants.BroadcastChannelId, channelProxy)
{

    /**
     * Fill collection of actions
     * @param actions
     */
    override fun initActions(actions: ActionsCollection<IGeoLocationChannel>)
    {
        actions.addAction(LocationUpdatedAction())
    }
}