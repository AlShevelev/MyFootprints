package com.syleiman.myfootprints.businessLayer.footprintsService

import android.app.Activity
import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.modelLayer.dto.FootprintForGalleryDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintSaveDto
import com.syleiman.myfootprints.businessLayer.footprintsService.extUpdateProcessing.CreateProcessor
import com.syleiman.myfootprints.businessLayer.footprintsService.extUpdateProcessing.DeleteProcessor
import com.syleiman.myfootprints.businessLayer.footprintsService.extUpdateProcessing.ExpUpdateProcessorBase
import com.syleiman.myfootprints.businessLayer.footprintsService.extUpdateProcessing.UpdateProcessor
import com.syleiman.myfootprints.common.letNull
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.modelLayer.dto.FootprintDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintForGridDto
import com.syleiman.myfootprints.modelLayer.dto.FootprintMarkerDto
import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.mainActivityCoverService.MainActivityCoverService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.businessLayer.tasksService.ITasksService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.sql.SQLException
import javax.inject.Inject

/** Working with footprint  */
class FootprintsService
@Inject
constructor(
    private val tasksService: ITasksService,
    private val sysInfoService: ISystemInformationService,
    private val mainActivityCoverService: MainActivityCoverService,
    private val localDb: ILocalDbService,
    private val bitmapService: IBitmapService)  : IFootprintsService
{

    /** Create footprint in normal mode  */
    @Throws(IOException::class, SQLException::class)
    private fun createFootprint(context: Activity, footprint: FootprintSaveDto) =
        FootprintCreator(
            context,
            tasksService,
            sysInfoService,
            mainActivityCoverService,
            localDb,
            bitmapService).saveFootprint(footprint)

    /**
     * Save footprint async
     * @param footprint footprint to saveToPrivateArea
     * @param result    callback (true - success)
     */
    override fun createFootprint(context: Activity, footprint: FootprintSaveDto, result: (Boolean) -> Unit)
    {
        Observable.fromCallable {
            this.createFootprint(context, footprint)
            true
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                result(true)            // Success
            }, { s ->
                s.printStackTrace()    // Error
                result(false)
            }
        )
    }

    /**
     * Update footprint
     * @param context
     * @param footprint footprint to saveToPrivateArea
     * @param result    callback (true - success)
     */
    override fun updateFootprint(
        context: Activity,
        footprint: FootprintSaveDto,
        isPhotoUpdated: Boolean,
        isCommentUpdated: Boolean,
        isLocationUpdated: Boolean,
        result: (Boolean) -> Unit)
    {
        Observable.fromCallable {
            FootprintUpdator(
                context,
                tasksService,
                sysInfoService,
                mainActivityCoverService,
                localDb,
                bitmapService).saveFootprint(footprint, isPhotoUpdated, isCommentUpdated, isLocationUpdated)
            true
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                result(true)        // Success
            }, { s ->
                s.printStackTrace()    // Error
                result(false)
            }
        )
    }

    /**
     * Get all footprints asynchronous
     * @param result list of footprints (null - in case of error)
     */
    override fun getAllMarkers(result: (List<FootprintMarkerDto>?) -> Unit)
    {
        Observable.fromCallable { localDb.footprints().getAllMarkers() }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( { o ->
            result(o as List<FootprintMarkerDto>) },     // Success
            { s ->
                s.printStackTrace()    // Error
                result(null)
            }
        )
    }

    /**
     * Get all footprints for gallery sorted by create moment in descending order
     * @param result list of footprints (null - in case of error)
     */
    override fun getAllForGrid(result: (List<FootprintForGridDto>?) -> Unit)
    {
        Observable.fromCallable { localDb.footprints().getAllForGrid() }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( { o ->
            result(o as List<FootprintForGridDto>) }, // Success
            { s ->
                s.printStackTrace()    // Error
                result(null)
            }
        )
    }

    /**
     * List of footprints' ids sorted by CreationDate
     * @return null in case of error
     */
    override fun getAllIdsOrderByCreation(normalSortOrder: Boolean, result: (List<Long>?) -> Unit)
    {
        Observable.fromCallable { localDb.footprints().getAllIdsOrderByCreation(normalSortOrder) }.
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( { o ->
            result(o as List<Long>) },      // Success
            { s ->
                s.printStackTrace()    // Error
                result(null)
            }
        )
    }

    /** Get footprint by its id
     * @return null in case of error
     */
    @Suppress("UNUSED_VARIABLE")
    override fun getForGallery(id: Long): FootprintForGalleryDto?
    {
        try
        {
            val (id1, comment, creationDate, markerColor, photo, geo) = localDb.footprints().getById(id)
            return FootprintForGalleryDto(
                id1,
                photo!!.photoFileName!!,
                comment!!,
                LatLng(geo!!.latitude, geo.longitude),
                geo.countryName,
                geo.cityName,
                photo.mapSnapshotFileName.letNull({it}, {null}),
                creationDate)
        }
        catch (e: SQLException) // Logged in DAL
        {
            return null
        }

    }

    /**
     * Delete footprint by its id
     * @param result false in case of error
     */
    override fun deleteFootprint(id: Long, result: (Boolean) -> Unit)
    {
        Observable.fromCallable {
            val filesToDelete = localDb.footprints().delete(id)       // Delete all from Db

            for (fileName in filesToDelete)         // Remove files
            {
                val fileToDelete = FileSingle.withName(fileName).inPrivate()
                val thumbnailToDelete = FileSingle.fromExistWithPrefix(fileToDelete, FileSingle.THUMBNAIL_FILES_PREFIX)

                fileToDelete.delete()
                thumbnailToDelete.delete()
            }

            mainActivityCoverService.updateOnRemoveFootprint(id)               // Update cover on main page
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            { result(true) },        // Success
            { s ->
                s.printStackTrace()    // Error
                result(false)
            }
        )
    }

    /**
     * Get footprint by id
     * @return null in case of error
     */
    override fun getById(footprintId: Long): FootprintDto?
    {
        try
        {
            return localDb.footprints().getById(footprintId)
        }
        catch (e: SQLException)
        {
            return null
        }
    }

    /**
     * Get processor for external log records
     * @param logRecord
     */
    override fun getExpUpdateProcessor(logRecord: SyncLogDto): ExpUpdateProcessorBase
    {
        val operation = logRecord.operationType

        if (operation == SyncLogDto.OperationCreate)
            return CreateProcessor(logRecord, localDb, mainActivityCoverService, bitmapService)
        else if (operation == SyncLogDto.OperationUpdate)
            return UpdateProcessor(logRecord, localDb, mainActivityCoverService, bitmapService)
        else if (operation == SyncLogDto.OperationDelete)
            return DeleteProcessor(logRecord, mainActivityCoverService, localDb)
        else
            throw UnsupportedOperationException("This code is not supported: " + logRecord.operationType)
    }
}