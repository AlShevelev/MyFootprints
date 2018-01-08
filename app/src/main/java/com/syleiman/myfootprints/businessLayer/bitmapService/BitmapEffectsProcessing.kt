package com.syleiman.myfootprints.businessLayer.bitmapService

import android.graphics.*
import java.util.Random

import android.util.Log

/** Add effects to bitmap */
class BitmapEffectsProcessing : IBitmapEffectsProcessing
{
    /**
    * Scale bitmap to some size
    * @param sourceBitmap
    * @param newSize
    * @return scaled bitmap
    */
    override fun scale(sourceBitmap: Bitmap, newSize: Point): Bitmap =
        Bitmap.createScaledBitmap(sourceBitmap, newSize.x, newSize.y, true)     // filter=true - do bilinear or bicubic interpolation

    /** */
    override fun rotate(bitmap: Bitmap, degrees: Float): Bitmap
    {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**  */
    override fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap
    {
        val matrix = Matrix()
        matrix.preScale((if (horizontal) -1 else 1).toFloat(), (if (vertical) -1 else 1).toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /** */
    override fun emboss(src: Bitmap): Bitmap
    {
        val EmbossConfig = arrayOf(doubleArrayOf(-1.0, 0.0, -1.0), doubleArrayOf(0.0, 4.0, 0.0), doubleArrayOf(-1.0, 0.0, -1.0))

        val matrix = ConvolutionMatrix(3)
        matrix.applyConfig(EmbossConfig)
        matrix.Factor = 1.0
        matrix.Offset = 127.0
        return ConvolutionMatrix.computeConvolution3x3(src, matrix)
    }

    /**
     * @param redPart [0, 150], default => 100
     * @param greenPart [0, 150], default => 100
     * @param bluePart [0, 150], default => 100
     */
    override fun colorFilter(src: Bitmap, redPart: Double, greenPart: Double, bluePart: Double): Bitmap
    {
        var red = redPart
        var green = greenPart
        var blue = bluePart
        red /= 100
        green /= 100
        blue /= 100

        // image size
        val width = src.width
        val height = src.height

        val sourcePixels = IntArray(width * height)                    // Load all pixels
        src.getPixels(sourcePixels, 0, width, 0, 0, width, height)

        val destinationPixels = IntArray(width * height)

        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0..width - 1)
        {
            for (y in 0..height - 1)
            {
                // convert pixel color
                pixel = sourcePixels[x + y * width]
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel)
                R = (Color.red(pixel) * red).toInt()
                G = (Color.green(pixel) * green).toInt()
                B = (Color.blue(pixel) * blue).toInt()
                // set new color pixel to output bitmap

                destinationPixels[x + y * width] = Color.argb(A, R, G, B)
            }
        }

        // return final image
        return Bitmap.createBitmap(destinationPixels, width, height, src.config)
    }

    /** Gaussian blur  */
    override fun gaussian(src: Bitmap): Bitmap
    {
        val GaussianBlurConfig = arrayOf(doubleArrayOf(1.0, 2.0, 1.0), doubleArrayOf(2.0, 4.0, 2.0), doubleArrayOf(1.0, 2.0, 1.0))
        val matrix = ConvolutionMatrix(3)
        matrix.applyConfig(GaussianBlurConfig)
        matrix.Factor = 16.0
        matrix.Offset = 0.0
        return ConvolutionMatrix.computeConvolution3x3(src, matrix)
    }

    /** @param bitOffset (16, 32, 64, 128) */
    override fun colorDepth(src: Bitmap, bitOffset: Int): Bitmap
    {
        // convert image size
        val width = src.width
        val height = src.height

        val sourcePixels = IntArray(width * height)                    // Load all pixels
        src.getPixels(sourcePixels, 0, width, 0, 0, width, height)

        val destinationPixels = IntArray(width * height)

        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0..width - 1)
        {
            for (y in 0..height - 1)
            {
                // convert pixel color
                pixel = sourcePixels[x + y * width]
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)

                // round-off color offset
                R = R + bitOffset / 2 - (R + bitOffset / 2) % bitOffset - 1
                if (R < 0)
                {
                    R = 0
                }
                G = G + bitOffset / 2 - (G + bitOffset / 2) % bitOffset - 1
                if (G < 0)
                {
                    G = 0
                }
                B = B + bitOffset / 2 - (B + bitOffset / 2) % bitOffset - 1
                if (B < 0)
                {
                    B = 0
                }

                destinationPixels[x + y * width] = Color.argb(A, R, G, B)        // set pixel color to output bitmap
            }
        }

