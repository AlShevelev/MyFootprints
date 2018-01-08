package com.syleiman.myfootprints.businessLayer.footprintsService.extUpdateProcessing

import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto

import java.io.IOException
import java.sql.SQLException

/** Base class for processing ext updates  */
abstract class ExpUpdateProcessorBase protected constructor(
    protected val syncRecord: SyncLogDto,
    protected val localDb: ILocalDbService)
{
    /** Process update  */
    @Throws(SQLException::class, IOException::class)
    abstract fun process()
}
