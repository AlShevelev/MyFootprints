package com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.result

import android.os.Parcel
import android.os.Parcelable

/** Result of FootprintsGalleryActivity  */
open class FootprintsGalleryActivityResult : Parcelable
{
    var deletedFootprintsIds: LongArray
    var updatedFootprints: List<FootprintsGalleryActivityUpdateInfo>

    constructor(deletedFootprintsIds: LongArray, updatedFootprints: List<FootprintsGalleryActivityUpdateInfo>)
    {
        this.deletedFootprintsIds = deletedFootprintsIds
        this.updatedFootprints = updatedFootprints
    }

    protected constructor(`in`: Parcel)
    {
        deletedFootprintsIds = `in`.createLongArray()
        updatedFootprints = `in`.createTypedArray(FootprintsGalleryActivityUpdateInfo.CREATOR).map{it}
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's marshalled representation.
     * @return a bitmask indicating the set of special object types marshalled
     * * by the Parcelable.
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
        dest.writeLongArray(deletedFootprintsIds)
        dest.writeTypedArray(updatedFootprints.toTypedArray(), flags)
    }

    /**  */
    fun hasData(): Boolean
    {
        return deletedFootprintsIds.isNotEmpty() || updatedFootprints.isNotEmpty()
    }

    companion object
    {
        @JvmField
        val CREATOR = object : Parcelable.Creator<FootprintsGalleryActivityResult>
        {
            override fun createFromParcel(`in`: Parcel): FootprintsGalleryActivityResult = FootprintsGalleryActivityResult(`in`)

            override fun newArray(size: Int): Array<FootprintsGalleryActivityResult?> = arrayOfNulls(size)
        }
    }
}