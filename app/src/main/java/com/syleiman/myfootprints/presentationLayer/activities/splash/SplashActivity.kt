package com.syleiman.myfootprints.presentationLayer.activities.splash

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.presentationLayer.ToastsParams
import com.syleiman.myfootprints.presentationLayer.activities.ActivityBase
import com.syleiman.myfootprints.presentationLayer.activities.main.InActivityGoogleDriveAuth
import com.syleiman.myfootprints.presentationLayer.activities.main.MainActivity
import com.syleiman.myfootprints.presentationLayer.externalIntents.ExternalActivitiesCodes
import permissions.dispatcher.*

/** Splash screen  */
@RuntimePermissions
class SplashActivity : ActivityBase()
{
    private var inActivityGoogleDriveAuth: InActivityGoogleDriveAuth? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        SplashActivityPermissionsDispatcher.checkReadExtStoreWithCheck(this)            // Request first permission  // !!!
    }

    /**  */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        when (ExternalActivitiesCodes.from(requestCode))
        {
            ExternalActivitiesCodes.GoogleDriveAuth ->
            {
                Log.d(LogTags.GD_AUTH, "Auth activity completed. Result is: " + resultCode)
                inActivityGoogleDriveAuth!!.onAuthCompleted(resultCode == Activity.RESULT_OK)
            }
            ExternalActivitiesCodes.PermissionRequest -> SplashActivityPermissionsDispatcher.checkReadExtStoreWithCheck(this)   // Let's try again
            else -> throw UnsupportedOperationException("This code is not supported: "+requestCode)
        }
    }

    /**  */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults)        // Pass result to custom dispatcher       // !!!
    }

    /** Check READ_EXTERNAL_STORAGE permission
     * This method called only we have permission (from manifest for Android < 6 or from request for Android >=6)  */
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun checkReadExtStore()
    {
        SplashActivityPermissionsDispatcher.checkAccessCourseLocationWithCheck(this)            // Request next permission   // !!!
    }

    /** Check ACCESS_COARSE_LOCATION permission
     * This method called only we have permission (from manifest for Android < 6 or from request for Android >=6) */
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun checkAccessCourseLocation()
    {
        resetNeverAskAgain()           // Reset NeverAskAgain flag

        inActivityGoogleDriveAuth = InActivityGoogleDriveAuth(this) { isConnected ->            // Try auth on GD
            Log.d(LogTags.GD_AUTH, "isConnected: " + isConnected)
            if (isConnected)
                App.startSync()                // If auth success - start sync process

            val intent = Intent(this@SplashActivity, MainActivity::class.java)     // Start main activity
            this@SplashActivity.startActivity(intent)

            this@SplashActivity.finish()
        }
        inActivityGoogleDriveAuth!!.startAuth()      // Start auth on GoogleDrive
    }

    /** Show dialog for READ_EXTERNAL_STORAGE permission explanation  */
    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showRationaleForReadExtStorage(request: PermissionRequest)
    {
        showOkDialog(R.string.splash_read_ext_storage_explanation) {
            request.proceed()
        }
    }

    /** Show dialog for ACCESS_COURSE_LOCATION permission explanation  */
    @OnShowRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun showRationaleForAccessCourseLocation(request: PermissionRequest)
    {
        showOkDialog(R.string.splash_coarse_location_explanation) {
            request.proceed()
        }
    }

    /** READ_EXTERNAL_STORAGE denied  */
    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onRationaleForReadExtStorageDenied()
    {
        showMessage(R.string.splash_read_ext_storage_denied, ToastsParams.Duration.Long)
        finish()
    }

    /** ACCESS_COARSE_LOCATION denied  */
    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onAccessCourseLocationDenied()
    {
        showMessage(R.string.splash_coarse_location_denied, ToastsParams.Duration.Long)
        finish()
    }

    /** READ_EXTERNAL_STORAGE never ask again  */
    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onRationaleForReadExtStorageNeverAskAgain()
    {
        processNeverAskAgain(R.string.splash_read_ext_storage_denied)
    }

    /** ACCESS_COARSE_LOCATION never ask again  */
    @OnNeverAskAgain(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onAccessCourseLocationNeverAskAgain()
    {
        processNeverAskAgain(R.string.splash_coarse_location_denied)
    }

    /** Actions when user set flag NeverAskAgain  */
    private fun processNeverAskAgain(textResourceId: Int)
    {
        if (setNeverAskAgain())
        // second time
        {
            showQueryDialog(R.string.splash_go_to_options, // Confirm
            {
                this@SplashActivity.openApplicationSettings()
            },
            {
                this@SplashActivity.finish()    // Reject
            })
        }
        else
        {
            showMessage(textResourceId, ToastsParams.Duration.Long)            // first time
            finish()
        }
    }

    /** Memorize NeverAskAgain flag and return its old value  */
    private fun setNeverAskAgain(): Boolean
    {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)

        val result = sharedPreferences.contains(SHARED_PREFERENCES_NEVER_ASK)
        if (!result)
            sharedPreferences.edit().putBoolean(SHARED_PREFERENCES_NEVER_ASK, true).apply()

        return result
    }

    /** Reset NeverAskAgain flag  */
    private fun resetNeverAskAgain()
    {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE)
        if (!sharedPreferences.contains(SHARED_PREFERENCES_NEVER_ASK))
            return

        sharedPreferences.edit().remove(SHARED_PREFERENCES_NEVER_ASK).apply()
    }

    /** Open app settings  */
    fun openApplicationSettings()
    {
        val appSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + packageName))
        startActivityForResult(appSettingsIntent, ExternalActivitiesCodes.PermissionRequest.value)
    }

    companion object
    {

        private val SHARED_PREFERENCES_FILE = "permissions"
        private val SHARED_PREFERENCES_NEVER_ASK = "permissions_neverAsk"
    }
}