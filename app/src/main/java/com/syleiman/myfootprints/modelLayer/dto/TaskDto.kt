package com.syleiman.myfootprints.modelLayer.dto


import com.syleiman.myfootprints.modelLayer.dbEntities.DbTask

import java.util.Date

/** One task */
data class TaskDto(
        var id: Long,                       // Unique id of task
        var type: TaskTypes,                // Type of task
        var creationDate: Date?,             // Date/time when task was created
        var lastAttemptDate: Date?,         // Date/time when try to run task last time
        var attempts: Int,                  // How many times we try to run task (0 - not try yet)
        var errorCode: TaskErrorCodes,      // Last error's code (0 - without error)
        var errorText: String?,             // Last error's text
        var footprintId: Long,              // Id of footprint
        var data1: String?,                  // Some data
        var data2: String?,
        var data3: String?,
        var data4: String?,
        var data5: String?,
        var data6: String?,
        var data7: String?,
        var data8: String?,
        var data9: String?,
        var data10: String?)
{

    constructor(dbTask: DbTask) : this (
        dbTask.id!!,
        TaskTypes.from(dbTask.type),
        dbTask.creationDate,
        dbTask.lastAttemptDate,
        dbTask.attempts,
        TaskErrorCodes.from(dbTask.errorCode),
        dbTask.errorText,
        dbTask.footprintId,
        dbTask.data1,
        dbTask.data2,
        dbTask.data3,
        dbTask.data4,
        dbTask.data5,
        dbTask.data6,
        dbTask.data7,
        dbTask.data8,
        dbTask.data9,
        dbTask.data10)

    constructor(
            type: TaskTypes,
            footprintId: Long,
            data1: String?,
            data2: String?,
            data3: String?,
            data4: String?,
            data5: String?,
            data6: String?,
            data7: String?,
            data8: String?,
            data9: String?,
            data10: String?)
            : this(
                0,
                type,
                Date(),
                null,
                0,
                TaskErrorCodes.None,
                null,
                footprintId,
                data1,
                data2,
                data3,
                data4,
                data5,
                data6,
                data7,
                data8,
                data9,
                data10)
}