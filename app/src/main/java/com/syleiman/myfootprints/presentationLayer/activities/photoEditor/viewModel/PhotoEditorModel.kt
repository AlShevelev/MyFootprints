package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel

import android.graphics.Bitmap
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.bitmaps.IBitmapsIO
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.bitmaps.BitmapsRepository
import javax.inject.Inject

/** Model for PhotoEditorActivity */
class PhotoEditorModel
@Inject
constructor(private val bitmapRepository : BitmapsRepository, private val io : IBitmapsIO) : IPhotoEditorModel
{
    /** */
    override fun setSource(bitmap : Bitmap) { bitmapRepository.source = bitmap }

    /** */
    override fun addBitmap(bitmap : Bitmap) = bitmapRepository.add(bitmap)

    /** */
    override fun hasUndo() : Boolean = bitmapRepository.hasUndo()

    /** Remove last bitmap and get prior */
    override fun removeAndGetLast() : Bitmap
    {
        bitmapRepository.removeLast()
        return bitmapRepository.getLast(true)
    }

    /** Return last bitmap from repository
     * @param isForRead - if true original bitmap was returned; otherwise - copy for edit */
    override fun getLast(isForRead: Boolean) : Bitmap = bitmapRepository.getLast(isForRead)

    /** Save bitmap to disk and get path */
    override fun saveAndGetPath(bitmap: Bitmap) : String = io.save(bitmap, false).getPath()

    /** Clear undo buffer */
    override fun clear() = bitmapRepository.close()
}