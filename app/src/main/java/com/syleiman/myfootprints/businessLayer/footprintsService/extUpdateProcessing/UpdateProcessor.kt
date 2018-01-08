package com.syleiman.myfootprints.businessLayer.footprintsService.extUpdateProcessing

import android.graphics.Bitmap
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.modelLayer.dto.FootprintDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintGeoDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintPhotoDto
import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.mainActivityCoverService.IMainActivityCoverService
import java.io.IOException
import java.sql.SQLException
import java.util.*

/** Update footprint from log record   */
class UpdateProcessor(
    syncRecord: SyncLogDto,
    localDb: ILocalDbService,
    private val mainActivityCoverService: IMainActivityCoverService,
    private val bitmapService: IBitmapService) : ExpUpdateProcessorBase(syncRecord, localDb)
{
    /** Process update  */
    @Throws(IOException::class, SQLException::class)
    override fun process()
    {
        if (!localDb.footprints().isFootprintExist(syncRecord.entityUid))     // can't find footprint - do nothing
            return

        val photoFileData = syncRecord.getBinValue(SyncLogDto.PhotoFileFootprintField)
        var photoFile: IFileSingleOperation? = null                  // Photo
        if (photoFileData != null)
            photoFile = FileSingle.withRandomName("", bitmapService.getFileExt(Bitmap.CompressFormat.JPEG)).inPrivate()

        val mapSnapshotData = syncRecord.getBinValue(SyncLogDto.MapSnapshotFileFootprintField)
        var mapSnapshotFile: IFileSingleOperation? = null            // Map snapshot
        if (mapSnapshotData != null)
            mapSnapshotFile = FileSingle.withRandomName("", bitmapService.getFileExt(Bitmap.CompressFormat.JPEG)).inPrivate()

        if (photoFileData != null)
        {
            bitmapService.save(photoFileData, photoFile!!)          // Create photo file

            val thumbnailFile = FileSingle.fromExistWithPrefix(photoFile, FileSingle.THUMBNAIL_FILES_PREFIX)
            bitmapService.createThumbnail(photoFile, thumbnailFile)
        }

        if (mapSnapshotData != null)                // Create map snapshot file
            bitmapService.save(mapSnapshotData, mapSnapshotFile!!)

        val footprintGeoDto = if (syncRecord.hasStrValue(SyncLogDto.LatitudeFootprintField))        // Get geodata if existed
            FootprintGeoDto(
                    0,
                    syncRecord.getDoubleValue(SyncLogDto.Companion.LatitudeFootprintField)!!,
                    syncRecord.getDoubleValue(SyncLogDto.Companion.LongitudeFootprintField)!!,
                    syncRecord.getStringValue(SyncLogDto.Companion.CountryNameFootprintField),
                    syncRecord.getStringValue(SyncLogDto.Companion.CountryCodeFootprintField),
                    syncRecord.getStringValue(SyncLogDto.Companion.CityNameFootprintField))
        else
            null

        val footprintDto = FootprintDto(
            0,
            syncRecord.getStringValue(SyncLogDto.Companion.CommentFootprintField),
            Date(),                                     // Data for Db - not used in case of update
            0,
            FootprintPhotoDto(0, photoFile?.getName(), mapSnapshotFile?.getName()),
            footprintGeoDto)

        val saveResult = localDb.footprints().updateExternal(footprintDto, syncRecord.entityUid)       // Save to Db

        for (fileNameToDelete in saveResult.second)        // Remove old files
        {
            val fileToDelete = FileSingle.withName(fileNameToDelete).inPrivate()
            val thumbnailToDelete = FileSingle.fromExistWithPrefix(fileToDelete, FileSingle.THUMBNAIL_FILES_PREFIX)

            fileToDelete.delete()
            thumbnailToDelete.delete()
        }

        if (photoFile != null)
            mainActivityCoverService.updateOnUpdateFootprint(photoFile.getName(), saveResult.first)            // Update cover's info
    }
}
