package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.bitmaps

import android.graphics.Bitmap
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.modelLayer.enums.BitmapsQuality
import javax.inject.Inject

/** I/O operations with bitmaps */
class BitmapsIO
@Inject
constructor(private val bitmapsService : IBitmapService) : IBitmapsIO
{
    /**
     * Load bitmap from file
     * @param bitmapFileName - name of file
     * @return decoded bitmap
     */
    override fun load(bitmapFileName: String): Bitmap
    {
        return bitmapsService.load(FileSingle.withName(bitmapFileName).inShared())
    }

    /**
     * Save bitmap to file with random name in app private area.
     * @param bitmap bitmap to saveToPrivateArea
     * @param returnName - if @true returns name of file without path otherwise returns full path to file
     * @return file name of saved bitmap without path
     */
    override fun save(bitmap: Bitmap, returnName : Boolean): IFileSingleOperation
    {
        val file = FileSingle.withRandomName(FileSingle.TEMP_FILES_PREFIX, "png").inShared()
        bitmapsService.save(bitmap, file, BitmapsQuality.VeryLow, Bitmap.CompressFormat.PNG)
        return file
    }

    /** Remove file */
    override fun delete(bitmapFileName: String)
    {
        FileSingle.withName(bitmapFileName).inShared().delete()
    }
}