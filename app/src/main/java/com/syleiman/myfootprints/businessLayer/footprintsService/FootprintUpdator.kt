package com.syleiman.myfootprints.businessLayer.footprintsService

import android.app.Activity
import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.mainActivityCoverService.IMainActivityCoverService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.businessLayer.tasksService.ITasksService
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.modelLayer.dto.FootprintDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintGeoDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintPhotoDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintSaveDto
import com.syleiman.myfootprints.modelLayer.enums.BitmapsQuality
import java.io.IOException
import java.sql.SQLException
import java.util.*

/** Process footprint update  */
class FootprintUpdator(
    context: Activity,
    tasks: ITasksService,
    systemInformation: ISystemInformationService,
    mainActivityCoverFacade: IMainActivityCoverService,
    private val localDb: ILocalDbService,
    bitmapService: IBitmapService)
    : FootprintSaver(
        context,
        tasks,
        systemInformation,
        mainActivityCoverFacade,
        bitmapService)
{

    /**  */
    @Throws(IOException::class, SQLException::class)
    fun saveFootprint(footprint: FootprintSaveDto, isPhotoUpdated: Boolean, isCommentUpdated: Boolean, isLocationUpdated: Boolean)
    {
        var photoFile: IFileSingleOperation? = null
        if (isPhotoUpdated)
        {
            val photo = bitmapService.load(footprint.photoFile)
            photoFile = bitmapService.saveToPrivateArea(photo, BitmapsQuality.Moderate, Bitmap.CompressFormat.JPEG)

            val thumbnailPhotoFile = FileSingle.fromExistWithPrefix(photoFile, FileSingle.THUMBNAIL_FILES_PREFIX)
            bitmapService.createThumbnail(photoFile, thumbnailPhotoFile)                // Create thumbnail
        }

        var snapshotFile: IFileSingleOperation? = null
        if (footprint.mapSnapshot != null)
            snapshotFile = bitmapService.saveToPrivateArea(footprint.mapSnapshot!!, BitmapsQuality.Low, Bitmap.CompressFormat.JPEG)

        removeTempFiles()        // Remove all temp files - camera's images and map's snapshot

        val fileNamesToDelete = saveToDb(footprint, photoFile?.getName(), snapshotFile?.getName(), isPhotoUpdated, isCommentUpdated, isLocationUpdated)                // Save to db

        for (fileNameToDelete in fileNamesToDelete)        // Remove old files
        {
            val fileToDelete = FileSingle.withName(fileNameToDelete).inPrivate()
            val thumbnailToDelete = FileSingle.fromExistWithPrefix(fileToDelete, FileSingle.THUMBNAIL_FILES_PREFIX)

            fileToDelete.delete()
            thumbnailToDelete.delete()              // Remove thumbnail
        }

        if (isPhotoUpdated)
            mainActivityCoverFacade.updateOnUpdateFootprint(photoFile!!.getName(), footprint.footprintId!!)            // Update cover's info

        if (isLocationUpdated)
            tasks.createGeoCodingTask(footprint.location!!, footprint.footprintId!!)          // Create task for fill City and Country and try to process it

        //App.startSync();
    }

    /** Save footprint to Db  */
    @Throws(SQLException::class)
    private fun saveToDb(footprint: FootprintSaveDto, photoFileName: String?, snapshotFilename: String?, isPhotoUpdated: Boolean, isCommentUpdated: Boolean, isLocationUpdated: Boolean): List<String>
    {
        val footprintDto = FootprintDto(
            footprint.footprintId!!,
            footprint.comment!!,
            Date(),
            0,
            FootprintPhotoDto(0, photoFileName, snapshotFilename),
            FootprintGeoDto(0, footprint.location!!.latitude, footprint.location!!.longitude, null, null, null))
        return localDb.footprints().update(footprintDto, isPhotoUpdated, isCommentUpdated, isLocationUpdated)
    }
}
