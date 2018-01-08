package com.syleiman.myfootprints.businessLayer.tasksService

import com.google.android.gms.maps.model.LatLng

import java.sql.SQLException

/** Facade for tasks working  */
interface ITasksService
{
    /**
     * Create new task
     * @return id of task
     */
    @Throws(SQLException::class)
    fun createGeoCodingTask(location: LatLng, footprintId: Long): Long

    /** Get factory of tasks  */
    fun getTasksFactory(): TasksFactory
}
