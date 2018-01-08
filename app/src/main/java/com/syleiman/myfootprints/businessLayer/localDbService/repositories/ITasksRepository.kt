package com.syleiman.myfootprints.businessLayer.localDbService.repositories

import com.syleiman.myfootprints.modelLayer.dto.TaskDto
import com.syleiman.myfootprints.modelLayer.dto.TaskErrorCodes

import java.sql.SQLException

/** Repository for tasks  */
interface ITasksRepository
{
    /**
     * Add task to Db
     * @return id of task from Db
     */
    @Throws(SQLException::class)
    fun add(taskDto: TaskDto): Long

    /** Delete tack   */
    @Throws(SQLException::class)
    fun delete(id: Long)

    /** Update task in case of error  */
    @Throws(SQLException::class)
    fun updateErrors(id: Long, errorCode: TaskErrorCodes, errorText: String)

    /** Get task by id  */
    @Throws(SQLException::class)
    fun getById(id: Long): TaskDto?

    /** Get all tasks  */
    fun getAll(): List<TaskDto>
}
