package com.syleiman.myfootprints.businessLayer.localDbService.repositories

import android.util.Log
import com.activeandroid.Model

import com.activeandroid.query.Select
import com.activeandroid.util.SQLiteUtils
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.businessLayer.localDbService.TransactionWrapper
import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto
import com.syleiman.myfootprints.modelLayer.dbEntities.DbSyncLogData
import com.syleiman.myfootprints.modelLayer.dbEntities.DbSyncLogHeader

import java.sql.SQLException
import java.util.ArrayList

/** Repository for sync log  */
class SyncLogRepository : ISyncLogRepositoryInternal, ISyncLogRepositoryExternal
{
    /**
     * Insert data from Dto
     * @return id of header record
     */
    private fun insertLogData(dto: SyncLogDto): Long
    {
        val headerToInsert = DbSyncLogHeader(dto)
        headerToInsert.save()

        val dataToInsert = DbSyncLogData(dto)
        dataToInsert.header = headerToInsert
        dataToInsert.save()

        return headerToInsert.id!!
    }

    /** Get data record for header record  */
    private fun getData(headerId: Long): DbSyncLogData = Select().from(DbSyncLogData::class.java).where("SyncLogDataHeader = ?", headerId).executeSingle<DbSyncLogData>()

    /** Insert data into log (internal method, executed in external transaction)  */
    override fun insert(dto: SyncLogDto)
    {
        //Log.d(LogTags.Sync, dto.toLogString("INPUT: "));
        if (dto.operationType == SyncLogDto.OperationCreate || dto.operationType == SyncLogDto.OperationDelete)
        {
            val headerId = insertLogData(dto)
            Log.d(LogTags.SYNC_PROCESS, "Insert input log record. Id is: " + headerId)
        }
        else
        {                               // Get not processed data's headers for our entity
            val dbHeaders = Select().from(DbSyncLogHeader::class.java).where("EntityUid = ? AND IsProcessing = ?", *arrayOf(dto.entityUid, "0")).orderBy("id").execute<DbSyncLogHeader>()
            val dbData = dbHeaders.map { getData(it.id!!) }         // Get data for headers

            if (dbHeaders.size == 0)        // No data - no merge needed - add record to sync log
            {
                val headerId = insertLogData(dto)
                Log.d(LogTags.SYNC_PROCESS, "No data to merge - insert input log record. Id is: " + headerId)
            }
            else
            {
                val dataToMerge = ArrayList<SyncLogDto>(dbHeaders.size + 1)         // Create DTOs for merging
                dbHeaders.indices.mapTo(dataToMerge) { SyncLogDto(dbHeaders[it], dbData[it]) }
                dataToMerge.add(dto)               // Source list for merge
                Log.d(LogTags.SYNC_PROCESS, "Total records to merge: " + dataToMerge.size)

                // Merging
                val mergeTo = dataToMerge[0]            // We'll merge to first record
                //Log.d(LogTags.Sync, mergeTo.toLogString("TARGET RECORD: "));

                for (i in 1..dataToMerge.size - 1)      // Skip mergeTo record
                {
                    val mergeFrom = dataToMerge[i]
                    //Log.d(LogTags.Sync, mergeFrom.toLogString("RECORD TO MERGE: "));

                    for ((key, value) in mergeFrom.strValues)
                        mergeTo.strValues.put(key, value)

                    for ((key, value) in mergeFrom.binValues)
                        mergeTo.binValues.put(key, value)
                }

                // Delete all intermediate records
                for (i in 1..dataToMerge.size - 1 - 1)          // Skip first (MergeTo) and last (@dto param)
                {
                    val id = dataToMerge[i].id
                    Model.delete(DbSyncLogHeader::class.java, id)
                    Log.d(LogTags.SYNC_PROCESS, "Delete merged record with id: " + id)
                }

                // Save merged record
                //Log.d(LogTags.Sync, mergeTo.toLogString("RESULT IS: "));
                val headerToUpdate = dbHeaders[0]
                headerToUpdate.updateFieldsFromDto(mergeTo)
                headerToUpdate.save()

                val dataToUpdate = dbData[0]
                dataToUpdate.updateFieldsFromDto(mergeTo)
                dataToUpdate.save()
            }
        }
    }

    /** Get Id of log records to process  */
    @Throws(SQLException::class)
    override fun getDataToProcess(): List<Long>
    {
        return TransactionWrapper.processInTransaction<List<Long>> {
            SQLiteUtils.rawQuery<Model>(DbSyncLogHeader::class.java, "UPDATE SyncLogHeader SET IsProcessing = ?", arrayOf("1"))        // Set processing flag - without condition

            Select("id").from(DbSyncLogHeader::class.java).orderBy("id").execute<DbSyncLogHeader>().map { it.id }      // Get and return data
        }
    }

    /**
     * Get log record by Id
     * @return null if record not found
     */
    @Throws(SQLException::class)
    override fun getRecord(id: Long): SyncLogDto? =
        TransactionWrapper.processWithoutTransaction<SyncLogDto?> {
            Select().from(DbSyncLogHeader::class.java).where("id = ?", id).executeSingle<DbSyncLogHeader>()?.let{SyncLogDto(it, this.getData(id))}
    }

    /** Remove all locks on log records  */
    @Throws(SQLException::class)
    override fun resetAllLocks()
    {
        TransactionWrapper.processInTransaction { SQLiteUtils.rawQuery<Model>(DbSyncLogHeader::class.java, "UPDATE SyncLogHeader SET IsProcessing = ?", arrayOf("0"))  }
    }

    /** Remove log record  */
    @Throws(SQLException::class)
    override fun removeLogRecord(id: Long)
    {
        TransactionWrapper.processInTransaction {
            this.getData(id).delete()
            Model.delete(DbSyncLogHeader::class.java, id)
        }
    }
}