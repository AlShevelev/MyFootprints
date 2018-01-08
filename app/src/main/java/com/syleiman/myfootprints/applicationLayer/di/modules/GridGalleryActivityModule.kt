package com.syleiman.myfootprints.applicationLayer.di.modules

import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.presentationLayer.activities.gridGallery.GridGalleryActivity
import com.syleiman.myfootprints.presentationLayer.activities.gridGallery.GridGalleryActivityModel
import com.syleiman.myfootprints.presentationLayer.activities.gridGallery.GridGalleryActivityPresenter

import dagger.Module
import dagger.Provides

/** Dagger module for GridGalleryActivity  */
@Module
class GridGalleryActivityModule(private val activity: GridGalleryActivity)
{
    @Provides
    @ActivityScope
    fun providePresenter(model: GridGalleryActivityModel): GridGalleryActivityPresenter = GridGalleryActivityPresenter(activity, model)

    @Provides
    @ActivityScope
    fun provideModel(footprints : IFootprintsService, bitmapService : IBitmapService): GridGalleryActivityModel = GridGalleryActivityModel(footprints, bitmapService)
}