package com.syleiman.myfootprints.modelLayer.dto

import android.os.Parcel
import android.os.Parcelable

import com.google.android.gms.maps.model.LatLng

import java.util.Date

/** Footprints data for gallery  */
data class FootprintForGalleryDto(
    var footprintId: Long,
    var photoFileName: String,
    var comment: String,
    var location: LatLng,
    var countryName: String?,
    var cityName: String?,
    var mapSnapshotFileName: String?,
    var createDateTime: Date) : Parcelable
{

    /**  */
    protected constructor(parcelData: Parcel) : this(
        parcelData.readLong(),
        parcelData.readString(),
        parcelData.readString(),
        parcelData.readParcelable<LatLng>(LatLng::class.java.classLoader),
        parcelData.readString(),
        parcelData.readString(),
        parcelData.readString(),
        Date(parcelData.readLong())
    )

    companion object
    {
        @JvmField
        val CREATOR = object : Parcelable.Creator<FootprintForGalleryDto>
        {
            override fun createFromParcel(`in`: Parcel): FootprintForGalleryDto = FootprintForGalleryDto(`in`)

            override fun newArray(size: Int): Array<FootprintForGalleryDto?> = arrayOfNulls(size)
        }
    }


    /**  */
    override fun describeContents(): Int = 0

    /**  */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(footprintId)
        dest.writeString(photoFileName)
        dest.writeString(comment)
        dest.writeParcelable(location, flags)
        dest.writeString(countryName)
        dest.writeString(cityName)
        dest.writeString(mapSnapshotFileName)
        dest.writeLong(createDateTime.time)
    }
}