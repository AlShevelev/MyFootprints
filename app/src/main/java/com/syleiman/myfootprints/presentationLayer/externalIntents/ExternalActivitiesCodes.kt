package com.syleiman.myfootprints.presentationLayer.externalIntents

enum class ExternalActivitiesCodes(var value : Int)
{
    CapturePhoto(0),
    GetPhotoFromGallery(1),
    GoogleDriveAuth(2),
    PermissionRequest(3);

    companion object Create
    {
        fun from(findValue: Int): ExternalActivitiesCodes? = ExternalActivitiesCodes.values().firstOrNull { it.value == findValue }
    }
}
