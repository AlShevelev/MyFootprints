package com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast

import android.content.Intent

import com.syleiman.myfootprints.common.broadcast.ActionBase

/** Sync was completed (broadcast)  */
class SyncCompletedAction : ActionBase<ISyncResultChannel>(Constants.SyncCompletedActionId)
{
    /** Add data from @dataToSend to @broadcastData  */
    override fun initBroadcastData(broadcastData: Intent, vararg dataToSend: Any)
    {
        val syncResult = dataToSend[0] as SyncResult
        broadcastData.putExtra("FootprintsWasChanged", syncResult.footprintsWasChanged)
    }

    /** Process received intent - decode and call some method of  @channelProxy  */
    override fun receive(broadcastData: Intent, channelProxy: ISyncResultChannel): Boolean
    {
        val syncResult = SyncResult()
        syncResult.footprintsWasChanged = broadcastData.getBooleanExtra("FootprintsWasChanged", false)

        channelProxy.syncCompleted(syncResult)

        return true
    }
}
