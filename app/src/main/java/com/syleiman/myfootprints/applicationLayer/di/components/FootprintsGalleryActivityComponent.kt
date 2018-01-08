package com.syleiman.myfootprints.applicationLayer.di.components

import com.syleiman.myfootprints.applicationLayer.di.modules.FootprintsGalleryActivityModule
import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.FootprintsGalleryActivity

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(FootprintsGalleryActivityModule::class))
@ActivityScope
interface FootprintsGalleryActivityComponent
{
    fun inject(activity: FootprintsGalleryActivity)
}
