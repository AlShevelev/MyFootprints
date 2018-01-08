package com.syleiman.myfootprints.applicationLayer.di.components

import com.syleiman.myfootprints.applicationLayer.di.modules.GridGalleryActivityModule
import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.presentationLayer.activities.gridGallery.GridGalleryActivity

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(GridGalleryActivityModule::class))
@ActivityScope
interface GridGalleryActivityComponent
{
    fun inject(activity: GridGalleryActivity)
}
