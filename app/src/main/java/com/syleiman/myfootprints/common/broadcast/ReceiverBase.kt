package com.syleiman.myfootprints.common.broadcast

import android.content.Context
import android.content.Intent
import android.content.IntentFilter

import com.syleiman.myfootprints.applicationLayer.App

/** Base class for broadcast event receiver  */
abstract class ReceiverBase<T>(channelId: String, channelProxy: T)
{
    private val actionsCollection: ActionsCollection<T> = ActionsCollection.createForReceiver(channelId, channelProxy)

    private val broadcastReceiver: android.content.BroadcastReceiver

    init
    {
        initActions(actionsCollection)

        broadcastReceiver = object : android.content.BroadcastReceiver()
        {
            override fun onReceive(context: Context, intent: Intent)
            {
                try { actionsCollection.receive(intent) } catch (ex: Exception) { ex.printStackTrace() }
            }
        }

        val intentFilter = IntentFilter(channelId)
        App.context.registerReceiver(broadcastReceiver, intentFilter)
    }

    /**
     * Fill collection of actions
     * @param actions
     */
    protected abstract fun initActions(actions: ActionsCollection<T>)

    /** Unsubscribe from broadcast */
    fun unregister()
    {
        App.context.unregisterReceiver(broadcastReceiver)
    }
}
