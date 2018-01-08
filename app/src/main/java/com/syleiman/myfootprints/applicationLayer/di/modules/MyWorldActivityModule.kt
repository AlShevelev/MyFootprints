package com.syleiman.myfootprints.applicationLayer.di.modules

import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.presentationLayer.activities.myWorld.MyWorldActivity
import com.syleiman.myfootprints.presentationLayer.activities.myWorld.MyWorldActivityModel
import com.syleiman.myfootprints.presentationLayer.activities.myWorld.MyWorldActivityPresenter
import com.syleiman.myfootprints.presentationLayer.activities.myWorld.googleMap.MapController

import dagger.Module
import dagger.Provides

/** Dagger module for My World activity  */
@Module
class MyWorldActivityModule(private val activity: MyWorldActivity)
{
    @Provides
    @ActivityScope
    fun providePresenter(model: MyWorldActivityModel): MyWorldActivityPresenter = MyWorldActivityPresenter(activity, model)

    @Provides
    @ActivityScope
    fun provideModel(): MyWorldActivityModel = MyWorldActivityModel()

    @Provides
    @ActivityScope
    fun provideMapController(footprints : IFootprintsService, bitmapService : IBitmapService, sysInfo : ISystemInformationService): MapController =
        MapController(activity, footprints, bitmapService, sysInfo)
}
