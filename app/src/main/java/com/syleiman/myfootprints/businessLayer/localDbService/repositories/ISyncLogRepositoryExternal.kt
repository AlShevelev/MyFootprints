package com.syleiman.myfootprints.businessLayer.localDbService.repositories

import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto
import java.sql.SQLException

/** Repository for sync log - external interface  */
interface ISyncLogRepositoryExternal
{
    /** Get Id of log records to process  */
    @Throws(SQLException::class)
    fun getDataToProcess(): List<Long>

    /**
     * Get log record by Id
     * @return null if record not found
     */
    @Throws(SQLException::class)
    fun getRecord(id: Long): SyncLogDto?

    /** Remove all locks on log records  */
    @Throws(SQLException::class)
    fun resetAllLocks()

    /** Remove log record  */
    @Throws(SQLException::class)
    fun removeLogRecord(id: Long)
}
