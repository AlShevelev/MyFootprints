package com.syleiman.myfootprints.presentationLayer.activities.myWorld

import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.ISyncResultChannel
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncResult
import javax.inject.Inject

class MyWorldActivityPresenter
@Inject
constructor(private val activityCallbacks: IMyWorldActivityCallbacks, private val model: MyWorldActivityModel)
{
    /** When activity started  */
    fun init()
    {
        val initMessage = model.initMessage
        if (initMessage != null)
            activityCallbacks.showMessage(initMessage)

        model.startReceiveSyncStatus(object : ISyncResultChannel
        {
            override fun syncCompleted(result: SyncResult)            // Start receive events from sync
            {
                activityCallbacks.syncCompleted(result.footprintsWasChanged)
            }
        })
    }

    /** Called from activity when it destroyed finally  */
    fun onDestroyUi()
    {
        model.stopReceiveSyncStatus()          // Stop receive events from sync
    }

    /** When user click on Help button  */
    fun onHelpButtonClick()
    {
        activityCallbacks.showMessage(model.helpMessage)
    }
}
