package com.syleiman.myfootprints.common.broadcast

/**
 * Base class for broadcast event sender
 */
abstract class SenderBase<T>(channelId: String)
{
    protected val actionsCollection: ActionsCollection<T> = ActionsCollection.createForSender<T>(channelId)

    init
    {
        initActions(actionsCollection)
    }

    /**
     * Fill collection of actions
     * @param actions
     */
    protected abstract fun initActions(actions: ActionsCollection<T>)
}
