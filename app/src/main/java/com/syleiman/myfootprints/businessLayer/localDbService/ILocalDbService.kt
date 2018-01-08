package com.syleiman.myfootprints.businessLayer.localDbService

import com.syleiman.myfootprints.businessLayer.localDbService.repositories.IFootprintsRepository
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.IKeyValueStorageRepository
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.ILastLocationRepository
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.ISyncLogRepositoryExternal
import com.syleiman.myfootprints.businessLayer.localDbService.repositories.ITasksRepository

/** Interface of DAL for local Db */
interface ILocalDbService
{
    fun footprints(): IFootprintsRepository
    fun keyValueStorage(): IKeyValueStorageRepository
    fun lastLocation(): ILastLocationRepository
    fun tasks(): ITasksRepository
    fun syncLog(): ISyncLogRepositoryExternal
}