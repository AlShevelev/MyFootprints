package com.syleiman.myfootprints.common.broadcast

import android.content.Intent

import com.syleiman.myfootprints.applicationLayer.App

/** Base class for processing one broadcast event  */
abstract class ActionBase<in T>(val actionId: Int)         // Unique id of processor
{
    private var channelId: String? = null
    private var channelProxy: T? = null

    companion object
    {
        fun extractActionId(broadcastData: Intent): Int = broadcastData.getIntExtra("Action", -1)
    }

    /** Memorize broadcast channel id  */
    fun setChannelId(channelId: String)
    {
        this.channelId = channelId
    }

    /** Memorize proxy to channel (interface - set of methods call to)  */
    fun setChannelProxy(channelProxy: T?)
    {
        this.channelProxy = channelProxy
    }

    /**
     * Send event
     * @return true - success
     */
    fun initBroadcastData(vararg dataToSend: Any): Boolean
    {
        try
        {
            val broadcastData = Intent(channelId)
            broadcastData.putExtra("Action", actionId)

            initBroadcastData(broadcastData, *dataToSend)

            App.context.sendBroadcast(broadcastData)

            return true
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            return false
        }
    }

    /**
     * Add data from @dataToSend to @broadcastData
     * @return true - success
     */
    protected abstract fun initBroadcastData(broadcastData: Intent, vararg dataToSend: Any)

    /**
     * Process received intent
     * @return true - success
     */
    fun receive(broadcastData: Intent): Boolean
    {
        return try { receive(broadcastData, channelProxy!!) }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            false
        }
    }

    /**
     * Process received intent - decode and call some method of  @channelProxy
     * @return true - success
     */
    protected abstract fun receive(broadcastData: Intent, channelProxy: T): Boolean
}
