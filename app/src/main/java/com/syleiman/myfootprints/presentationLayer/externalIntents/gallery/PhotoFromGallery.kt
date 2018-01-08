package com.syleiman.myfootprints.presentationLayer.externalIntents.gallery

import android.content.Intent
import android.provider.MediaStore
import android.support.v4.app.Fragment
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.presentationLayer.externalIntents.ExternalActivitiesCodes
import javax.inject.Inject

/**
 * Process getting photo from gallery
 */
class PhotoFromGallery
@Inject
constructor(private val context: Fragment)
{
    /**
     * Start photo getting process
     * @return true - success
     */
    fun startGetPhoto(): Boolean
    {
        try
        {
            val captureImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            context.startActivityForResult(captureImage, ExternalActivitiesCodes.GetPhotoFromGallery.value)
            return true
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            return false
        }
    }

    /**
     * Decode file url from response
     * @return Url of file or null in case of error
     */
    fun decodeFileUrl(data: Intent): String
    {
        val selectedImage = data.data
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        App.context.contentResolver.query(selectedImage, filePathColumn, null, null, null)!!.use { cursor ->
            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val fileName = cursor.getString(columnIndex)

            return FileSingle.fromPath(fileName).getUrl()
        }
    }
}
