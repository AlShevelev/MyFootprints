package com.syleiman.myfootprints.businessLayer.optionsCacheService

import com.syleiman.myfootprints.common.iif
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import java.sql.SQLException
import java.util.*
import javax.inject.Inject

class OptionsCacheService
@Inject
constructor(private val localDbService: ILocalDbService) : IOptionsCacheService
{
    companion object
    {
        private val True = "true"
        private val False = "false"
    }

    /** Convert key from enum to int value  */
    private fun convertKey(key: OptionsKeys): Int = key.value

    /** Get boolean options  */
    private fun getBooleanOption(key: OptionsKeys, defaultValue: Boolean): Boolean
    {
        try
        {
            val value = localDbService.keyValueStorage().getValue(convertKey(key)) ?: return defaultValue
            return value == True
        }
        catch (ignored: SQLException)
        {
            return defaultValue
        }
    }

    /** Get boolean options  */
    private fun setBooleanOption(key: OptionsKeys, value: Boolean)
    {
        try
        {
            localDbService.keyValueStorage().setValue(convertKey(key), value.iif ({True}, {False}))
        }
        catch (ignored: SQLException)
        {
        }
    }

    /** Get string options  */
    private fun getStringOption(key: OptionsKeys): String?
    {
        try
        {
            return localDbService.keyValueStorage().getValue(convertKey(key))
        }
        catch (ignored: SQLException)
        {
            return null
        }
    }

    /** Get string options  */
    private fun setStringOption(key: OptionsKeys, value: String)
    {
        try
        {
            localDbService.keyValueStorage().setValue(convertKey(key), value)
        }
        catch (ignored: SQLException)
        {
        }
    }

    /**  */
    private fun getLongValue(key: OptionsKeys): Long?
    {
        try
        {
            val rawValue = localDbService.keyValueStorage().getValue(convertKey(key)) ?: return null
            return java.lang.Long.parseLong(rawValue)
        }
        catch (ignored: SQLException)
        {
            return null
        }
    }

    /**  */
    private fun setLongValue(key: OptionsKeys, value: Long?)
    {
        try
        {
            localDbService.keyValueStorage().setValue(convertKey(key), value!!.toString())
        }
        catch (ignored: SQLException)
        {
        }
    }

    /**  */
    private fun getIntValue(key: OptionsKeys): Int?
    {
        try
        {
            val rawValue = localDbService.keyValueStorage().getValue(convertKey(key)) ?: return null
            return Integer.parseInt(rawValue)
        } catch (ignored: SQLException)
        {
        }

        return null
    }

    /**  */
    private fun setIntValue(key: OptionsKeys, value: Int?)
    {
        try
        {
            localDbService.keyValueStorage().setValue(convertKey(key), value!!.toString())
        } catch (ignored: SQLException)
        {
        }

    }

    /**  */
    private fun getDateValue(key: OptionsKeys): Date? = getLongValue(key)?.let(::Date)

    /**  */
    private fun setDateValue(key: OptionsKeys, value: Date) = setLongValue(key, value.time)

    /**
     * Get file name of last photo
     * @return File name of last photo or null if no photos yet
     */
    @Synchronized override fun getLastPhotoFileName(): String? = getStringOption(OptionsKeys.LastPhotoFileName)

    /**
     * Get file name of last photo
     * @return File name of last photo or null if no photos yet
     */
    @Synchronized override fun setLastPhotoFileName(value: String) = setStringOption(OptionsKeys.LastPhotoFileName, value)

    /** Get last footprint id  */
    override fun getLastFootprintId(): Long? = getLongValue(OptionsKeys.LastFootprintId)

    /** Set last footprint id  */
    override fun setLastFootprintId(id: Long?) = setLongValue(OptionsKeys.LastFootprintId, id)

    /** Get total footprints  */
    @Synchronized override fun getTotalFootprints(): Int? = getIntValue(OptionsKeys.TotalFootprints)

    /** Update total footprints value  */
    @Synchronized override fun setTotalFootprints(value: Int) = setIntValue(OptionsKeys.TotalFootprints, value)

    /** Get "Send data to social networks only via WiFi" option  */
    @Synchronized override fun getSyncOnlyViaWifiOptions(): Boolean = getBooleanOption(OptionsKeys.SyncOnlyViaWifiOptions, true)

    /** Update "Send data to social networks only via WiFi" option  */
    @Synchronized override fun setSyncOnlyViaWifiOptions(isTurnOn: Boolean) = setBooleanOption(OptionsKeys.SyncOnlyViaWifiOptions, isTurnOn)

    /** Get creation moment of last processed external sync file  */
    override fun getExternalLastSyncDate(): Date? = getDateValue(OptionsKeys.ExternalLastSyncDate)

    /** Set creation moment of last processed external sync file  */
    override fun setExternalLastSyncDate(externalLastSyncDate: Date) = setDateValue(OptionsKeys.ExternalLastSyncDate, externalLastSyncDate)

    /** Get unique if of app installiation  */
    override fun getInstallId(): String? = getStringOption(OptionsKeys.InstallId)

    /** Set unique if of app installiation  */
    override fun setInstallId(value: String) = setStringOption(OptionsKeys.InstallId, value)
}