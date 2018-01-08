package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync

import com.google.android.gms.common.api.GoogleApiClient

/** Context's interface for external processors  */
interface ISyncContextProcessing
{
    /** Get API client  */
    fun getClient(): GoogleApiClient

    /** Check connection status  */
    fun isConnected(): Boolean

    /** Close API connection  */
    fun disconnect()

    /** Get files for other devices  */
    fun getExternalFiles(): List<ExternalLogRecord>

    /** Get files for other devices  */
    fun setExternalFiles(externalFiles : List<ExternalLogRecord>)

    /** Get Ids of log records  */
    fun getLogRecords(): List<Long>

    /** Set Ids of log records  */
    fun setLogRecords(logRecords : List<Long>)
}