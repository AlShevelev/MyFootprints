package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.bitmaps

import android.graphics.Bitmap
import java.util.*
import javax.inject.Inject

/** Repository of bitmaps  */
class BitmapsRepository
@Inject
constructor(private val io : IBitmapsIO) : IBitmapsRepository
{
    /** Source bitmap */
    lateinit var source : Bitmap

    /** Files in Undo stack */
    private val undoFiles : Stack<String> = Stack()

    /** Last placed bitmap */
    private var lastBitmap : Bitmap? = null

    /** Return last processed bitmap
     * @param isForRead - if true original bitmap was returned; otherwise - copy for edit * */
    override fun getLast(isForRead: Boolean) : Bitmap
    {
        val result = if(undoFiles.empty()) source else lastBitmap!!
        return if(isForRead) result else copy(result)
    }

    /** Add new bitmap to repository */
    override fun add(bitmap : Bitmap)
    {
        lastBitmap = bitmap
        undoFiles.push(io.save(bitmap).getName())
    }

    /** Copy bitmap for edit */
    private fun copy(bitmap : Bitmap) : Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    /** Remove last bitmap from Undo list */
    override fun removeLast()
    {
        if(undoFiles.empty())
            return

        io.delete(undoFiles.pop())

        if(!undoFiles.empty())
            lastBitmap = io.load(undoFiles.peek())
        else
            lastBitmap = null
    }

    /** True if Undo buffer hast data */
    override fun hasUndo() : Boolean = !undoFiles.empty()

    /** Remove all bitmaps - need to clear in the end of work */
    override fun close()
    {
        lastBitmap = null
        undoFiles.iterator().forEach { io.delete(it) }          // Remove all files
    }
}

