package com.syleiman.myfootprints.businessLayer.tasksService

import android.location.Address
import android.location.Geocoder
import android.util.Log

import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.businessLayer.sysInfoService.InternetConnectionStatuses
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.modelLayer.dto.TaskDto
import com.syleiman.myfootprints.modelLayer.dto.TaskTypes

import java.io.IOException
import java.sql.SQLException
import java.util.Locale

/** Get Country and City from location  */
class GeoCodingTask(
    private val location: LatLng,
    private val footprintId: Long,
    private val taskId: Long,
    private val localDb: ILocalDbService,
    private val sysInfo: ISystemInformationService?) : ITaskRunning
{

    constructor(location: LatLng, footprintId: Long, localDb: ILocalDbService) : this(location, footprintId, 0, localDb, null)

    constructor(taskDto: TaskDto, localDb: ILocalDbService, sysInfo: ISystemInformationService) : this(
        LatLng(java.lang.Double.parseDouble(taskDto.data1), java.lang.Double.parseDouble(taskDto.data2)),
        taskDto.footprintId,
        taskDto.id,
        localDb,
        sysInfo)

    /**
     * Save task to Db
     * @return Id of task
     * *
     * @throws SQLException
     */
    @Throws(SQLException::class)
    fun save(): Long
    {
        val latitude = java.lang.Double.toString(location.latitude)
        val longitude = java.lang.Double.toString(location.longitude)

        Log.d(LogTags.TASKS_PROCESSING, String.format("GeoCodingTask::saveToPrivateArea() latitude: %1\$s; longitude: %2\$s; footprintId: %3\$s", latitude, longitude, footprintId))

        val taskDto = TaskDto(TaskTypes.Geocoding, footprintId, latitude, longitude, null, null, null, null, null, null, null, null)
        return localDb.tasks().add(taskDto)
    }

    /**
     * Execute task
     * @return true - task complete successfully (from point of vies of service); false - fatal error ant service must interrupt tasks processing
     */
    override fun execute(): Boolean
    {
        val internetStatus = SystemInformationService.getInternetConnectionStatus()
        if (internetStatus === InternetConnectionStatuses.None)
        {
            Log.d(LogTags.TASKS_PROCESSING, "GeoCodingTask::execute() no connection")
            return false           // No Internet connection - must interrupt tasks processing
        }

        if (!Geocoder.isPresent())
        // Geocoder is not present
        {
            Log.d(LogTags.TASKS_PROCESSING, "GeoCodingTask::execute() geocoder is not present")
            return true
        }

        val addresses: List<Address>?
        try
        {
            val geocoder = Geocoder(App.context, Locale.getDefault())            // Get geodata

            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses == null || addresses.isEmpty())
            {
                Log.d(LogTags.TASKS_PROCESSING, "GeoCodingTask::execute() Location received but addresses list is empty")
                return true
            }
        } catch (ex: IOException)
        {
            Log.e(LogTags.TASKS_PROCESSING, "GeoCodingTask::execute() IOException: " + ex.message)
            ex.printStackTrace()
            return false                   // No internet connection
        }

        try
        {
            val countryName = addresses[0].countryName
            val countryCode = addresses[0].countryCode
            val cityName = addresses[0].locality

            Log.d(LogTags.TASKS_PROCESSING, String.format("GeoCodingTask::execute() CountryName: $countryName; CountryCode: $countryCode; CityName: $cityName", countryName, countryCode, cityName))
            localDb.footprints().updateGeoInformation(footprintId, countryName, countryCode, cityName)

            localDb.tasks().delete(taskId)         // Remove task after process
        }
        catch (ex: SQLException)
        {
            Log.e(LogTags.TASKS_PROCESSING, "GeoCodingTask::execute() SQLException: " + ex.message)
            ex.printStackTrace()
            return false
        }

        return true
    }
}