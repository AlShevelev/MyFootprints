package com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast

/** Interface methods for receiver  */
interface ISyncResultChannel
{
    /** Sync was completed  */
    fun syncCompleted(result: SyncResult)
}
