package com.syleiman.myfootprints.businessLayer.localDbService.repositories

import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto

/** Repository for sync log - internal interface  */
interface ISyncLogRepositoryInternal
{
    /** Insert data into log (internal method, executed in external transaction)  */
    fun insert(dto: SyncLogDto)
}
