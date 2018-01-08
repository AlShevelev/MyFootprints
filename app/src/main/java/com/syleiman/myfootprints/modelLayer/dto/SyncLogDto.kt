package com.syleiman.myfootprints.modelLayer.dto

import com.syleiman.myfootprints.common.DataToString
import com.syleiman.myfootprints.common.debug.Hashing
import com.syleiman.myfootprints.common.letNull
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.modelLayer.dbEntities.DbSyncLogData
import com.syleiman.myfootprints.modelLayer.dbEntities.DbSyncLogHeader

import java.io.IOException
import java.util.Date
import java.util.TreeMap

/** Dto for sync log  */
class SyncLogDto(
        var id: Long,                               // Id of log record in Db
        var operationType: String,                  // Operation type (CUD)
        var entityType: Int,                        // Type of synced entity
        var entityUid: String) : ISyncLogDtoBuild   // Unique if of entity
{

    companion object {
        val OperationCreate = "C"           // Operations codes for @operationType field
        val OperationUpdate = "U"
        val OperationDelete = "D"

        // Entity type code for @entityType field
        var Footprint = 1

        var CommentFootprintField = "Fp.Comment"
        var CreationDateFootprintField = "Fp.CreationDate"
        var MarkerColorFootprintField = "Fp.MarkerColor"
        var LatitudeFootprintField = "Fp.Latitude"
        var LongitudeFootprintField = "Fp.Longitude"
        var CountryNameFootprintField = "Fp.CountryName"
        var CountryCodeFootprintField = "Fp.CountryCode"
        var CityNameFootprintField = "Fp.CityName"
        var PhotoFileFootprintField = "Fp.PhotoFile"
        var MapSnapshotFileFootprintField = "Fp.MapSnapshotFile"

        /** Start build entity  */
        fun startBuild(operationType: String, entityType: Int, entityUid: String): ISyncLogDtoBuild  = SyncLogDto(0, operationType, entityType, entityUid)
    }

    var strValues: MutableMap<String, String?> = TreeMap()           // String values [key: value]
    var binValues: MutableMap<String, ByteArray?> = TreeMap()           // Binary values [key: value]

    constructor(dbSyncLogHeader: DbSyncLogHeader, dbSyncLogData: DbSyncLogData) : this(dbSyncLogHeader.id!!, dbSyncLogHeader.operationType!!, dbSyncLogHeader.entityType, dbSyncLogHeader.entityUid!!)
    {

        dbSyncLogData.field1Str?.let{strValues.put(dbSyncLogData.field1Str!!, dbSyncLogData.value1Str)}
        dbSyncLogData.field2Str?.let{strValues.put(dbSyncLogData.field2Str!!, dbSyncLogData.value2Str)}
        dbSyncLogData.field3Str?.let{strValues.put(dbSyncLogData.field3Str!!, dbSyncLogData.value3Str)}
        dbSyncLogData.field4Str?.let{strValues.put(dbSyncLogData.field4Str!!, dbSyncLogData.value4Str)}
        dbSyncLogData.field5Str?.let{strValues.put(dbSyncLogData.field5Str!!, dbSyncLogData.value5Str)}
        dbSyncLogData.field6Str?.let{strValues.put(dbSyncLogData.field6Str!!, dbSyncLogData.value6Str)}
        dbSyncLogData.field7Str?.let{strValues.put(dbSyncLogData.field7Str!!, dbSyncLogData.value7Str)}
        dbSyncLogData.field8Str?.let{strValues.put(dbSyncLogData.field8Str!!, dbSyncLogData.value8Str)}
        dbSyncLogData.field9Str?.let{strValues.put(dbSyncLogData.field9Str!!, dbSyncLogData.value9Str)}
        dbSyncLogData.field10Str?.let{strValues.put(dbSyncLogData.field10Str!!, dbSyncLogData.value10Str)}

        dbSyncLogData.field1Bin?.let{binValues.put(dbSyncLogData.field1Bin!!, dbSyncLogData.value1Bin)}
        dbSyncLogData.field2Bin?.let{binValues.put(dbSyncLogData.field2Bin!!, dbSyncLogData.value2Bin)}
        dbSyncLogData.field3Bin?.let{binValues.put(dbSyncLogData.field3Bin!!, dbSyncLogData.value3Bin)}
        dbSyncLogData.field4Bin?.let{binValues.put(dbSyncLogData.field4Bin!!, dbSyncLogData.value4Bin)}
        dbSyncLogData.field5Bin?.let{binValues.put(dbSyncLogData.field5Bin!!, dbSyncLogData.value5Bin)}
    }

    /** Add field and value to fields collections   */
    override fun addValue(field: String, value: String?): ISyncLogDtoBuild {
        strValues.put(field, value)
        return this
    }

    /** Get value from fields collections   */
    fun getStringValue(field: String): String?  = strValues[field]

    /** Add field and value to fields collections   */
    override fun addValue(field: String, value: Double): ISyncLogDtoBuild = addValue(field, DataToString.toString(value))

    /** Get value from fields collections   */
    fun getDoubleValue(field: String): Double? {
        val strValue = strValues[field] ?: return null
        return DataToString.fromStringToDouble(strValue, 0.0)
    }

    /** Add field and value to fields collections   */
    override fun addValue(field: String, value: Long): ISyncLogDtoBuild = addValue(field, DataToString.toString(value))

    /** Get value from fields collections   */
    fun getLongValue(field: String): Long? {
        val strValue = strValues[field] ?: return null
        return DataToString.fromStringToLong(strValue)
    }

    /** Add field and value to fields collections   */
    override fun addValue(field: String, value: Date): ISyncLogDtoBuild = addValue(field, DataToString.toString(value))

    /** Get value from fields collections   */
    fun getDateValue(field: String): Date? {
        val strValue = strValues[field] ?: return null
        return DataToString.fromStringToDate(strValue)
    }

    /** Add field and value to fields collections   */
    override fun addValue(field: String, value: Int): ISyncLogDtoBuild = addValue(field, DataToString.toString(value))

    /** Get value from fields collections   */
    fun getIntValue(field: String): Int? {
        val strValue = strValues[field] ?: return null
        return DataToString.fromStringToInt(strValue)
    }

    /** Add field and value to fields collections  */
    override fun addBinValue(field: String, value: ByteArray?): ISyncLogDtoBuild {
        binValues.put(field, value)
        return this
    }

    /** Check the string value existance by the key  */
    fun hasStrValue(field: String): Boolean = strValues.containsKey(field)

    /** Check the binary value existance by the key  */
    fun hasBinValue(field: String): Boolean = binValues.containsKey(field)

    /** Add field and value to fields collections  */
    fun getBinValue(field: String): ByteArray? = binValues[field]

    /** Add file's content (as array of bytes)  */
    override fun addFile(field: String, file: IFileSingleOperation, bitmapService: IBitmapService): ISyncLogDtoBuild
    {
        try
        {
            return addBinValue(field, bitmapService.loadToBytes(file)!!)
        }
        catch (e: IOException)
        {
            e.printStackTrace()
        }

        return this
    }

    /** Get entity  */
    override fun build(): SyncLogDto  = this

    /** Create string for logging  */
    fun toLogString(prefix: String): String {
        val sb = StringBuilder()
        sb.append(prefix)
        sb.append("Id: ").append(id).append("; operationType: ").append(operationType).append("; entityType: ").append(entityType).append("; entityUid: ").append(entityUid).append("; ")

        for ((key, value) in strValues)
            value.letNull({sb.append("[").append(key).append(" : ").append(it).append("]")}, {sb.append("[").append(key).append(" : null]")})

        for ((key, value) in binValues)
            value.letNull({sb.append("[").append(key).append(" : ").append(it!!.size).append(" bytes : " + Hashing.calculateSHA(it) + "]")}, {sb.append("[").append(key).append(" : null]")})

        return sb.toString()
    }
}