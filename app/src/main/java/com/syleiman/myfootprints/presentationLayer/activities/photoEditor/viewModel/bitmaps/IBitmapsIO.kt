package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.bitmaps

import android.graphics.Bitmap
import com.syleiman.myfootprints.common.files.IFileSingleOperation

/** I/O interface for bitmaps */
interface IBitmapsIO
{
    /**
     * Load bitmap from file
     * @param bitmapFileName - name of file
     * @return decoded bitmap
     */
    fun load(bitmapFileName: String): Bitmap

    /**
     * Save bitmap to file with random name in app private area.
     * @param bitmap bitmap to saveToPrivateArea
     * @return file name of saved bitmap without path
     */
    fun save(bitmap: Bitmap, returnName : Boolean = true): IFileSingleOperation

    /** Remove file */
    fun delete(bitmapFileName: String)
}