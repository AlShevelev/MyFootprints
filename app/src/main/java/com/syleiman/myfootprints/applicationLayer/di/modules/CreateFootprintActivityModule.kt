package com.syleiman.myfootprints.applicationLayer.di.modules

import com.syleiman.myfootprints.common.UniversalRepository
import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.presentationLayer.commonUIHelper.ICommonUIHelper
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.businessLayer.galleryService.IGalleryService
import com.syleiman.myfootprints.businessLayer.geoLocationService.IGeoLocationService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.presentationLayer.activities.InternalActivitiesCodes
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.CreateFootprintActivityModel
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.CreateFootprintActivityPresenter
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.ICreateFootprintActivityCallbacks
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.photo.CreateStepPhotoFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.CreateStepFragmentModel
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.photo.CreateStepPhotoFragmentPresenter
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.map.CreateStepMapFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.map.CreateStepMapFragmentPresenter
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.PhotosGridFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.PhotosGridFragmentModel
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.PhotosGridFragmentPresenter
import com.syleiman.myfootprints.presentationLayer.externalIntents.camera.PhotoCapture
import com.syleiman.myfootprints.presentationLayer.externalIntents.gallery.PhotoFromGallery

import dagger.Module
import dagger.Provides

/** Dagger module for CreateFootprintActivity  */
@Module
class CreateFootprintActivityModule(private val activity: ICreateFootprintActivityCallbacks)
{
    private lateinit var photosGridFragment: PhotosGridFragment
    private lateinit var createStepFragment: CreateStepPhotoFragment
    private lateinit var createStepMapFragment: CreateStepMapFragment

    /** Repository for flexible presenters link  */
    @Provides
    @ActivityScope
    fun provideUniversalRepository(): UniversalRepository
    {
        return UniversalRepository()
    }

    //region Activity
    @Provides
    @ActivityScope
    fun provideActivityModel(geoLocation : IGeoLocationService): CreateFootprintActivityModel
    {
        return CreateFootprintActivityModel(geoLocation)
    }

    @Provides
    @ActivityScope
    fun provideActivityPresenter(model: CreateFootprintActivityModel, presentersRepository: UniversalRepository): CreateFootprintActivityPresenter
    {
        return CreateFootprintActivityPresenter(activity, presentersRepository, model)
    }
    // endregion

    //region PhotosGridFragment
    @Provides
    @ActivityScope
    fun providePhotosGridFragmentModel(gallery  : IGalleryService, photoCapture: PhotoCapture, photoFromGallery: PhotoFromGallery): PhotosGridFragmentModel
    {
        return PhotosGridFragmentModel(gallery, photoCapture, photoFromGallery)
    }

    @Provides
    @ActivityScope
    fun providePhotosGridFragmentPresenter(
        model: PhotosGridFragmentModel,
        presentersRepository: UniversalRepository,
        bitmapService : IBitmapService,
        sysInfo : ISystemInformationService): PhotosGridFragmentPresenter
    {
        return PhotosGridFragmentPresenter(photosGridFragment, presentersRepository, model, bitmapService, sysInfo, InternalActivitiesCodes.CreateFootprint)
    }

    @Provides
    @ActivityScope
    fun providePhotosGridFragment(): PhotosGridFragment
    {
        photosGridFragment = PhotosGridFragment.Create(InternalActivitiesCodes.CreateFootprint)
        return photosGridFragment
    }

    @Provides
    @ActivityScope
    fun providePhotoCapture(fragment: PhotosGridFragment): PhotoCapture
    {
        return PhotoCapture(fragment)
    }

    @Provides
    @ActivityScope
    fun providePhotoFromGallery(fragment: PhotosGridFragment): PhotoFromGallery
    {
        return PhotoFromGallery(fragment)
    }
    // endregion

    //region CreateStepPhotoFragment
    @Provides
    @ActivityScope
    fun provideCreateStepFragmentModel(
        geoLocation: IGeoLocationService,
        sysInfo : ISystemInformationService,
        footprints : IFootprintsService): CreateStepFragmentModel
    {
        return CreateStepFragmentModel(geoLocation, sysInfo, footprints)
    }

    @Provides
    @ActivityScope
    fun provideCreateStepFragmentPresenter(model: CreateStepFragmentModel, presentersRepository: UniversalRepository): CreateStepPhotoFragmentPresenter
    {
        return CreateStepPhotoFragmentPresenter(createStepFragment, presentersRepository, model)
    }

    @Provides
    @ActivityScope
    fun provideCreateStepFragment(): CreateStepPhotoFragment
    {
        createStepFragment = CreateStepPhotoFragment()
        return createStepFragment
    }
    // endregion

    //region CreateStepMapFragment
    @Provides
    @ActivityScope
    fun provideCreateMapStepFragmentPresenter(
        model: CreateStepFragmentModel,
        presentersRepository: UniversalRepository,
        commonUI: ICommonUIHelper): CreateStepMapFragmentPresenter
    {
        return CreateStepMapFragmentPresenter(createStepMapFragment, presentersRepository, model, commonUI)
    }

    @Provides
    @ActivityScope
    fun provideCreateStepMapFragment(): CreateStepMapFragment
    {
        createStepMapFragment = CreateStepMapFragment()
        return createStepMapFragment
    }
    // endregion
}