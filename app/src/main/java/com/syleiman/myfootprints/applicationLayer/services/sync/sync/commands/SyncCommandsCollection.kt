package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands

import com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast.SyncResult

import java.util.ArrayList

/** Collection for sync tasks  */
class SyncCommandsCollection
{
    private val tasks: MutableList<SyncCommandBase>

    /**  */
    init
    {
        this.tasks = ArrayList<SyncCommandBase>()
    }

    /**  */
    fun add(task: SyncCommandBase)
    {
        tasks.add(task)
    }

    /**
     * Start process sync actions
     * @return result of sync
     */
    fun execute(): List<SyncResult>
    {
        val result = tasks.indices.map { tasks[it].execute() }

        return result
    }
}
