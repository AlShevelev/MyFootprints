package com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.broadcast

import android.location.Location

import com.syleiman.myfootprints.common.broadcast.ActionsCollection
import com.syleiman.myfootprints.common.broadcast.SenderBase

/** Broadcast sender  */
class GeoLocationSender : SenderBase<IGeoLocationChannel>(Constants.BroadcastChannelId), IGeoLocationChannel
{

    /**
     * Fill collection of actions
     * @param actions
     */
    override fun initActions(actions: ActionsCollection<IGeoLocationChannel>)
    {
        actions.addAction(LocationUpdatedAction())
    }

    /**
     * Location was updated
     * @param location current location
     */
    override fun locationUpdated(location: Location)
    {
        actionsCollection.send(Constants.LocationUpdatedActionId, location)
    }
}
