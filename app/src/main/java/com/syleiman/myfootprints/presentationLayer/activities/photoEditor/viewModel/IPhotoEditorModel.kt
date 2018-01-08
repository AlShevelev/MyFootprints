package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel

import android.graphics.Bitmap

/** */
interface IPhotoEditorModel
{
    /** */
    fun setSource(bitmap : Bitmap)

    /** */
    fun addBitmap(bitmap : Bitmap)

    /** */
    fun hasUndo() : Boolean

    /** Remove last bitmap and get prior */
    fun removeAndGetLast() : Bitmap

    /** Return last bitmap from repository
     * @param isForRead - if true original bitmap was returned; otherwise - copy for edit */
    fun getLast(isForRead: Boolean) : Bitmap

    /** Save bitmap to disk and get path */
    fun saveAndGetPath(bitmap: Bitmap) : String

    /** Clear undo buffer */
    fun clear()
}