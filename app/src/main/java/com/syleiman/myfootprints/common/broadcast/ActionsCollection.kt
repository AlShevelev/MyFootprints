package com.syleiman.myfootprints.common.broadcast

import android.content.Intent
import java.util.TreeMap

/**
 * List of actions
 * @param <T> Proxy to channel (interface - set of methods call to)
</T> */
class ActionsCollection<T> private constructor(private val channelId: String, private val channelProxy: T?) : IActionsCollectionEdit<T>
{
    private val actions: MutableMap<Int, ActionBase<T>> = TreeMap()

    companion object
    {
        /**
         * Create collection for sender
         * @param channelId
         * @return
         */
        fun <T> createForSender(channelId: String): ActionsCollection<T> = ActionsCollection(channelId, null)

        /**
         * Create collection for receiver
         * @param channelId
         * @param <T>
         * @return
        </T> */
        fun <T> createForReceiver(channelId: String, channelProxy: T): ActionsCollection<T> = ActionsCollection(channelId, channelProxy)
    }

    /**
     * Add action to collection
     */
    override fun addAction(actionToAdd: ActionBase<T>): IActionsCollectionEdit<*>
    {
        actionToAdd.setChannelId(channelId)
        actionToAdd.setChannelProxy(channelProxy)

        actions.put(actionToAdd.actionId, actionToAdd)

        return this
    }

    /**
     * Send message
     * @param actionId
     * @param dataToSend
     * @return
     */
    fun send(actionId: Int, vararg dataToSend: Any): Boolean
    {
        val action = actions[actionId]
        return action!!.initBroadcastData(*dataToSend)
    }

    /**
     * Process received intent - decode and call some method of  @channelProxy
     * @param broadcastData
     * @return
     */
    fun receive(broadcastData: Intent): Boolean
    {
        val actionId = ActionBase.extractActionId(broadcastData)

        val action = actions[actionId]
        return action!!.receive(broadcastData)
    }
}