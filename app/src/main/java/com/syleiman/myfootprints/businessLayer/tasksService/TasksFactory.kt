package com.syleiman.myfootprints.businessLayer.tasksService

import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.modelLayer.dto.TaskDto
import com.syleiman.myfootprints.modelLayer.dto.TaskTypes
import java.sql.SQLException
import java.util.*

/** Load tasks from Db  */
class TasksFactory(
    private val localDb: ILocalDbService,
    private val sysInfo: ISystemInformationService)
{

    private lateinit var allTasks: List<TaskDto>

    /**
     * Init tasks list
     * @return total number of tasks
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun init(): Int
    {
        allTasks = load()
        return allTasks.size
    }

    /**
     * Load tasks from Db
     * @throws SQLException
     */
    @Throws(SQLException::class)
    private fun load(): List<TaskDto> = localDb.tasks().getAll().sortedWith(Comparator { o1, o2 -> -1 * o1.creationDate!!.compareTo(o2.creationDate)})

    /** Get task by index  */
    fun getByIndex(index: Int): ITaskRunning
    {
        val currentDto = allTasks[index]

        when (currentDto.type)
        {
            TaskTypes.Geocoding -> return GeoCodingTask(currentDto, localDb, sysInfo)
            else -> throw UnsupportedOperationException("This code is not supported: " + currentDto.type)
        }
    }
}