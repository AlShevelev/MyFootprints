package com.syleiman.myfootprints.businessLayer.localDbService.repositories

import com.activeandroid.Model
import com.activeandroid.query.Select
import com.syleiman.myfootprints.businessLayer.localDbService.TransactionWrapper
import com.syleiman.myfootprints.modelLayer.dto.TaskDto
import com.syleiman.myfootprints.modelLayer.dto.TaskErrorCodes
import com.syleiman.myfootprints.modelLayer.dbEntities.DbTask
import java.sql.SQLException
import java.util.*

/** Repository for tasks  */
class TasksRepository : ITasksRepository
{
    /**
     * Add task to Db
     * @return id of task from Db
     */
    @Throws(SQLException::class)
    override fun add(taskDto: TaskDto): Long
    {
        return TransactionWrapper.processWithoutTransaction<Long> {
            val dbTask = DbTask(taskDto)
            dbTask.save()
            dbTask.id
        }
    }

    /** Delete tack  */
    @Throws(SQLException::class)
    override fun delete(id: Long) =  TransactionWrapper.processWithoutTransaction { Model.delete(DbTask::class.java, id) }

    /** Update task in case of error  */
    @Throws(SQLException::class)
    override fun updateErrors(id: Long, errorCode: TaskErrorCodes, errorText: String)
    {
        update(id, {
            it.errorCode = errorCode.value
            it.errorText = errorText
            it.attempts = it.attempts + 1
            it.lastAttemptDate = Date()
        })
    }

    /**  */
    @Throws(SQLException::class)
    private fun update(id: Long, action: (DbTask) -> Unit)
    {
        TransactionWrapper.processWithoutTransaction {
            Model.load<DbTask>(DbTask::class.java, id)?.let{
                action(it)
                it.save()
            }
        }
    }

    /** Get task by id  */
    @Throws(SQLException::class)
    override fun getById(id: Long): TaskDto? = TransactionWrapper.processWithoutTransaction<TaskDto?> { Model.load<DbTask>(DbTask::class.java, id)?.let(::TaskDto) }

    /**
     * Get all tasks
     */
    @Throws(SQLException::class)
    override fun getAll(): List<TaskDto> = TransactionWrapper.processWithoutTransaction<List<TaskDto>> { Select().from(DbTask::class.java).execute<DbTask>().map(::TaskDto) }
}