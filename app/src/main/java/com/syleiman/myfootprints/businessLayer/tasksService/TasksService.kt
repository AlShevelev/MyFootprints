package com.syleiman.myfootprints.businessLayer.tasksService

import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService

import java.sql.SQLException
import javax.inject.Inject

/** Facade for tasks working  */
class TasksService
@Inject
constructor(
    private val localDb: ILocalDbService,
    private val sysInfo: ISystemInformationService) : ITasksService
{

    /**
     * Create new task
     * @return id of task
     */
    @Throws(SQLException::class)
    override fun createGeoCodingTask(location: LatLng, footprintId: Long): Long = GeoCodingTask(location, footprintId, localDb).save()

    /** Get factory of tasks  */
    override fun getTasksFactory(): TasksFactory = TasksFactory(localDb, sysInfo)
}
