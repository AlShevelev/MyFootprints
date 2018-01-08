package com.syleiman.myfootprints.businessLayer.mainActivityCoverService

import android.graphics.Bitmap
import android.media.ExifInterface
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.modelLayer.dto.MainActivityCoverDto
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.sql.SQLException
import javax.inject.Inject

/** Facade for working with main activity cover */
class MainActivityCoverService
@Inject
constructor(
    private val optionsCache: IOptionsCacheService,
    private val localDbDal: ILocalDbService,
    private val bitmapService: IBitmapService) : IMainActivityCoverService
{
    /** Get cover  */
    override fun getCover(resultCallback: (MainActivityCoverDto?) -> Unit)
    {
        Observable.fromCallable {
            MainActivityCoverDto().apply {
                totalFootprints = optionsCache.getTotalFootprints() ?: 0

                if (totalFootprints == 0)
                    lastPhotoBitmap = this@MainActivityCoverService.getDefaultImage()
                else
                {
                    lastPhotoFileName = optionsCache.getLastPhotoFileName()
                    if (lastPhotoFileName != null)
                        lastPhotoBitmap = bitmapService.load(FileSingle.withName(lastPhotoFileName!!).inPrivate(), ExifInterface.ORIENTATION_NORMAL)

                    lastFootprintId = optionsCache.getLastFootprintId()
                }
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ o ->
            resultCallback.invoke(o as MainActivityCoverDto) },
            { s ->
                s.printStackTrace()    // Error
                resultCallback.invoke(null)
            }
        )
    }

    /** Get default cover image as bitmap */
    override fun getDefaultImage(): Bitmap
    {
        return bitmapService.getFromRaw(R.raw.default_cover)
    }

    /** Update cover's data when footprint was added  */
    override fun updateOnAddFootprint(lastPhotoFileName: String, lastFootprintId: Long)
    {
        optionsCache.setTotalFootprints((optionsCache.getTotalFootprints() ?: 0) + 1)
        optionsCache.setLastPhotoFileName(lastPhotoFileName)
        optionsCache.setLastFootprintId(lastFootprintId)
    }

    /**
     * Update cover's data when footprint was updated
     * @param photoFileName
     * @param footprintId
     */
    override fun updateOnUpdateFootprint(photoFileName: String?, footprintId: Long)
    {
        val lastFootprintId = optionsCache.getLastFootprintId()
        if (footprintId == lastFootprintId)
            optionsCache.setLastPhotoFileName(photoFileName!!)
    }

    /** Update cover's data when footprint was removed  */
    @Throws(SQLException::class)
    override fun updateOnRemoveFootprint(lastFootprintId: Long)
    {
        optionsCache.setTotalFootprints((optionsCache.getTotalFootprints() ?: 0) - 1)

        val lastFootprintIdOld = optionsCache.getLastFootprintId()
        if (lastFootprintId == lastFootprintIdOld)
        {
            val lastFootprint = localDbDal.footprints().getLastFootprint()
            if (lastFootprint != null)
            {
                optionsCache.setLastPhotoFileName(lastFootprint.photo!!.photoFileName!!)
                optionsCache.setLastFootprintId(lastFootprint.id)
            }
        }
    }
}