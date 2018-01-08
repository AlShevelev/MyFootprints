package com.syleiman.myfootprints.businessLayer.optionsCacheService

import java.util.Date

interface IOptionsCacheService
{
    /**
     * Get file name of last photo
     * @return File name of last photo or null if no photos yet
     */
    fun getLastPhotoFileName(): String?

    /**
     * Get file name of last photo
     * @return File name of last photo or null if no photos yet
     */
    fun setLastPhotoFileName(value: String)

    /** Get last footprint id  */
    fun getLastFootprintId(): Long?

    /** Set last footprint id  */
    fun setLastFootprintId(id: Long?)

    /** Get total footprints  */
    fun getTotalFootprints(): Int?

    /** Update total footprints value  */
    fun setTotalFootprints(value: Int)

    /** Get "Send data to social networks only via WiFi" option  */
    fun getSyncOnlyViaWifiOptions(): Boolean

    /** Update "Send data to social networks only via WiFi" option  */
    fun setSyncOnlyViaWifiOptions(isTurnOn: Boolean)

    /** Get creation moment of last processed external sync file  */
    fun getExternalLastSyncDate(): Date?

    /** Set creation moment of last processed external sync file  */
    fun setExternalLastSyncDate(externalLastSyncDate: Date)

    /** Get unique if of app installiation  */
    fun getInstallId(): String?

    /** Set unique if of app installiation  */
    fun setInstallId(value: String)
}
