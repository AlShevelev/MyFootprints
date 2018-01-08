package com.syleiman.myfootprints.applicationLayer.di.modules

import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.FootprintsGalleryActivity
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.FootprintsGalleryActivityModel
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.FootprintsGalleryActivityPresenter

import dagger.Module
import dagger.Provides

/** Dagger module for Footprints gallery activity  */
@Module
class FootprintsGalleryActivityModule(private val activity: FootprintsGalleryActivity)
{
    @Provides
    @ActivityScope
    fun providePresenter(model: FootprintsGalleryActivityModel): FootprintsGalleryActivityPresenter = FootprintsGalleryActivityPresenter(activity, model)

    @Provides
    @ActivityScope
    fun provideModel(footprints : IFootprintsService): FootprintsGalleryActivityModel = FootprintsGalleryActivityModel(footprints)
}
