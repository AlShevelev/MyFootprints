package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands

import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncResult

/** Base class for sync tasks  */
abstract class SyncCommandBase
{
    /** Execute task  */
    abstract fun execute(): SyncResult
}
