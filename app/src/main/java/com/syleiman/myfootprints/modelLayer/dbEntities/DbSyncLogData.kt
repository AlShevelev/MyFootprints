package com.syleiman.myfootprints.modelLayer.dbEntities

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.syleiman.myfootprints.modelLayer.dto.SyncLogDto

/** Sync log - data only (for performance)  */
@Table(name = "SyncLogData")
class DbSyncLogData() : Model()
{
    /** Some field  */
    @Column(name = "Field1Str")
    var field1Str: String? = null

    /** Some data  */
    @Column(name = "Value1Str")
    var value1Str: String? = null

    /** Some field  */
    @Column(name = "Field2Str")
    var field2Str: String? = null

    /** Some data  */
    @Column(name = "Value2Str")
    var value2Str: String? = null

    /** Some field  */
    @Column(name = "Field3Str")
    var field3Str: String? = null

    /** Some data  */
    @Column(name = "Value3Str")
    var value3Str: String? = null

    /** Some field  */
    @Column(name = "Field4Str")
    var field4Str: String? = null

    /** Some data  */
    @Column(name = "Value4Str")
    var value4Str: String? = null

    /** Some field  */
    @Column(name = "Field5Str")
    var field5Str: String? = null

    /** Some data  */
    @Column(name = "Value5Str")
    var value5Str: String? = null

    /** Some field  */
    @Column(name = "Field6Str")
    var field6Str: String? = null

    /** Some data  */
    @Column(name = "Value6Str")
    var value6Str: String? = null

    /** Some field  */
    @Column(name = "Field7Str")
    var field7Str: String? = null

    /** Some data  */
    @Column(name = "Value7Str")
    var value7Str: String? = null

    /** Some field  */
    @Column(name = "Field8Str")
    var field8Str: String? = null

    /** Some data  */
    @Column(name = "Value8Str")
    var value8Str: String? = null

    /** Some field  */
    @Column(name = "Field9Str")
    var field9Str: String? = null

    /** Some data  */
    @Column(name = "Value9Str")
    var value9Str: String? = null

    /** Some field  */
    @Column(name = "Field10Str")
    var field10Str: String? = null

    /** Some data  */
    @Column(name = "Value10Str")
    var value10Str: String? = null

    /** Some field  */
    @Column(name = "Field1Bin")
    var field1Bin: String? = null

    /** Some data  */
    @Column(name = "Value1Bin")
    var value1Bin: ByteArray? = null

    /** Some field  */
    @Column(name = "Field2Bin")
    var field2Bin: String? = null

    /** Some data  */
    @Column(name = "Value2Bin")
    var value2Bin: ByteArray? = null

    /** Some field  */
    @Column(name = "Field3Bin")
    var field3Bin: String? = null

    /** Some data  */
    @Column(name = "Value3Bin")
    var value3Bin: ByteArray? = null

    /** Some field  */
    @Column(name = "Field4Bin")
    var field4Bin: String? = null

    /** Some data  */
    @Column(name = "Value4Bin")
    var value4Bin: ByteArray? = null

    /** Some field  */
    @Column(name = "Field5Bin")
    var field5Bin: String? = null

    /** Some data  */
    @Column(name = "Value5Bin")
    var value5Bin: ByteArray? = null

    @Column(name = "SyncLogDataHeader", index = true)
    var header: DbSyncLogHeader? = null

    constructor(syncLogDto: SyncLogDto) : this()
    {
        updateFieldsFromDto(syncLogDto, false)
    }

    /** Update data in fields from Dto  */
    fun updateFieldsFromDto(syncLogDto: SyncLogDto) = updateFieldsFromDto(syncLogDto, true)

    /** Update data in fields from Dto  */
    private fun updateFieldsFromDto(syncLogDto: SyncLogDto, clearDataFields: Boolean)
    {
        val totalValuesStr = syncLogDto.strValues.size
        if (totalValuesStr > 10)
            throw IndexOutOfBoundsException("Too many data in fieldsStr: " + totalValuesStr)

        val totalValuesBin = syncLogDto.binValues.size
        if (totalValuesBin > 5)
            throw IndexOutOfBoundsException("Too many data in fieldsBin: " + totalValuesBin)

        if (clearDataFields)
        {
            field1Str = null
            value1Str = null
            field2Str = null
            value2Str = null
            field3Str = null
            value3Str = null
            field4Str = null
            value4Str = null
            field5Str = null
            value5Str = null
            field6Str = null
            value6Str = null
            field7Str = null
            value7Str = null
            field8Str = null
            value8Str = null
            field9Str = null
            value9Str = null
            field10Str = null
            value10Str = null
            field1Bin = null
            value1Bin = null
            field2Bin = null
            value2Bin = null
            field3Bin = null
            value3Bin = null
            field4Bin = null
            value4Bin = null
            field5Bin = null
            value5Bin = null
        }

        var index = 0
        for ((key, value) in syncLogDto.strValues)
        {
            when (index)
            {
                0 ->
                {
                    field1Str = key
                    value1Str = value
                }
                1 ->
                {
                    field2Str = key
                    value2Str = value
                }
                2 ->
                {
                    field3Str = key
                    value3Str = value
                }
                3 ->
                {
                    field4Str = key
                    value4Str = value
                }
                4 ->
                {
                    field5Str = key
                    value5Str = value
                }
                5 ->
                {
                    field6Str = key
                    value6Str = value
                }
                6 ->
                {
                    field7Str = key
                    value7Str = value
                }
                7 ->
                {
                    field8Str = key
                    value8Str = value
                }
                8 ->
                {
                    field9Str = key
                    value9Str = value
                }
                9 ->
                {
                    field10Str = key
                    value10Str = value
                }
            }
            index++
        }

        index = 0
        for ((key, value) in syncLogDto.binValues)
        {
            when (index)
            {
                0 ->
                {
                    field1Bin = key
                    value1Bin = value
                }
                1 ->
                {
                    field2Bin = key
                    value2Bin = value
                }
                2 ->
                {
                    field3Bin = key
                    value3Bin = value
                }
                3 ->
                {
                    field4Bin = key
                    value4Bin = value
                }
                4 ->
                {
                    field5Bin = key
                    value5Bin = value
                }
            }
            index++
        }
    }
}