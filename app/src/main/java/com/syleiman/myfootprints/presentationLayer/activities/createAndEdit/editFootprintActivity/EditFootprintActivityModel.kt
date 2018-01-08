package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity

import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.CreateEditFootprintActivityStates

import javax.inject.Inject

class EditFootprintActivityModel

@Inject
constructor()
{
    var currentState: CreateEditFootprintActivityStates? = null

    init
    {
        currentState = CreateEditFootprintActivityStates.None
    }

    /** Get resource with activity title  */
    // "Select photo"
    // "Add comment"
    // "Set location"
    val title: Int
        get()
        {
            when (currentState)
            {
                CreateEditFootprintActivityStates.InGalleryMode -> return R.string.create_footprint_activity_title1
                CreateEditFootprintActivityStates.InPhotoMode -> return R.string.create_footprint_activity_title2
                CreateEditFootprintActivityStates.InMapMode -> return R.string.create_footprint_activity_title3
                else -> UnsupportedOperationException("Invalid value: " + currentState!!)
            }
            return 0
        }
}
