package com.syleiman.myfootprints.businessLayer.mainActivityCoverService

import android.graphics.Bitmap
import com.syleiman.myfootprints.modelLayer.dto.MainActivityCoverDto
import java.sql.SQLException

/** Facade for working with main activity cover  */
interface IMainActivityCoverService
{
    /** Get cover  */
    fun getCover(resultCallback: (MainActivityCoverDto?) -> Unit)

    /** Get default cover image as bitmap  */
    fun getDefaultImage(): Bitmap

    /** Update cover's data when footprint was added  */
    fun updateOnAddFootprint(lastPhotoFileName: String, lastFootprintId: Long)

    /** Update cover's data when footprint was updated  */
    fun updateOnUpdateFootprint(photoFileName: String?, footprintId: Long)

    /** Update cover's data when footprint was removed  */
    @Throws(SQLException::class)
    fun updateOnRemoveFootprint(lastFootprintId: Long)
}
