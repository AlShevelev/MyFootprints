package com.syleiman.myfootprints.applicationLayer.di.components

import com.syleiman.myfootprints.applicationLayer.di.modules.CreateFootprintActivityModule
import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.CreateFootprintActivity
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.photo.CreateStepPhotoFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.map.CreateStepMapFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.PhotosGridFragment

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(CreateFootprintActivityModule::class))
@ActivityScope
interface CreateFootprintActivityComponent
{
    fun inject(activity: CreateFootprintActivity)
    fun inject(fragment: PhotosGridFragment)
    fun inject(fragment: CreateStepPhotoFragment)
    fun inject(fragment: CreateStepMapFragment)
}
