package com.syleiman.myfootprints.presentationLayer.externalIntents.send

import android.app.Activity
import android.content.Intent
import android.support.v4.content.FileProvider
import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.presentationLayer.StringsHelper

object FootprintSender
{
    /**
     * Start send to external program
     * @return true - success
     */
    fun startSend(context: Activity, comment: String, photoFileName: String, location: LatLng): Boolean
    {
        try
        {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "*/*"

            val googleMapUrl = StringsHelper.GetGoogleMapUrl(location, 18)
            val innerComment = comment+"\r\n" + googleMapUrl       // Add location
            shareIntent.putExtra(Intent.EXTRA_TEXT, innerComment)           // Put text

            val file = FileSingle.withName(photoFileName).inPrivate().getFile()           // Put photo file
            val sharedFileUri = FileProvider.getUriForFile(context, App.getStringRes(R.string.files_authority), file)
            shareIntent.putExtra(Intent.EXTRA_STREAM, sharedFileUri)

            context.startActivity(Intent.createChooser(shareIntent, App.getStringRes(R.string.send_choose_window_header)))

            return true
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            return false
        }
    }
}
