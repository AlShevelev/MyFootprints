package com.syleiman.myfootprints.businessLayer.bitmapService

import android.graphics.Bitmap
import android.graphics.Color

class ConvolutionMatrix(size: Int)
{
    var Matrix: Array<DoubleArray> = Array(size) { DoubleArray(size) }
    var Factor = 1.0
    var Offset = 1.0

    fun applyConfig(config: Array<DoubleArray>)
    {
        for (x in 0..SIZE - 1)
            for (y in 0..SIZE - 1)
                Matrix[x][y] = config[x][y]
    }

    companion object
    {
        val SIZE = 3

        fun computeConvolution3x3(src: Bitmap, matrix: ConvolutionMatrix): Bitmap
        {
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
            val pixels = Array(SIZE) { IntArray(SIZE) }

            for (y in 0..height - 2 - 1)
            {
                for (x in 0..width - 2 - 1)
                {

                    // convert pixel matrix
                    for (i in 0..SIZE - 1)
                        for (j in 0..SIZE - 1)
                            pixels[i][j] = sourcePixels[x + i + width * (y + j)]

                    // convert alpha of center pixel
                    A = Color.alpha(pixels[1][1])

                    // init color sum
                    sumB = 0
                    sumG = sumB
                    sumR = sumG

                    // convert sum of RGB on matrix
                    for (i in 0..SIZE - 1)
                        for (j in 0..SIZE - 1)
                        {
                            sumR += (Color.red(pixels[i][j]) * matrix.Matrix[i][j]).toInt()
                            sumG += (Color.green(pixels[i][j]) * matrix.Matrix[i][j]).toInt()
                            sumB += (Color.blue(pixels[i][j]) * matrix.Matrix[i][j]).toInt()
                        }

                    // convert final Red
                    R = (sumR / matrix.Factor + matrix.Offset).toInt()
                    if (R < 0)
                        R = 0
                    else if (R > 255)
                        R = 255

                    // convert final Green
                    G = (sumG / matrix.Factor + matrix.Offset).toInt()
                    if (G < 0)
                        G = 0
                    else if (G > 255)
                        G = 255

                    // convert final Blue
                    B = (sumB / matrix.Factor + matrix.Offset).toInt()
                    if (B < 0)
                        B = 0
                    else if (B > 255)
                        B = 255

                    // apply new pixel
                    destinationPixels[x + 1 + width * (y + 1)] = Color.argb(A, R, G, B)
                }
            }

            // final image
            return Bitmap.createBitmap(destinationPixels, width, height, src.config)
        }
    }
}