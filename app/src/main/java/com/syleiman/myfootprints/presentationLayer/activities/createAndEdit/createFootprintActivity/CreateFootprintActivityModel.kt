package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity

import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.businessLayer.geoLocationService.IGeoLocationService
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.CreateEditFootprintActivityStates

import javax.inject.Inject

/** Model for CreateFootprintActivity */
class CreateFootprintActivityModel
@Inject
constructor(private val geoLocation : IGeoLocationService)
{
    var currentState: CreateEditFootprintActivityStates? = null

    init
    {
        currentState = CreateEditFootprintActivityStates.None
    }

    /** Get resource with activity title  */
    val title: Int
        get()
        {
            when (currentState)
            {
                CreateEditFootprintActivityStates.InGalleryMode -> return R.string.create_footprint_activity_title1     // "Select photo"
                CreateEditFootprintActivityStates.InPhotoMode -> return R.string.create_footprint_activity_title2       // "Add comment"
                CreateEditFootprintActivityStates.InMapMode -> return R.string.create_footprint_activity_title3         // "Set location"
                else -> throw UnsupportedOperationException("Invalid value: " + currentState!!)
            }
        }

    val isGeoLocationEnabled: Boolean
        get() = geoLocation.isGeoLocationEnabled()
}
