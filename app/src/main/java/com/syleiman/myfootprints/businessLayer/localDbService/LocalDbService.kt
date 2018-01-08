package com.syleiman.myfootprints.businessLayer.localDbService

import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.FootprintsRepository
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.IFootprintsRepository
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.IKeyValueStorageRepository
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.ILastLocationRepository
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.ISyncLogRepositoryExternal
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.ITasksRepository
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.KeyValueStorageRepository
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.LastLocationRepository
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.SyncLogRepository
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.TasksRepository

/** Dal for local Db  */
class LocalDbService(bitmapService: IBitmapService) : ILocalDbService
{
    private val keyValueStorageInstance: IKeyValueStorageRepository = KeyValueStorageRepository()
    private val lastLocationInstance: ILastLocationRepository = LastLocationRepository()
    private val tasks: ITasksRepository = TasksRepository()

    private val syncLogInstance: ISyncLogRepositoryExternal
    private val footprintsInstance: IFootprintsRepository

    init
    {
        val syncLog = SyncLogRepository()

        syncLogInstance = syncLog
        footprintsInstance = FootprintsRepository(syncLog, bitmapService)
    }

    override fun footprints(): IFootprintsRepository = footprintsInstance

    override fun keyValueStorage(): IKeyValueStorageRepository = keyValueStorageInstance

    override fun lastLocation(): ILastLocationRepository = lastLocationInstance

    override fun tasks(): ITasksRepository = tasks

    override fun syncLog(): ISyncLogRepositoryExternal = syncLogInstance
}

