package com.syleiman.myfootprints.modelLayer.dto

import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import java.util.Date

/** Dto for sync log - build methods  */
interface ISyncLogDtoBuild {
    /** Add field and value to fields collections   */
    fun addValue(field: String, value: String?): ISyncLogDtoBuild

    /** Add field and value to fields collections   */
    fun addValue(field: String, value: Double): ISyncLogDtoBuild

    /** Add field and value to fields collections   */
    fun addValue(field: String, value: Long): ISyncLogDtoBuild

    /** Add field and value to fields collections   */
    fun addValue(field: String, value: Date): ISyncLogDtoBuild

    /** Add field and value to fields collections   */
    fun addValue(field: String, value: Int): ISyncLogDtoBuild

    /** Add field and value to fields collections   */
    fun addBinValue(field: String, value: ByteArray?): ISyncLogDtoBuild

    /** Add file's content (as array of bytes)  */
    fun addFile(field: String, file: IFileSingleOperation, bitmapService: IBitmapService): ISyncLogDtoBuild

    /** Get entity  */
    fun build(): SyncLogDto
}
