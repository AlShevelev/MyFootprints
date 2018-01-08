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

/** Process footprint saving  */
class FootprintCreator(
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
    fun saveFootprint(footprint: FootprintSaveDto)
    {
        val photo = bitmapService.load(footprint.photoFile)
        val photoFile = bitmapService.saveToPrivateArea(photo, BitmapsQuality.Moderate, Bitmap.CompressFormat.JPEG)

        val thumbnailPhotoFile = FileSingle.fromExistWithPrefix(photoFile, FileSingle.THUMBNAIL_FILES_PREFIX)
        bitmapService.createThumbnail(photoFile, thumbnailPhotoFile)                // Create thumbnail

        var snapshotFile: IFileSingleOperation? = null
        if (footprint.mapSnapshot != null)
            snapshotFile = bitmapService.saveToPrivateArea(footprint.mapSnapshot!!, BitmapsQuality.Low, Bitmap.CompressFormat.JPEG)

        removeTempFiles()        // Remove all temp files - camera's images and map's snapshot

        val footprintId = saveToDb(footprint, photoFile.getName(), snapshotFile?.getName())                // Save to db

        mainActivityCoverFacade.updateOnAddFootprint(photoFile.getName(), footprintId)            // Update cover's info

        tasks.createGeoCodingTask(footprint.location!!, footprintId)          // Create task for fill City and Country and try to process it
        // App.startSync();
    }

    /** Save footprint to Db  */
    @Throws(SQLException::class)
    private fun saveToDb(footprint: FootprintSaveDto, photoFileName: String, snapshotFilename: String?): Long
    {
        val footprintDto = FootprintDto(
            0,
            footprint.comment!!,
            Date(),
            0,
            FootprintPhotoDto(0, photoFileName, snapshotFilename),
            FootprintGeoDto(0, footprint.location!!.latitude, footprint.location!!.longitude, null, null, null))
        return localDb.footprints().create(footprintDto)!!
    }
}