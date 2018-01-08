package com.syleiman.myfootprints.businessLayer.bitmapService

import android.graphics.Bitmap
import android.graphics.Point

/** Interface for BitmapEffectsProcessing */
interface IBitmapEffectsProcessing
{
    /**
    * Scale bitmap to some size
    * @param sourceBitmap
    * @param newSize
    * @return scaled bitmap
    */
    fun scale(sourceBitmap: Bitmap, newSize: Point): Bitmap

    /** */
    fun rotate(bitmap: Bitmap, degrees: Float): Bitmap

    /**  */
    fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap

    /** */
    fun emboss(src: Bitmap): Bitmap

    /**
     * @param redPart [0, 150], default => 100
     * @param greenPart [0, 150], default => 100
     * @param bluePart [0, 150], default => 100
     */
    fun colorFilter(src: Bitmap, redPart: Double, greenPart: Double, bluePart: Double): Bitmap

    /** Gaussian blur  */
    fun gaussian(src: Bitmap): Bitmap

    /** @param bitOffset (16, 32, 64, 128) */
    fun colorDepth(src: Bitmap, bitOffset: Int): Bitmap

    /**  */
    fun sharpen(src: Bitmap): Bitmap

    /**  */
    fun noise(source: Bitmap): Bitmap

    /**
     * @param src
     * @param value [-255, +255] -> Default = 0
     * @return
     */
    fun brightness(src: Bitmap, value: Int): Bitmap

    /**  */
    fun sepia(src: Bitmap): Bitmap

    /**
     * @param src
     * @param redPart redPart, greenPart, bluePart [0, 48]
     * @param greenPart redPart, greenPart, bluePart [0, 48]
     * @param bluePart redPart, greenPart, bluePart [0, 48]
     * @return
     */
    fun gamma(src: Bitmap, redPart: Double, greenPart: Double, bluePart: Double): Bitmap

    /**
     * @param src
     * @param value [-100, +100] -> Default = 0
     * @return
     */
    fun contrast(src: Bitmap, value: Double): Bitmap

    /**
     * @param src
     * @param value    [0, 200] -> Default = 100
     * @return
     */
    fun saturation(src: Bitmap, value: Int): Bitmap

    /**  */
    fun grayscale(src: Bitmap): Bitmap

    /**  */
    fun vignette(image: Bitmap): Bitmap

    /**
     * @param bitmap
     * @param hue [0, 360] -> Default = 0
     */
    fun hue(bitmap: Bitmap, hue: Float): Bitmap

    /**  */
    fun tint(src: Bitmap, color: Int): Bitmap

    /**   */
    fun invert(src: Bitmap): Bitmap

    /**
     * @param src
     * @param type
     * @param percentValue [0, 150], type = (1, 2, 3) => (R, G, B)
     * @return
     */
    fun boost(src: Bitmap, type: Int, percentValue: Float): Bitmap

    /** */
    fun sketch(src: Bitmap): Bitmap
}