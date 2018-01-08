package com.syleiman.myfootprints.applicationLayer.di.modules

import android.app.Application

import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.applicationLayer.di.scopes.ApplicationScope
import com.syleiman.myfootprints.businessLayer.bitmapService.BitmapService
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.presentationLayer.commonUIHelper.CommonUIHelper
import com.syleiman.myfootprints.presentationLayer.commonUIHelper.ICommonUIHelper
import com.syleiman.myfootprints.businessLayer.footprintsService.FootprintsService
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.businessLayer.galleryService.GalleryService
import com.syleiman.myfootprints.businessLayer.galleryService.IGalleryService
import com.syleiman.myfootprints.businessLayer.geoLocationService.GeoLocationService
import com.syleiman.myfootprints.businessLayer.geoLocationService.IGeoLocationService
import com.syleiman.myfootprints.businessLayer.localDbService.LocalDbService
import com.syleiman.myfootprints.businessLayer.localDbService.ILocalDbService
import com.syleiman.myfootprints.businessLayer.mainActivityCoverService.MainActivityCoverService
import com.syleiman.myfootprints.businessLayer.mainActivityCoverService.IMainActivityCoverService
import com.syleiman.myfootprints.businessLayer.optionsCacheService.OptionsCacheService
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService
import com.syleiman.myfootprints.businessLayer.tasksService.TasksService
import com.syleiman.myfootprints.businessLayer.tasksService.ITasksService

import dagger.Module
import dagger.Provides

/** Application level module - global objects created here  */
@Module
class AppModule(private val application: Application)
{
    @Provides
    @ApplicationScope
    fun provideApplication(): Application = application

    @Provides
    @ApplicationScope
    internal fun provideBitmapService(): IBitmapService = BitmapService()

    @Provides
    @ApplicationScope
    internal fun provideFootprintsService(
        tasksService: ITasksService,
        sysInfoService: ISystemInformationService,
        mainActivityCoverService: MainActivityCoverService,
        localDbService: ILocalDbService,
        bitmapService: IBitmapService): IFootprintsService = FootprintsService(tasksService, sysInfoService, mainActivityCoverService, localDbService, bitmapService)

    @Provides
    @ApplicationScope
    internal fun provideLocalDbService(bitmapService: IBitmapService): ILocalDbService = LocalDbService(bitmapService)

    @Provides
    @ApplicationScope
    internal fun provideOptionsCacheService(localDbService: ILocalDbService): IOptionsCacheService = OptionsCacheService(localDbService)

    @Provides
    @ApplicationScope
    internal fun provideMainActivityCoverService(
        optionsCacheService : IOptionsCacheService,
        localDbService: ILocalDbService,
        bitmapService: IBitmapService): IMainActivityCoverService = MainActivityCoverService(optionsCacheService, localDbService, bitmapService)

    @Provides
    @ApplicationScope
    internal fun provideSystemInformationService(
        optionsCacheService: IOptionsCacheService): ISystemInformationService = SystemInformationService(optionsCacheService)

    @Provides
    @ApplicationScope
    internal fun provideGeoLocationService(
        systemInformationService: ISystemInformationService,
        localDb: ILocalDbService): IGeoLocationService = GeoLocationService(systemInformationService, localDb.lastLocation())

    @Provides
    @ApplicationScope
    internal fun provideGalleryService(): IGalleryService = GalleryService()

    @Provides
    @ApplicationScope
    internal fun provideTasksService(
        localDb: ILocalDbService,
        systemInformationService: ISystemInformationService): ITasksService = TasksService(localDb, systemInformationService)

    @Provides
    @ApplicationScope
    internal fun provideCommonUIService(): ICommonUIHelper = CommonUIHelper()
}
