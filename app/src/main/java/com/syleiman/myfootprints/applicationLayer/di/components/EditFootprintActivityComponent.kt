package com.syleiman.myfootprints.applicationLayer.di.components

import com.syleiman.myfootprints.applicationLayer.di.modules.EditFootprintActivityModule
import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.PhotosGridFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.EditFootprintActivity
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map.EditStepMapFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.photo.EditStepPhotoFragment

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(EditFootprintActivityModule::class))
@ActivityScope
interface EditFootprintActivityComponent
{
    fun inject(activity: EditFootprintActivity)
    fun inject(fragment: PhotosGridFragment)
    fun inject(fragment: EditStepPhotoFragment)
    fun inject(fragment: EditStepMapFragment)
}
