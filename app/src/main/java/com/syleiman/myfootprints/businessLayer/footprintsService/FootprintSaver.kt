package com.syleiman.myfootprints.businessLayer.footprintsService

import android.app.Activity
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.mainActivityCoverService.IMainActivityCoverService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.businessLayer.tasksService.ITasksService
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.common.files.FilesMulti

/** Base class for update and create footprint  */
abstract class FootprintSaver(
    protected val context: Activity,
    protected val tasks: ITasksService,
    protected val systemInformation: ISystemInformationService,
    protected val mainActivityCoverFacade: IMainActivityCoverService,
    protected val bitmapService: IBitmapService)
{

    /** Remove all temp files - camera's images and map's snapshot */
    protected fun removeTempFiles() = FilesMulti.inShared().clear(FileSingle.TEMP_FILES_PREFIX)
}