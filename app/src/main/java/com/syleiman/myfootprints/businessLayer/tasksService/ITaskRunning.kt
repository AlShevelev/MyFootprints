package com.syleiman.myfootprints.businessLayer.tasksService

/** Base class for tasks  */
interface ITaskRunning
{
    /**
     * Execute task
     * @return true - task complete successfully (from point of vies of service); false - fatal error ant service must interrupt tasks processing
     */
    fun execute(): Boolean
}
