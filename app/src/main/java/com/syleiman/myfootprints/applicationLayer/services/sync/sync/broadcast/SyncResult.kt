package com.syleiman.myfootprints.applicationLayer.services.sync.sync.broadcast

import android.os.Parcel
import android.os.Parcelable

/** Sync result  */
class SyncResult : Parcelable
{
    /** Some footprints was updated  */
    var footprintsWasChanged: Boolean = false

    /**  */
    constructor()
    {
        footprintsWasChanged = false
    }

    /**  */
    protected constructor(`in`: Parcel)
    {
        footprintsWasChanged = `in`.readByte().toInt() != 0
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
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
     * *
     * @param flags Additional flags about how the object should be written.
     * *              May be 0 or [.PARCELABLE_WRITE_RETURN_VALUE].
     */
    override fun writeToParcel(dest: Parcel, flags: Int)
    {
        dest.writeByte((if (footprintsWasChanged) 1 else 0).toByte())
    }

    companion object
    {
        @JvmField
        val CREATOR = object : Parcelable.Creator<SyncResult>
        {
            override fun createFromParcel(`in`: Parcel): SyncResult = SyncResult(`in`)

            override fun newArray(size: Int): Array<SyncResult?> = arrayOfNulls(size)
        }

        /** Merge two results in one  */
        fun Merge(result1: SyncResult, result2: SyncResult): SyncResult
        {
            val mergeResult = SyncResult()

            mergeResult.footprintsWasChanged = result1.footprintsWasChanged or result2.footprintsWasChanged

            return mergeResult
        }
    }
}