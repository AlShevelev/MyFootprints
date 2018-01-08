package com.syleiman.myfootprints.businessLayer.bitmapService

import android.graphics.Bitmap
import android.graphics.Point
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.modelLayer.enums.BitmapsQuality
import io.reactivex.Single

/** Interface for BitmapService */
interface IBitmapService
{
    /** Get bitmap from Raw  */
    fun getFromRaw(resourceId: Int): Bitmap

    /**
     * Get size of bitmap
     * @return size of bitmap
     */
    fun getSize(bitmapFile: IFileSingleOperation): Point

    /**
     * Load bitmap from file
     * @param orientation - rotation angle of bitmap (ExifInterface.ORIENTATION_*)
     * @return decoded bitmap
     */
    fun load(bitmapFile: IFileSingleOperation, orientation: Int): Bitmap

    /** Load bitmap from file and correct its orientation  */
    fun load(bitmapFile: IFileSingleOperation): Bitmap

    /** Loads image, corrects its size and orientation (to place into some holder) */
    fun loadAndInscribeQuick(bitmapFile: IFileSingleOperation, holderWidth: Int, holderHeight : Int): Bitmap

    /** Loads image, corrects its orientation and inscribe to some holder (over scaling - strict but not quick) */
    fun loadAndInscribeStrict(bitmapFile: IFileSingleOperation, holderWidth: Int, holderHeight : Int): Bitmap

    /** Loads image, corrects its orientation and inscribe to some holder (over scaling - strict but not quick) */
    fun loadAndInscribeStrictRx(bitmapFile: IFileSingleOperation, holderWidth: Int, holderHeight : Int): Single<Bitmap>

    /**
     * Get rotation angle for saved bitmap (from here: http://stackoverflow.com/questions/14066038/why-image-captured-using-camera-intent-gets-rotated-on-some-devices-in-android)
     * @return ExifInterface.ORIENTATION_*
     */
    fun getOrientation(bitmapFile: IFileSingleOperation): Int

    /**
     * Save bitmap to file with random name in app private area.
     * @param bitmap bitmap to saveToPrivateArea
     * @param quality quality of bitmap
     * @param compressFormat image format
     * @return file of saved bitmap
     */
    fun saveToPrivateArea(bitmap: Bitmap, quality: BitmapsQuality, compressFormat: Bitmap.CompressFormat): IFileSingleOperation

    /**
     * Save bitmap to file
     * @param bitmap bitmap to saveToPrivateArea
     * @param quality quality of bitmap
     * @param compressFormat image format
     * @return file name of saved bitmap without path
     */
    fun save(bitmap: Bitmap, file : IFileSingleOperation, quality: BitmapsQuality, compressFormat: Bitmap.CompressFormat): String

    /**
     * Save bitmap to file
     * @param bitmap bitmap to saveToPrivateArea
     * @param quality quality of bitmap
     * @param compressFormat image format
     * @return file name of saved bitmap without path
     */
    fun saveRx(bitmap: Bitmap, file : IFileSingleOperation, quality: BitmapsQuality, compressFormat: Bitmap.CompressFormat): Single<String>

    /**
     * Save bitmap to file with random name in app private area.
     * @param data data to saveToPrivateArea
     * @param fileToSave file to saveToPrivateArea
     */
    fun save(data: ByteArray, fileToSave: IFileSingleOperation)

    /** Get extension of the file  */
    fun getFileExt(compressFormat: Bitmap.CompressFormat): String

    /**
     * Read data from bitmap file
     * @return set of bytes
     */
    fun loadToBytes(bitmapFile: IFileSingleOperation): ByteArray?

    /** Get processor for placing effects on bitmap */
    fun getBitmapEffectsProcessing() : IBitmapEffectsProcessing

    /** Creates thumbnail, saves it and returns result bitmap */
    fun createThumbnail(source: IFileSingleOperation, thumbnailFile: IFileSingleOperation)
}