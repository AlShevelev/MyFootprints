package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync

import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.drive.Drive
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.common.LogTags

/** Context for sync  */
/**  */
class SyncContext : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ISyncContextProcessing
{
    // Ids of log records to process, SORTED BY GROWTH
    private lateinit var logRecords: List<Long>

    private var syncCallbacks: ISyncSmProcess? = null

    private lateinit var googleApiClient: GoogleApiClient

    private var isConnected = false

    private var isInit = true

    private val locker = Any()

    /** Files from other devices  */
    private lateinit var externalFiles: List<ExternalLogRecord>

    override fun getExternalFiles(): List<ExternalLogRecord> = externalFiles

    override fun setExternalFiles(externalFiles: List<ExternalLogRecord>) { this.externalFiles = externalFiles }

    override fun getLogRecords(): List<Long> = logRecords

    override fun setLogRecords(logRecords: List<Long>) { this.logRecords = logRecords }

    fun attachCallbacks(syncCallbacks: ISyncSmProcess) { this.syncCallbacks = syncCallbacks }

    /** Try to connect  */
    fun connect()
    {
        Log.d(LogTags.SYNC_PROCESS, "Connect to GD...")
        googleApiClient = GoogleApiClient.Builder(App.context)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
        googleApiClient.connect()
    }

    /**  */
    override fun onConnected(bundle: Bundle?)
    {
        Log.d(LogTags.SYNC_PROCESS, "GD Connected")
        synchronized(locker) {
            isConnected = true

            if (isInit)
            {
                isInit = false
                syncCallbacks!!.process(Events.Connected)
            }
        }
    }

    /** Connection temporary unavailable  */
    override fun onConnectionSuspended(i: Int)
    {
        Log.d(LogTags.SYNC_PROCESS, "GD Connection suspended")
        synchronized(locker) {
            isConnected = false
        }
    }

    /**  */
    override fun onConnectionFailed(connectionResult: ConnectionResult)
    {
        synchronized(locker) {
            isConnected = false

            if (isInit)
            {
                isInit = false
                Log.w(LogTags.SYNC_PROCESS, "[SyncContext] GD Connection failed. Error is: " + connectionResult.errorCode + "; Error message: " + connectionResult.errorMessage)
                syncCallbacks!!.process(Events.Error)
            }
        }
    }

    /** Get API client  */
    override fun getClient(): GoogleApiClient
    {
        return googleApiClient
    }

    /** Check connection status  */
    override fun isConnected(): Boolean
    {
        synchronized(locker) {
            return isConnected
        }
    }

    /** Close API connection  */
    override fun disconnect()
    {
        googleApiClient.disconnect()
    }
}