package com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.result

import android.os.Parcel
import android.os.Parcelable

import com.google.android.gms.maps.model.LatLng

/** Updated footprint info  */
class FootprintsGalleryActivityUpdateInfo : Parcelable
{
    var footprintId: Long = 0

    var photoFileName: String

    var location: LatLng

    var description: String

    constructor(footprintId: Long, photoFileName: String, location: LatLng, description: String)
    {
        this.footprintId = footprintId
        this.photoFileName = photoFileName
        this.location = location
        this.description = description
    }

    protected constructor(`in`: Parcel)
    {
        footprintId = `in`.readLong()
        photoFileName = `in`.readString()
        location = `in`.readParcelable<LatLng>(LatLng::class.java.classLoader)
        description = `in`.readString()
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's marshalled representation.
     * @return a bitmask indicating the set of special object types marshalled by the Parcelable.
     */
    override fun describeContents(): Int
    {
        return 0
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or [.PARCELABLE_WRITE_RETURN_VALUE].
     */
    override fun writeToParcel(dest: Parcel, flags: Int)
    {
        dest.writeLong(footprintId)
        dest.writeString(photoFileName)
        dest.writeParcelable(location, flags)
        dest.writeString(description)
    }

    companion object
    {
        @JvmField
        val CREATOR = object : Parcelable.Creator<FootprintsGalleryActivityUpdateInfo>
        {
            override fun createFromParcel(`in`: Parcel): FootprintsGalleryActivityUpdateInfo = FootprintsGalleryActivityUpdateInfo(`in`)

            override fun newArray(size: Int): Array<FootprintsGalleryActivityUpdateInfo?> = arrayOfNulls(size)
        }
    }
}