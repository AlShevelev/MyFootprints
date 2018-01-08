package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync

import com.google.android.gms.drive.DriveId

import java.util.Date

/** One external log record  */
data class ExternalLogRecord (
    val driveId: DriveId,
    val createDate: Date,
    val fileSize: Int)            // in bytes
