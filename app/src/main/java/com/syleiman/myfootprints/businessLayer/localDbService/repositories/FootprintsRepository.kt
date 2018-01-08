package com.syleiman.myfootprints.businessLayer.localDbService.repositories

import android.util.Pair
import com.activeandroid.Model
import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import com.syleiman.myfootprints.common.iif
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.localDbService.TransactionWrapper
import com.syleiman.myfootprints.modelLayer.dto.*
import com.syleiman.myfootprints.modelLayer.dbEntities.DbFootprint
import com.syleiman.myfootprints.modelLayer.dbEntities.DbFootprintGeo
import com.syleiman.myfootprints.modelLayer.dbEntities.DbFootprintPhoto
import com.syleiman.myfootprints.modelLayer.dbEntities.DbTask
import java.sql.SQLException
import java.util.*

class FootprintsRepository(
    private val syncLogRepository: ISyncLogRepositoryInternal,
    private val bitmapService: IBitmapService) : IFootprintsRepository
{
    /**
     * Save footprint
     * @return id of footprint in Db (or null in case of error)
     */
    @Throws(SQLException::class)
    override fun create(footprint: FootprintDto): Long?
    {
        return TransactionWrapper.processInTransaction<Long?> {
            val dbFootprint = DbFootprint(footprint)
            dbFootprint.save()

            val dbFootprintPhoto = DbFootprintPhoto(footprint.photo!!)
            dbFootprintPhoto.footprint = dbFootprint
            dbFootprintPhoto.save()

            val dbFootprintGeo = DbFootprintGeo(footprint.geo!!)
            dbFootprintGeo.footprint = dbFootprint
            dbFootprintGeo.save()

            val syncLogDto = SyncLogDto.
                    startBuild(SyncLogDto.OperationCreate, SyncLogDto.Footprint, dbFootprint.uid).
                    addValue(SyncLogDto.CommentFootprintField, dbFootprint.comment!!).// Add data to sync log
                    addValue(SyncLogDto.CreationDateFootprintField, dbFootprint.creationDate!!).
                    addValue(SyncLogDto.MarkerColorFootprintField, dbFootprint.markerColor).
                    addValue(SyncLogDto.LatitudeFootprintField, dbFootprintGeo.latitude).
                    addValue(SyncLogDto.LongitudeFootprintField, dbFootprintGeo.longitude).
                    addValue(SyncLogDto.CountryNameFootprintField, dbFootprintGeo.countryName).
                    addValue(SyncLogDto.CountryCodeFootprintField, dbFootprintGeo.countryCode).
                    addValue(SyncLogDto.CityNameFootprintField, dbFootprintGeo.cityName).
                    addFile(
                        SyncLogDto.PhotoFileFootprintField,
                        FileSingle.withName(dbFootprintPhoto.photoFileName!!).inPrivate(),
                        bitmapService)

            if (dbFootprintPhoto.mapSnapshotFileName == null)
                syncLogDto.addBinValue(SyncLogDto.MapSnapshotFileFootprintField, null)
            else
                syncLogDto.addFile(
                    SyncLogDto.MapSnapshotFileFootprintField,
                    FileSingle.withName(dbFootprintPhoto.mapSnapshotFileName!!).inPrivate(),
                    bitmapService)

            syncLogRepository.insert(syncLogDto.build())

            dbFootprint.id
        }
    }

    /**
     * Save footprint from external data
     * @param uid unique if of footprint
     * @return id of footprint in Db (or null in case of error)
     * @throws SQLException
     */
    @Throws(SQLException::class)
    override fun createExternal(footprint: FootprintDto, uid: String): Long?
    {
        return TransactionWrapper.processInTransaction<Long?> {
            val dbFootprint = DbFootprint(footprint)
            dbFootprint.uid = uid
            dbFootprint.save()

            val dbFootprintPhoto = DbFootprintPhoto(footprint.photo!!)
            dbFootprintPhoto.footprint = dbFootprint
            dbFootprintPhoto.save()

            val dbFootprintGeo = DbFootprintGeo(footprint.geo!!)
            dbFootprintGeo.footprint = dbFootprint
            dbFootprintGeo.save()

            dbFootprint.id
        }
    }

    /**
     * Save footprint
     * @return name of files to delete
     */
    @Throws(SQLException::class)
    override fun update(footprint: FootprintDto, isPhotoUpdated: Boolean, isCommentUpdated: Boolean, isLocationUpdated: Boolean): List<String>
    {
        return TransactionWrapper.processInTransaction<List<String>> {
            val result = ArrayList<String>(2)

            val dbFootprint = Select().from(DbFootprint::class.java).where("id = ?", footprint.id).executeSingle<DbFootprint>()

            val syncLogDto = SyncLogDto.startBuild(SyncLogDto.OperationUpdate, SyncLogDto.Footprint, dbFootprint.uid)      // Start make sync log record

            if (isPhotoUpdated || footprint.photo!!.mapSnapshotFileName != null)
            {
                val dbPhoto = Select().from(DbFootprintPhoto::class.java).where("Footprint = ?", footprint.id).executeSingle<DbFootprintPhoto>()

                if (isPhotoUpdated)
                {
                    result.add(dbPhoto.photoFileName!!)
                    dbPhoto.photoFileName = footprint.photo!!.photoFileName

                    syncLogDto.addFile(
                        SyncLogDto.PhotoFileFootprintField,
                        FileSingle.withName(footprint.photo!!.photoFileName!!).inPrivate(),
                        bitmapService)
                }

                if (footprint.photo!!.mapSnapshotFileName != null)
                {
                    if (dbPhoto.mapSnapshotFileName != null)
                        result.add(dbPhoto.mapSnapshotFileName!!)

                    dbPhoto.mapSnapshotFileName = footprint.photo!!.mapSnapshotFileName

                    syncLogDto.addFile(
                        SyncLogDto.MapSnapshotFileFootprintField,
                        FileSingle.withName(footprint.photo!!.mapSnapshotFileName!!).inPrivate(),
                        bitmapService)
                }

                dbPhoto.save()
            }

            if (isCommentUpdated)
            {
                dbFootprint.comment = footprint.comment
                dbFootprint.save()

                syncLogDto.addValue(SyncLogDto.CommentFootprintField, footprint.comment!!)
            }

            if (isLocationUpdated)
            {
                val dbFootprintGeo = Select().from(DbFootprintGeo::class.java).where("Footprint = ?", footprint.id).executeSingle<DbFootprintGeo>()
                dbFootprintGeo.latitude = footprint.geo!!.latitude
                dbFootprintGeo.longitude = footprint.geo!!.longitude
                dbFootprintGeo.countryName = footprint.geo!!.countryName
                dbFootprintGeo.countryCode = footprint.geo!!.countryCode
                dbFootprintGeo.cityName = footprint.geo!!.cityName

                dbFootprintGeo.save()

                syncLogDto.addValue(SyncLogDto.LatitudeFootprintField, footprint.geo!!.latitude)
                syncLogDto.addValue(SyncLogDto.LongitudeFootprintField, footprint.geo!!.longitude)

                Delete().from(DbTask::class.java).where("FootprintId = ?", footprint.id).execute<Model>()
            }

            syncLogRepository.insert(syncLogDto.build())           // Add sync log record

            result
        }
    }

    /**
     * Update footprint from external data
     * @param footprint
     * *
     * @param uid       unique if of footprint
     * *
     * @return if of footprint in Db and names of files to delete
     * *
     * @throws SQLException
     */
    @Throws(SQLException::class)
    override fun updateExternal(footprint: FootprintDto, uid: String): Pair<Long, List<String>>
    {
        return TransactionWrapper.processInTransaction<Pair<Long, List<String>>> {
            val filesToDelete = ArrayList<String>(2)

            val dbFootprint = Select().from(DbFootprint::class.java).where("Uid = ?", uid).executeSingle<DbFootprint>()
            val footprintId = dbFootprint.id

            if (footprint.photo!!.photoFileName != null || footprint.photo!!.mapSnapshotFileName != null)
            {
                val dbPhoto = Select().from(DbFootprintPhoto::class.java).where("Footprint = ?", footprintId!!).executeSingle<DbFootprintPhoto>()

                if (footprint.photo!!.photoFileName != null)
                {
                    filesToDelete.add(dbPhoto.photoFileName!!)
                    dbPhoto.photoFileName = footprint.photo!!.photoFileName
                }

                if (footprint.photo!!.mapSnapshotFileName != null)
                {
                    if (dbPhoto.mapSnapshotFileName != null)
                        filesToDelete.add(dbPhoto.mapSnapshotFileName!!)
                    dbPhoto.mapSnapshotFileName = footprint.photo!!.mapSnapshotFileName
                }

                dbPhoto.save()
            }

            if (footprint.comment != null)
            {
                dbFootprint.comment = footprint.comment
                dbFootprint.save()
            }

            if (footprint.geo != null)
            {
                val dbFootprintGeo = Select().from(DbFootprintGeo::class.java).where("Footprint = ?", footprintId!!).executeSingle<DbFootprintGeo>()
                dbFootprintGeo.latitude = footprint.geo!!.latitude
                dbFootprintGeo.longitude = footprint.geo!!.longitude

                dbFootprintGeo.countryName = footprint.geo!!.countryName
                dbFootprintGeo.countryCode = footprint.geo!!.countryCode
                dbFootprintGeo.cityName = footprint.geo!!.cityName

                dbFootprintGeo.save()
            }

            Pair(footprintId, filesToDelete)
        }
    }

    /** Update footprints geoinformation  */
    @Throws(SQLException::class)
    override fun updateGeoInformation(footprintId: Long, countryName: String?, countryCode: String?, cityName: String?)
    {
        TransactionWrapper.processInTransaction {
            val dbFootprint = Select("id", "Uid").from(DbFootprint::class.java).where("id = ?", footprintId).executeSingle<DbFootprint>()
            //DbFootprint dbFootprint=new Select().from(DbFootprint.class).where("id = ?", footprintId).executeSingle();

            val dbGeo = Select().from(DbFootprintGeo::class.java).where("Footprint = ?", footprintId).executeSingle<DbFootprintGeo>()
            if (dbGeo != null)
            {
                dbGeo.countryName = countryName
                dbGeo.countryCode = countryCode
                dbGeo.cityName = cityName

                val syncLogDto = SyncLogDto.startBuild(SyncLogDto.OperationUpdate, SyncLogDto.Footprint, dbFootprint.uid).addValue(SyncLogDto.CountryNameFootprintField, countryName).addValue(SyncLogDto.CountryCodeFootprintField, countryCode).addValue(SyncLogDto.CityNameFootprintField, cityName).build()
                syncLogRepository.insert(syncLogDto)

                dbGeo.save()
            }
        }
    }

    /** Get all footprints  markers */
    @Throws(SQLException::class)
    override fun getAllMarkers(): List<FootprintMarkerDto>
    {
        return TransactionWrapper.processInTransaction<List<FootprintMarkerDto>> {
            val dbResult = Select().from(DbFootprintGeo::class.java).innerJoin(DbFootprint::class.java).on("Footprint.Id=FootprintGeo.Footprint").execute<DbFootprintGeo>()

            dbResult.map { FootprintMarkerDto(
                it.footprint!!.id!!,
                it.footprint!!.markerColor,
                it.latitude,
                it.longitude,
                it.footprint!!.comment!!,
                it.footprint!!.photos.first().photoFileName!!) }
        }
    }

    /**
     * Get all footprints for gallery sorted by create moment in descending order
     */
    @Throws(SQLException::class)
    override fun getAllForGrid(): List<FootprintForGridDto>
    {
        return TransactionWrapper.processInTransaction <List<FootprintForGridDto>> {
            Select().from(DbFootprint::class.java).
                innerJoin(DbFootprintGeo::class.java).
                on("Footprint.Id=FootprintGeo.Footprint").
                innerJoin(DbFootprintPhoto::class.java).
                on("Footprint.Id=FootprintPhoto.Footprint").
                orderBy("CreationDate DESC").
                execute<DbFootprint>().
            map {
                val geo = it.footprintsGeo[0]
                FootprintForGridDto(it.id!!, it.creationDate!!, it.photos[0].photoFileName!!, geo.cityName, geo.countryName)
            }
        }
    }

    /** Sort footprints by CreationDate and get ids  */
    @Throws(SQLException::class)
    override fun getAllIdsOrderByCreation(normalSortOrder: Boolean): List<Long>
    {
        val sort = normalSortOrder.iif({"CreationDate"}, {"CreationDate DESC"})

        return TransactionWrapper.processWithoutTransaction <List<Long>> {
            Select("id").from(DbFootprint::class.java).orderBy(sort).execute<DbFootprint>().
            map{it.id}
        }
    }

    /** Get footprint by its id  */
    @Throws(SQLException::class)
    override fun getById(id: Long): FootprintDto
    {
        return TransactionWrapper.processInTransaction<FootprintDto> {
            val dbFootprint = Select().from(DbFootprint::class.java).where("id = ?", id).executeSingle<DbFootprint>()
            val dbFootprintPhoto = Select().from(DbFootprintPhoto::class.java).where("Footprint = ?", id).executeSingle<DbFootprintPhoto>()
            val dbFootprintGeo = Select().from(DbFootprintGeo::class.java).where("Footprint = ?", id).executeSingle<DbFootprintGeo>()

            val result = FootprintDto(dbFootprint)
            result.geo = FootprintGeoDto(dbFootprintGeo)
            result.photo = FootprintPhotoDto(dbFootprintPhoto)

            result
        }
    }

    /**
     * Delete footprint
     * @return list of files names to delete (without path)
     */
    @Throws(SQLException::class)
    override fun delete(id: Long): List<String>
    {
        return TransactionWrapper.processInTransaction<List<String>> {
            val result = ArrayList<String>(2)

            val dbFootprint = Select().from(DbFootprint::class.java).where("id = ?", id).executeSingle<DbFootprint>()

            val dbPhoto = Select().from(DbFootprintPhoto::class.java).where("Footprint = ?", id).executeSingle<DbFootprintPhoto>()

            result.add(dbPhoto.photoFileName!!)

            if (dbPhoto.mapSnapshotFileName != null)
                result.add(dbPhoto.mapSnapshotFileName!!)

            dbPhoto.delete()

            Delete().from(DbFootprintGeo::class.java).where("Footprint = ?", id).execute<Model>()
            Delete().from(DbTask::class.java).where("FootprintId = ?", id).execute<Model>()

            val syncLogDto = SyncLogDto.startBuild(SyncLogDto.OperationDelete, SyncLogDto.Footprint, dbFootprint.uid).build()
            dbFootprint.delete()

            syncLogRepository.insert(syncLogDto)

            result
        }
    }

    /**
     * Delete footprint from external data
     * @param uid unique if of footprint
     * *
     * @return if of footprint in Db and names of files to delete
     */
    @Throws(SQLException::class)
    override fun deleteExternal(uid: String): Pair<Long, List<String>>
    {
        return TransactionWrapper.processInTransaction<Pair<Long, List<String>>> {
            val filesToDelete = ArrayList<String>(2)

            val dbFootprint = Select().from(DbFootprint::class.java).where("Uid = ?", uid).executeSingle<DbFootprint>()
            val footprintId = dbFootprint.id

            val dbPhoto = Select().from(DbFootprintPhoto::class.java).where("Footprint = ?", footprintId!!).executeSingle<DbFootprintPhoto>()

            filesToDelete.add(dbPhoto.photoFileName!!)

            if (dbPhoto.mapSnapshotFileName != null)
                filesToDelete.add(dbPhoto.mapSnapshotFileName!!)

            dbPhoto.delete()

            Delete().from(DbFootprintGeo::class.java).where("Footprint = ?", footprintId).execute<Model>()
            Delete().from(DbTask::class.java).where("FootprintId = ?", footprintId).execute<Model>()

            dbFootprint.delete()

            Pair(footprintId, filesToDelete)
        }
    }

    /**
     * Get last footprint
     * @return null in case of error or not footprints found
     */
    @Throws(SQLException::class)
    override fun getLastFootprint(): FootprintDto?
    {
        return TransactionWrapper.processInTransaction<FootprintDto?> {
            Select().from(DbFootprint::class.java).orderBy("CreationDate DESC").limit("1").executeSingle<DbFootprint>()?.let{
                val dbPhoto = Select().from(DbFootprintPhoto::class.java).where("Footprint = ?", it.id!!).executeSingle<DbFootprintPhoto>()
                FootprintDto(it).apply { photo = FootprintPhotoDto(dbPhoto) }
            }
        }
    }

    /** Is exist footprint with such Uid  */
    @Throws(SQLException::class)
    override fun isFootprintExist(uid: String): Boolean =
            TransactionWrapper.processWithoutTransaction<Boolean> { Select().from(DbFootprint::class.java).where("Uid = ?", uid).executeSingle<Model>() != null }
}