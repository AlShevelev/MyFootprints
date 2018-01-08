package com.syleiman.myfootprints.presentationLayer.activities.main

import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.presentationLayer.activities.IActivityBaseCallback
import com.syleiman.myfootprints.presentationLayer.externalIntents.ExternalActivitiesCodes

/** Authentication in activity  */
class InActivityGoogleDriveAuth(
        private val context: Activity,
        private val completeCallback: Function1<Boolean, Unit>)             // true - connected successfully
            : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private var googleApiClient: GoogleApiClient? = null
    private var hasAttempts = false

    /**  */
    fun startAuth()
    {
        googleApiClient = GoogleApiClient.Builder(context)
            .addApi(Drive.API)
            .addScope(Drive.SCOPE_FILE)
            .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        Log.d(LogTags.GD_AUTH, "Start connect")
        googleApiClient!!.connect()
    }

    /**  */
    override fun onConnected(bundle: Bundle?)
    {
        Log.d(LogTags.GD_AUTH, "OnConnected")
        googleApiClient!!.disconnect()
        complete(true)
    }

    /**  */
    override fun onConnectionSuspended(i: Int)
    {
        Log.d(LogTags.GD_AUTH, "onConnectionSuspended")
        complete(false)
    }

    /**  */
    override fun onConnectionFailed(connectionResult: ConnectionResult)
    {
        val errorCode = connectionResult.errorCode

        Log.d(LogTags.GD_AUTH, "onConnectionFailed. Error code is: " + connectionResult.errorCode)
        if (errorCode == ConnectionResult.NETWORK_ERROR)
        {
            Log.d(LogTags.GD_AUTH, "onConnectionFailed. Network error")
            complete(false)
            return
        }

        if (!connectionResult.hasResolution())
        {
            Log.d(LogTags.GD_AUTH, "onConnectionFailed. Without resolution")
            GoogleApiAvailability.getInstance().getErrorDialog(context, errorCode, 0) // show the localized error dialog.
            {
                this.complete(false)
            }.show()
            return
        }

        if (hasAttempts)        // Only one attempt
        {
            Log.d(LogTags.GD_AUTH, "onConnectionFailed. No attempts")
            complete(false)
            return
        }

        if (errorCode == ConnectionResult.SIGN_IN_REQUIRED)
        {
            Log.d(LogTags.GD_AUTH, "onConnectionFailed. SignIn required")
            hasAttempts = true
            (context as IActivityBaseCallback).showOkDialog(R.string.google_drive_auth_dialog) // Show dialog why we need to use GD
            {
                try         //FullScreenProgress.Show(context);
                {
                    val activityCode = ExternalActivitiesCodes.GoogleDriveAuth.value
                    Log.d(LogTags.GD_AUTH, "onConnectionFailed. Start resolution")
                    connectionResult.startResolutionForResult(context, activityCode)
                }
                catch (e: IntentSender.SendIntentException)
                {
                    e.printStackTrace()
                    this.complete(false)
                }
            }
        }
        else
            complete(false)
    }

    /** When auth completed  */
    fun onAuthCompleted(success: Boolean) = complete(success)

    /**  */
    private fun complete(isConnected: Boolean) =  completeCallback.invoke(isConnected)         // Complete
}