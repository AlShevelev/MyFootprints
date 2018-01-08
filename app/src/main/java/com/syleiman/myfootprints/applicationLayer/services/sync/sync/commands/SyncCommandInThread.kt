package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands

import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncResult

/** Command with sync in separate thread  */
abstract class SyncCommandInThread : SyncCommandBase(), Runnable
{
    private val locker = Any()

    private lateinit var syncResult: SyncResult

    /** Execute task  */
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    override fun execute(): SyncResult
    {
        syncResult = SyncResult()

        Thread(this).start()
        synchronized(locker) {
            try
            {
                (locker as Object).wait()
            } catch (ex: InterruptedException)
            {
                ex.printStackTrace()
            }

        }

        return syncResult
    }

    /**  */
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    override fun run()
    {
        try
        {
            doJob(locker, syncResult)
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            synchronized(locker) {
                (locker as Object).notify()
            }
        }

    }

    /**
     * Do useful job here
     * @param locker Don't forget call @locker.notify() !
     */
    protected abstract fun doJob(locker: Any, syncResult: SyncResult)
}