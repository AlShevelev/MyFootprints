package com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast

import com.syleiman.myfootprints.common.broadcast.ActionsCollection
import com.syleiman.myfootprints.common.broadcast.ReceiverBase

/** Broadcast receiver  */
class SyncCompletedReceiver(channelProxy: ISyncResultChannel) : ReceiverBase<ISyncResultChannel>(Constants.BroadcastChannelId, channelProxy)
{
    /** Fill collection of actions  */
    override fun initActions(actions: ActionsCollection<ISyncResultChannel>)
    {
        actions.addAction(SyncCompletedAction())
    }
}
