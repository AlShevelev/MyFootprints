package com.syleiman.myfootprints.businessLayer.bitmapService

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.media.ExifInterface
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.common.iif
import com.syleiman.myfootprints.modelLayer.enums.BitmapsQuality
import io.reactivex.Observable
import io.reactivex.Single
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/** Dal for working with filesAndBitmaps  */
class BitmapService : IBitmapService
{
    /** Get bitmap from Raw  */
    override fun getFromRaw(resourceId: Int): Bitmap = BitmapFactory.decodeStream(App.context.resources.openRawResource(resourceId))

    /**
     * Get size of bitmap
     * @return size of bitmap
     */
    override fun getSize(bitmapFile: IFileSingleOperation): Point
    {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(bitmapFile.getPath(), options)

        return Point(options.outWidth, options.outHeight)
    }

    /**
     * Load bitmap from file
     * @param orientation - rotation angle of bitmap (ExifInterface.ORIENTATION_*)
     * @return decoded bitmap
     */
    override fun load(bitmapFile: IFileSingleOperation, orientation: Int): Bitmap = load(bitmapFile, orientation, null)

    /** Load bitmap from file and correct its orientation  */
    override fun load(bitmapFile: IFileSingleOperation): Bitmap = load(bitmapFile, getOrientation(bitmapFile))

    /** Loads image, corrects its orientation and inscribe to some holder (over sample - quick but not strict) */
    override fun loadAndInscribeQuick(bitmapFile: IFileSingleOperation, holderWidth: Int, holderHeight : Int): Bitmap
    {
        val orientation = getOrientation(bitmapFile)

        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(bitmapFile.getPath(), bitmapOptions)

        var inSampleSize = 1

        val width = (orientation==ExifInterface.ORIENTATION_ROTATE_90 || orientation==ExifInterface.ORIENTATION_ROTATE_270).
            iif({bitmapOptions.outHeight}, {bitmapOptions.outWidth})        // Rotate sides if needed
        val height = (orientation==ExifInterface.ORIENTATION_ROTATE_90 || orientation==ExifInterface.ORIENTATION_ROTATE_270).
            iif({bitmapOptions.outWidth}, {bitmapOptions.outHeight})

        // Calculation inSampleSize
        while (height / inSampleSize > holderHeight && width / inSampleSize > holderWidth)
             inSampleSize *= 2

        bitmapOptions.inSampleSize = inSampleSize
        bitmapOptions.inJustDecodeBounds = false

        return load(bitmapFile, orientation, bitmapOptions)
    }

