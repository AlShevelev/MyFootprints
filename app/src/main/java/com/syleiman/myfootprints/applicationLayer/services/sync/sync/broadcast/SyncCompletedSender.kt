package com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast

import com.syleiman.myfootprints.common.broadcast.ActionsCollection
import com.syleiman.myfootprints.common.broadcast.SenderBase

/** Broadcast sender  */
class SyncCompletedSender : SenderBase<ISyncResultChannel>(Constants.BroadcastChannelId), ISyncResultChannel
{

    /** Sync was completed  */
    override fun syncCompleted(result: SyncResult)
    {
        actionsCollection.send(Constants.SyncCompletedActionId, result)
    }

    /** Fill collection of actions  */
    override fun initActions(actions: ActionsCollection<ISyncResultChannel>)
    {
        actions.addAction(SyncCompletedAction())
    }
}