        return Bitmap.createBitmap(destinationPixels, width, height, src.config)
    }

    /**  */
    override fun sharpen(src: Bitmap): Bitmap
    {
        val SharpConfig = arrayOf(doubleArrayOf(0.0, -2.0, 0.0), doubleArrayOf(-2.0, 11.0, -2.0), doubleArrayOf(0.0, -2.0, 0.0))
        val matrix = ConvolutionMatrix(3)
        matrix.applyConfig(SharpConfig)
        matrix.Factor = 3.0
        return ConvolutionMatrix.computeConvolution3x3(src, matrix)
    }

    /**  */
    override fun noise(source: Bitmap): Bitmap
    {
        val COLOR_MAX = 0xFF

        // convert image size
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        // convert pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height)
        // a random object
        val random = Random()

        var index : Int
        // iteration through pixels
        for (y in 0..height - 1)
        {
            for (x in 0..width - 1)
            {
                // convert current index in 2D-matrix
                index = y * width + x
                // convert random color
                val randColor = Color.rgb(random.nextInt(COLOR_MAX),
                    random.nextInt(COLOR_MAX), random.nextInt(COLOR_MAX))
                // OR
                pixels[index] = pixels[index] or randColor
            }
        }
        // output bitmap
        val bmOut = Bitmap.createBitmap(width, height, source.config)
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height)

        return Bitmap.createBitmap(pixels, width, height, source.config)
    }

    /**
     * @param src
     * @param value [-255, +255] -> Default = 0
     * @return
     */
    override fun brightness(src: Bitmap, value: Int): Bitmap
    {
        // image size
        val width = src.width
        val height = src.height

        val sourcePixels = IntArray(width * height)                    // Load all pixels
        src.getPixels(sourcePixels, 0, width, 0, 0, width, height)

        val destinationPixels = IntArray(width * height)

        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0..width - 1)
        {
            for (y in 0..height - 1)
            {
                // convert pixel color
                pixel = sourcePixels[x + y * width]
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)

                // increase/decrease each channel
                R += value
                if (R > 255)
                {
                    R = 255
                } else if (R < 0)
                {
                    R = 0
                }

                G += value
                if (G > 255)
                {
                    G = 255
                } else if (G < 0)
                {
                    G = 0
                }

                B += value
                if (B > 255)
                {
                    B = 255
                } else if (B < 0)
                {
                    B = 0
                }

                // apply new pixel color to output bitmap
                destinationPixels[x + y * width] = Color.argb(A, R, G, B)
            }
        }

        return Bitmap.createBitmap(destinationPixels, width, height, src.config)
    }

    /**  */
    override fun sepia(src: Bitmap): Bitmap
    {
        // image size
        val width = src.width
        val height = src.height

        val sourcePixels = IntArray(width * height)                    // Load all pixels
        src.getPixels(sourcePixels, 0, width, 0, 0, width, height)

        val destinationPixels = IntArray(width * height)

        // constant grayscale
        val GS_RED = 0.3
        val GS_GREEN = 0.59
        val GS_BLUE = 0.11
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0..width - 1)
        {
            for (y in 0..height - 1)
            {
                // convert pixel color
                pixel = sourcePixels[x + y * width]
                // convert color on each channel
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)
                // apply grayscale sample
                R = (GS_RED * R + GS_GREEN * G + GS_BLUE * B).toInt()
                G = R
                B = G

                R += 110
                if (R > 255)
                {
                    R = 255
                }

                G += 65
                if (G > 255)
                {
                    G = 255
                }

                B += 20
                if (B > 255)
                {
                    B = 255
                }

                // set new pixel color to output image
                destinationPixels[x + y * width] = Color.argb(A, R, G, B)
            }
        }

        return Bitmap.createBitmap(destinationPixels, width, height, src.config)
    }

    /**
     * @param src
     * @param redPart redPart, greenPart, bluePart [0, 48]
     * @param greenPart redPart, greenPart, bluePart [0, 48]
     * @param bluePart redPart, greenPart, bluePart [0, 48]
     * @return
     */
    override fun gamma(src: Bitmap, redPart: Double, greenPart: Double, bluePart: Double): Bitmap
    {
        var red = redPart
        var green = greenPart
        var blue = bluePart
        Log.d("Gamma", String.format("R[%1\$s] G[%2\$s] B[%3\$s]", red, green, blue))

        red = (red + 2) / 10.0
        green = (green + 2) / 10.0
        blue = (blue + 2) / 10.0

        // convert image size
        val width = src.width
        val height = src.height

        val sourcePixels = IntArray(width * height)                    // Load all pixels
        src.getPixels(sourcePixels, 0, width, 0, 0, width, height)

        val destinationPixels = IntArray(width * height)

        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int
        // constant value curve
        val MAX_SIZE = 256
        val MAX_VALUE_DBL = 255.0
        val MAX_VALUE_INT = 255
        val REVERSE = 1.0

        // gamma arrays
        val gammaR = IntArray(MAX_SIZE)
        val gammaG = IntArray(MAX_SIZE)
        val gammaB = IntArray(MAX_SIZE)

        // setting values for every gamma channels
        for (i in 0..MAX_SIZE - 1)
        {
            gammaR[i] = Math.min(MAX_VALUE_INT, (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red) + 0.5).toInt())
            gammaG[i] = Math.min(MAX_VALUE_INT, (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green) + 0.5).toInt())
            gammaB[i] = Math.min(MAX_VALUE_INT, (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue) + 0.5).toInt())
        }

        // apply gamma table
        for (x in 0..width - 1)
        {
            for (y in 0..height - 1)
            {
                // convert pixel color
                pixel = sourcePixels[x + y * width]
                A = Color.alpha(pixel)
                // look up gamma
                R = gammaR[Color.red(pixel)]
                G = gammaG[Color.green(pixel)]
                B = gammaB[Color.blue(pixel)]

                // set new color to output bitmap
                destinationPixels[x + y * width] = Color.argb(A, R, G, B)
            }
        }

        // return final image
        return Bitmap.createBitmap(destinationPixels, width, height, src.config)
    }

    /**
     * @param src
     * @param value [-100, +100] -> Default = 0
     * @return
     */
    override fun contrast(src: Bitmap, value: Double): Bitmap
    {
        // image size
        val width = src.width
        val height = src.height

        val sourcePixels = IntArray(width * height)                    // Load all pixels
        src.getPixels(sourcePixels, 0, width, 0, 0, width, height)

        val destinationPixels = IntArray(width * height)

        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int
        // convert contrast value
        val contrast = Math.pow((100 + value) / 100, 2.0)

        // scan through all pixels
        for (x in 0..width - 1)
        {
            for (y in 0..height - 1)
            {

                // convert pixel color
                pixel = sourcePixels[x + y * width]
                A = Color.alpha(pixel)
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel)
                R = (((R / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (R < 0)
                {
                    R = 0
                } else if (R > 255)
                {
                    R = 255
                }

                G = Color.green(pixel)
                G = (((G / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (G < 0)
                {
                    G = 0
                } else if (G > 255)
                {
                    G = 255
                }

                B = Color.blue(pixel)
                B = (((B / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (B < 0)
                {
                    B = 0
                } else if (B > 255)
                {
                    B = 255
                }

                // set new pixel color to output bitmap
                destinationPixels[x + y * width] = Color.argb(A, R, G, B)
            }
        }

        return Bitmap.createBitmap(destinationPixels, width, height, src.config)
    }

    /**
     * @param src
     * @param value    [0, 200] -> Default = 100
     * @return
     */
    override fun saturation(src: Bitmap, value: Int): Bitmap
    {
        val f_value = (value / 100.0).toFloat()

        val w = src.width
        val h = src.height

        val bitmapResult = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvasResult = Canvas(bitmapResult)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(f_value)
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = filter
        canvasResult.drawBitmap(src, 0f, 0f, paint)

        return bitmapResult
    }

    /**  */
    override fun grayscale(src: Bitmap): Bitmap
    {
        val GrayArray = floatArrayOf(//Array to generate Gray-Scale image
            0.213f, 0.715f, 0.072f, 0.0f, 0.0f, 0.213f, 0.715f, 0.072f, 0.0f, 0.0f, 0.213f, 0.715f, 0.072f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)

        val colorMatrixGray = ColorMatrix(GrayArray)

        val w = src.width
        val h = src.height

        val bitmapResult = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvasResult = Canvas(bitmapResult)
        val paint = Paint()

        val filter = ColorMatrixColorFilter(colorMatrixGray)
        paint.colorFilter = filter
        canvasResult.drawBitmap(src, 0f, 0f, paint)

        return bitmapResult
    }

    /**  */
    override fun vignette(image: Bitmap): Bitmap
    {
        val width = image.width
        val height = image.height

        val radius = (width / 1.2).toFloat()
        val colors = intArrayOf(0, 0x55000000, 0xff000000.toInt())
        val positions = floatArrayOf(0.0f, 0.5f, 1.0f)

        val gradient = RadialGradient((width / 2).toFloat(), (height / 2).toFloat(), radius, colors, positions, Shader.TileMode.CLAMP)

        val canvas = Canvas(image)
        canvas.drawARGB(1, 0, 0, 0)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.shader = gradient

        val rect = Rect(0, 0, image.width, image.height)
        val rectF = RectF(rect)

        canvas.drawRect(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(image, rect, rect, paint)

        return image
    }

    /**
     * @param bitmap
     * @param hue [0, 360] -> Default = 0
     */
    override fun hue(bitmap: Bitmap, hue: Float): Bitmap
    {
        val width = bitmap.width
        val height = bitmap.height

        val pixels = IntArray(width * height)                    // Load all pixels
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val hsv = FloatArray(3)

        for (y in 0..height - 1)
        {
            for (x in 0..width - 1)
            {
                val pixel = pixels[x + y * width]
                Color.colorToHSV(pixel, hsv)
                hsv[0] = hue

                pixels[x + y * width] = Color.HSVToColor(Color.alpha(pixel), hsv)
            }
        }

        return Bitmap.createBitmap(pixels, width, height, bitmap.config)
    }

    /**  */
    override fun tint(src: Bitmap, color: Int): Bitmap
    {
        val width = src.width            // image size
        val height = src.height

        val bmOut = Bitmap.createBitmap(width, height, src.config)        // create a mutable empty bitmap

        val p = Paint(Color.RED)
        val filter = LightingColorFilter(color, 0)

        p.colorFilter = filter

        val c = Canvas()
        c.setBitmap(bmOut)
        c.drawBitmap(src, 0f, 0f, p)

        return bmOut
    }

    /**   */
    override fun invert(src: Bitmap): Bitmap
    {
        val height = src.height
        val width = src.width

        val pixels = IntArray(width * height)                    // Load all pixels
        src.getPixels(pixels, 0, width, 0, 0, width, height)

        for (y in 0..height - 1)
        {
            for (x in 0..width - 1)
            {
                val pixelColor = pixels[x + y * width]

                val A = Color.alpha(pixelColor)
                val R = 255 - Color.red(pixelColor)
                val G = 255 - Color.green(pixelColor)
                val B = 255 - Color.blue(pixelColor)

                pixels[x + y * width] = Color.argb(A, R, G, B)
            }
        }

        return Bitmap.createBitmap(pixels, width, height, src.config)
    }

    /**
     * @param src
     * @param type
     * @param percentValue [0, 150], type = (1, 2, 3) => (R, G, B)
     * @return
     */
    override fun boost(src: Bitmap, type: Int, percentValue: Float): Bitmap
    {
        var percent = percentValue
        percent /= 100

        val width = src.width
        val height = src.height

        val pixels = IntArray(width * height)                    // Load all pixels
        src.getPixels(pixels, 0, width, 0, 0, width, height)

        for (x in 0..width - 1)
        {
            for (y in 0..height - 1)
            {
                val pixel = pixels[x + y * width]

                val A = Color.alpha(pixel)
                var R = Color.red(pixel)
                var G = Color.green(pixel)
                var B = Color.blue(pixel)
                if (type == 1)
                {
                    R = (R * (1 + percent)).toInt()
                    if (R > 255) R = 255
                } else if (type == 2)
                {
                    G = (G * (1 + percent)).toInt()
                    if (G > 255) G = 255
                } else if (type == 3)
                {
                    B = (B * (1 + percent)).toInt()
                    if (B > 255) B = 255
                }

                pixels[x + y * width] = Color.argb(A, R, G, B)
            }
        }

        return Bitmap.createBitmap(pixels, width, height, src.config)
    }

    /** */
    override fun sketch(src: Bitmap): Bitmap
    {
        val type = 6
        val threshold = 130

        val width = src.width
        val height = src.height

        val sourcePixels = IntArray(width * height)                    // Load all pixels
        src.getPixels(sourcePixels, 0, width, 0, 0, width, height)

        val destinationPixels = IntArray(width * height)

        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var sumR: Int
        var sumG: Int
        var sumB: Int
        val pixels = Array(3) { IntArray(3) }
        for (y in 0..height - 2 - 1)
        {
            for (x in 0..width - 2 - 1)
            {
                //      convert pixel matrix
                for (i in 0..2)
                {
                    for (j in 0..2)
                    {
                        pixels[i][j] = sourcePixels[x + i + (y + j) * width]
                    }
                }
                // convert alpha of center pixel
                A = Color.alpha(pixels[1][1])
                // init color sum
                sumR = type * Color.red(pixels[1][1]) - Color.red(pixels[0][0]) - Color.red(pixels[0][2]) - Color.red(pixels[2][0]) - Color.red(pixels[2][2])
                sumG = type * Color.green(pixels[1][1]) - Color.green(pixels[0][0]) - Color.green(pixels[0][2]) - Color.green(pixels[2][0]) - Color.green(pixels[2][2])
                sumB = type * Color.blue(pixels[1][1]) - Color.blue(pixels[0][0]) - Color.blue(pixels[0][2]) - Color.blue(pixels[2][0]) - Color.blue(pixels[2][2])
                // convert final Red
                R = sumR + threshold
                if (R < 0)
                {
                    R = 0
                } else if (R > 255)
                {
                    R = 255
                }
                // convert final Green
                G = sumG + threshold
                if (G < 0)
                {
                    G = 0
                } else if (G > 255)
                {
                    G = 255
                }
                // convert final Blue
                B = sumB + threshold
                if (B < 0)
                {
                    B = 0
                } else if (B > 255)
                {
                    B = 255
                }

                destinationPixels[x + 1 + (y + 1) * width] = Color.argb(A, R, G, B)
            }
        }

        return Bitmap.createBitmap(destinationPixels, width, height, src.config)
    }
}