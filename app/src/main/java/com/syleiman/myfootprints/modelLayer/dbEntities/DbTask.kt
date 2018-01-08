package com.syleiman.myfootprints.modelLayer.dbEntities

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.syleiman.myfootprints.modelLayer.dto.TaskDto
import java.util.*

/** One task  */
@Table(name = "Task")
class DbTask() : Model()
{
    /** Type of task  */
    @Column(name = "Type")
    var type: Int = 0

    /** Date/time when task was created  */
    @Column(name = "CreationDate")
    var creationDate: Date? = null

    /** Date/time when try to run task last time  */
    @Column(name = "LastAttemptDate")
    var lastAttemptDate: Date? = null

    /** How many times we try to run task (0 - not try yet)  */
    @Column(name = "Attempts")
    var attempts: Int = 0

    /** Last error's code (0 - without error)  */
    @Column(name = "ErrorCode")
    var errorCode: Int = 0

    /** Last error's text  */
    @Column(name = "ErrorText")
    var errorText: String? = null

    /** Id of footprint (we use @long but not DbFootprint as type to call this field dyrectly) */
    @Column(name = "FootprintId", index = true)
    var footprintId: Long = 0

    /** Some data  */
    @Column(name = "Data1")
    var data1: String? = null

    /** Some data  */
    @Column(name = "Data2")
    var data2: String? = null

    /** Some data  */
    @Column(name = "Data3")
    var data3: String? = null

    /** Some data  */
    @Column(name = "Data4")
    var data4: String? = null

    /** Some data  */
    @Column(name = "Data5")
    var data5: String? = null

    /** Some data  */
    @Column(name = "Data6")
    var data6: String? = null

    /** Some data  */
    @Column(name = "Data7")
    var data7: String? = null

    /** Some data  */
    @Column(name = "Data8")
    var data8: String? = null

    /** Some data  */
    @Column(name = "Data9")
    var data9: String? = null

    /** Some data  */
    @Column(name = "Data10")
    var data10: String? = null

    constructor(taskDto: TaskDto) : this()
    {
        type = taskDto.type.value
        creationDate = taskDto.creationDate
        lastAttemptDate = taskDto.lastAttemptDate
        attempts = taskDto.attempts
        errorCode = taskDto.errorCode.value
        errorText = taskDto.errorText
        footprintId = taskDto.footprintId
        data1 = taskDto.data1
        data2 = taskDto.data2
        data3 = taskDto.data3
        data4 = taskDto.data4
        data5 = taskDto.data5
        data6 = taskDto.data6
        data7 = taskDto.data7
        data8 = taskDto.data8
        data9 = taskDto.data9
        data10 = taskDto.data10
    }
}
