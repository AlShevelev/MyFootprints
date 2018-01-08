package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.bitmaps

import android.graphics.Bitmap
import java.io.Closeable

/** */
interface IBitmapsRepository  : Closeable
{
    /** Return last processed bitmap
     * @param isForRead - if true original bitmap was returned; otherwise - copy for edit * */
    fun getLast(isForRead: Boolean) : Bitmap

    /** Add new bitmap to repository */
    fun add(bitmap : Bitmap)

    /** Remove last bitmap from Undo list */
    fun removeLast()

    /** True if Undo buffer hast data */
    fun hasUndo() : Boolean
}