    /** Loads image, corrects its orientation and inscribe to some holder (over scaling - strict but not quick) */
    override fun loadAndInscribeStrict(bitmapFile: IFileSingleOperation, holderWidth: Int, holderHeight : Int): Bitmap
    {
        val photoSize = getSize(bitmapFile)
        val orientation = getOrientation(bitmapFile)

        if (photoSize.x > holderWidth || photoSize.x > holderHeight || photoSize.y > holderWidth || photoSize.y > holderHeight)        // Need resize
        {
            val maxScreenDimension = Math.max(holderWidth, holderHeight)
            var newPhotoSize: Point?

            if (photoSize.x > photoSize.y)            // Calculate new size
                newPhotoSize = Point(maxScreenDimension, (photoSize.y * maxScreenDimension.toFloat() / photoSize.x.toFloat()).toInt())
            else
                newPhotoSize = Point((photoSize.x * maxScreenDimension.toFloat() / photoSize.y.toFloat()).toInt(), maxScreenDimension)

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270)
                newPhotoSize = Point(newPhotoSize.y, newPhotoSize.x)           // Change sizes

            return getBitmapEffectsProcessing().scale(load(bitmapFile, orientation), newPhotoSize)           // and scale
        }
        else
            return load(bitmapFile, orientation)      // Return source bitmap
    }

    /** Loads image, corrects its orientation and inscribe to some holder (over scaling - strict but not quick) */
    override fun loadAndInscribeStrictRx(bitmapFile: IFileSingleOperation, holderWidth: Int, holderHeight: Int): Single<Bitmap>
    {
        return Observable.fromCallable {
            loadAndInscribeStrict(bitmapFile, holderWidth, holderHeight)
        }.singleOrError()
    }


    /**
     * Get rotation angle for saved bitmap (from here: http://stackoverflow.com/questions/14066038/why-image-captured-using-camera-intent-gets-rotated-on-some-devices-in-android)
     * @param bitmapFile - bitmap file name with path
     * @return ExifInterface.ORIENTATION_*
     */
    override fun getOrientation(bitmapFile: IFileSingleOperation): Int
    {
        try
        {
            return ExifInterface(bitmapFile.getPath()).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        }
        catch (e: IOException)
        {
            e.printStackTrace()
            return ExifInterface.ORIENTATION_UNDEFINED
        }
    }

    /** Get extension of the file  */
    override fun getFileExt(compressFormat: Bitmap.CompressFormat): String
    {
        when (compressFormat)
        {
            Bitmap.CompressFormat.JPEG -> return "jpg"
            Bitmap.CompressFormat.PNG -> return "png"
            else -> return "img"
        }
    }

    /**
     * Save bitmap to file with random name in app private area.
     * @param bitmap bitmap to saveToPrivateArea
     * @param quality quality of bitmap
     * @param compressFormat image format
     * @return file of saved bitmap
     */
    override fun saveToPrivateArea(bitmap: Bitmap, quality: BitmapsQuality, compressFormat: Bitmap.CompressFormat): IFileSingleOperation
    {
        val file = FileSingle.withRandomName("", getFileExt(compressFormat)).inPrivate()
        save(bitmap, file, quality, compressFormat)
        return file
    }

    /**
     * Save bitmap to file
     * @param bitmap bitmap to saveToPrivateArea
     * @param quality quality of bitmap
     * @param compressFormat image format
     * @return file name of saved bitmap without path
     */
    override fun save(bitmap: Bitmap, file : IFileSingleOperation, quality: BitmapsQuality, compressFormat: Bitmap.CompressFormat): String
    {
        FileOutputStream(file.getPath()).use { stream ->
            bitmap.compress(compressFormat, quality.value, stream)

            return file.getName()
        }
    }

    /**
     * Save bitmap to file
     * @param bitmap bitmap to saveToPrivateArea
     * @param quality quality of bitmap
     * @param compressFormat image format
     * @return file name of saved bitmap without path
     */
    override fun saveRx(bitmap: Bitmap, file: IFileSingleOperation, quality: BitmapsQuality, compressFormat: Bitmap.CompressFormat): Single<String>
    {
        return Observable.fromCallable {
            save(bitmap, file, quality, compressFormat)
        }.singleOrError()
    }

    /**
     * Save bitmap to file with random name in app private area.
     * @param data data to saveToPrivateArea
     * @param fileToSave file to saveToPrivateArea
     */
    override fun save(data: ByteArray, fileToSave: IFileSingleOperation)
    {
        FileOutputStream(fileToSave.getPath()).use { stream ->
            stream.write(data)
            stream.flush()
        }
    }

    /**
     * Read data from bitmap file
     * @return set of bytes
     * @throws IOException
     */
    override fun loadToBytes(bitmapFile: IFileSingleOperation): ByteArray?
    {
        val file = bitmapFile.getFile()
        if (!file.exists() || !file.isFile)
            return null

        val buffer = ByteArray(file.length().toInt())

        BufferedInputStream(FileInputStream(bitmapFile.getPath())).use { stream ->
            var bytesRead = stream.read(buffer)
            while (bytesRead != -1)
                bytesRead = stream.read(buffer, bytesRead, buffer.size - bytesRead)
        }
        // Log.d(LogTags.Sync, "loadToBytes. Size: "+buffer.length+"[bytes]; Hash: "+ Hashing.calculateSHA(buffer));

        return buffer
    }

    /** Get processor for placing effects on bitmap */
    override fun getBitmapEffectsProcessing(): IBitmapEffectsProcessing = BitmapEffectsProcessing()

    /**
     * Load bitmap from file with some options and rotate it
     * @param orientation - rotation angle of bitmap (ExifInterface.ORIENTATION_*)
     * @return decoded bitmap
     */
    private fun load(bitmapFile: IFileSingleOperation, orientation: Int, options : BitmapFactory.Options?): Bitmap
    {
        val source = BitmapFactory.decodeFile(bitmapFile.getPath(), options)

        // Rotate bitmap if needed (from here: http://stackoverflow.com/questions/14066038/why-image-captured-using-camera-intent-gets-rotated-on-some-devices-in-android)
        when (orientation)
        {
            ExifInterface.ORIENTATION_ROTATE_90 -> return getBitmapEffectsProcessing().rotate(source, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> return getBitmapEffectsProcessing().rotate(source, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> return getBitmapEffectsProcessing().rotate(source, 270f)
            else -> return source         // ExifInterface.ORIENTATION_NORMAL || ExifInterface.ORIENTATION_UNDEFINED
        }
    }

    /** Creates thumbnail and saves it to file */
    override fun createThumbnail(source: IFileSingleOperation, thumbnailFile: IFileSingleOperation)
    {
        val orientation = getOrientation(source)

        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = 2                  // half size
        save(load(source, orientation, bitmapOptions), thumbnailFile, BitmapsQuality.Low, Bitmap.CompressFormat.JPEG)
    }
}