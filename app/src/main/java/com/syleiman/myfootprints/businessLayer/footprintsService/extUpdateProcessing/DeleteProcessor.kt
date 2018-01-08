package com.syleiman.myfootprints.businessLayer.footprintsService.extUpdateProcessing

import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto
import com.syleiman.myfootprints.businessLayer.mainActivityCoverService.IMainActivityCoverService
import java.io.IOException
import java.sql.SQLException

/** Delete footprint from log record   */
class DeleteProcessor(
    syncRecord: SyncLogDto,
    private val mainActivityCoverService : IMainActivityCoverService,
    localDb: ILocalDbService) : ExpUpdateProcessorBase(syncRecord, localDb)
{
    /** Process update  */
    @Throws(SQLException::class, IOException::class)
    override fun process()
    {
        if (!localDb.footprints().isFootprintExist(syncRecord.entityUid))        // can't find footprint - do nothing
            return

        val deleteResult = localDb.footprints().deleteExternal(syncRecord.entityUid)       // Delete all from Db

        for (fileName in deleteResult.second)        // Remove files
        {
            val fileToDelete = FileSingle.withName(fileName).inPrivate()
            val thumbnailToDelete = FileSingle.fromExistWithPrefix(fileToDelete, FileSingle.THUMBNAIL_FILES_PREFIX)

            fileToDelete.delete()
            thumbnailToDelete.delete()
        }

        mainActivityCoverService.updateOnRemoveFootprint(deleteResult.first)               // Update cover on main page
    }
}
