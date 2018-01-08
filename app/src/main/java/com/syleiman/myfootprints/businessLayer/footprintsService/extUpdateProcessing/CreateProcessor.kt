package com.syleiman.myfootprints.businessLayer.footprintsService.extUpdateProcessing

import android.graphics.Bitmap
import android.util.Log
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.modelLayer.dto.FootprintDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintGeoDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintPhotoDto
import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto
import com.syleiman.myfootprints.businessLayer.mainActivityCoverService.IMainActivityCoverService
import java.io.IOException
import java.sql.SQLException

/** Create footprint from log record   */
class CreateProcessor(
    syncRecord: SyncLogDto,
    localDb: ILocalDbService,
    private val mainActivityCoverService: IMainActivityCoverService,
    private val bitmapService: IBitmapService) : ExpUpdateProcessorBase(syncRecord, localDb)
{
    /** Process update  */
    @Throws(SQLException::class, IOException::class)
    override fun process()
    {
        if (localDb.footprints().isFootprintExist(syncRecord.entityUid))       // can find footprint - do nothing
            return

        val photoFileData = syncRecord.getBinValue(SyncLogDto.PhotoFileFootprintField)              // Photo
        Log.d(LogTags.SYNC_PROCESS, "photoFileData. Size: " + photoFileData!!.size + "[bytes]")
        val photoFile = FileSingle.withRandomName("", bitmapService.getFileExt(Bitmap.CompressFormat.JPEG)).inPrivate()

        val mapSnapshotData = syncRecord.getBinValue(SyncLogDto.MapSnapshotFileFootprintField)
        var mapSnapshotFile: IFileSingleOperation? = null                    // Map snapshot
        if (mapSnapshotData != null)
            mapSnapshotFile = FileSingle.withRandomName("", bitmapService.getFileExt(Bitmap.CompressFormat.JPEG)).inPrivate()

        bitmapService.save(photoFileData, photoFile)          // Create photo file

        val thumbnailFile = FileSingle.fromExistWithPrefix(photoFile, FileSingle.THUMBNAIL_FILES_PREFIX)        // Create thumbnail
        bitmapService.createThumbnail(photoFile, thumbnailFile)

        if (mapSnapshotData != null)
            bitmapService.save(mapSnapshotData, mapSnapshotFile!!)              // Create map snapshot file

        val footprintDto = FootprintDto(
            0,
            syncRecord.getStringValue(SyncLogDto.Companion.CommentFootprintField)!!,
            syncRecord.getDateValue(SyncLogDto.Companion.CreationDateFootprintField)!!, // Data for Db
            syncRecord.getIntValue(SyncLogDto.Companion.MarkerColorFootprintField)!!,
            FootprintPhotoDto(0, photoFile.getName(), if (mapSnapshotFile != null) mapSnapshotFile.getName() else null),
            FootprintGeoDto(
                    0,
                    syncRecord.getDoubleValue(SyncLogDto.Companion.LatitudeFootprintField)!!,
                    syncRecord.getDoubleValue(SyncLogDto.Companion.LongitudeFootprintField)!!,
                    syncRecord.getStringValue(SyncLogDto.Companion.CountryNameFootprintField),
                    syncRecord.getStringValue(SyncLogDto.Companion.CountryCodeFootprintField),
                    syncRecord.getStringValue(SyncLogDto.Companion.CityNameFootprintField)))

        val footprintId = localDb.footprints().createExternal(footprintDto, syncRecord.entityUid)!!           // Save

        mainActivityCoverService.updateOnAddFootprint(photoFile.getName(), footprintId)            // Update cover's info
    }
}
