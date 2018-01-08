package com.syleiman.myfootprints.presentationLayer.externalIntents.camera

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R

import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.presentationLayer.externalIntents.ExternalActivitiesCodes

import java.io.File

import javax.inject.Inject

/**
 * Init photo capture by camera
 */
class PhotoCapture
@Inject
constructor(private val context: Fragment)
{
    private var currentCapturedPhoto: File? = null

    /**
     * Start photo capturing process
     * @return true - success
     */
    fun startCapturePhoto(): Boolean
    {
        try
        {
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val captureFile = FileSingle.withRandomName(FileSingle.TEMP_FILES_PREFIX, "jpg").inShared().getFile()
            currentCapturedPhoto = captureFile
            val fileUri : Uri

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                fileUri = Uri.fromFile(captureFile)
            else                                                    // Starting from API 24 we need to use FileProvider
                fileUri = FileProvider.getUriForFile(App.context, App.getStringRes(R.string.files_authority), currentCapturedPhoto)

            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            context.startActivityForResult(captureImage, ExternalActivitiesCodes.CapturePhoto.value)
            return true
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            return false
        }
    }

    /** Get captured file url */
    val capturedFileUrl: String?
        get()
        {
            if (currentCapturedPhoto!!.exists())
                return Uri.fromFile(currentCapturedPhoto).toString()
            return null
        }
}
