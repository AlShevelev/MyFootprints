package com.syleiman.myfootprints.modelLayer.dbEntities

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto

/** Sync log - header only (for performance)  */
@Table(name = "SyncLogHeader")
class DbSyncLogHeader() : Model()
{
    /** Operation type (CUD)  */
    @Column(name = "OperationType")
    var operationType: String? = null

    /** Type of synced entity  */
    @Column(name = "EntityType", index = true)
    var entityType: Int = 0

    /** Unique if of entity  */
    @Column(name = "EntityUid")
    var entityUid: String? = null

    /** Is record in sync process  */
    @Column(name = "IsProcessing")
    var isProcessing: Boolean = false

    constructor(syncLogDto: SyncLogDto) : this()
    {
        updateFieldsFromDto(syncLogDto)
    }

    /** Update data in fields from Dto  */
    fun updateFieldsFromDto(syncLogDto: SyncLogDto)
    {
        operationType = syncLogDto.operationType
        entityType = syncLogDto.entityType
        entityUid = syncLogDto.entityUid
        isProcessing = false
    }
}