package com.syleiman.myfootprints.applicationLayer.di.modules

import com.syleiman.myfootprints.common.UniversalRepository
import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.presentationLayer.commonUIHelper.ICommonUIHelper
import com.syleiman.myfootprints.businessLayer.footprintsService.IFootprintsService
import com.syleiman.myfootprints.businessLayer.galleryService.IGalleryService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.businessLayer.sysInfoService.SystemInformationService
import com.syleiman.myfootprints.presentationLayer.activities.InternalActivitiesCodes
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.PhotosGridFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.PhotosGridFragmentModel
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.PhotosGridFragmentPresenter
import com.syleiman.myfootprints.presentationLayer.externalIntents.camera.PhotoCapture
import com.syleiman.myfootprints.presentationLayer.externalIntents.gallery.PhotoFromGallery
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.EditFootprintActivityModel
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.EditFootprintActivityPresenter
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.IEditFootprintActivityCallbacks
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.EditStepFragmentModel
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map.EditStepMapFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map.EditStepMapFragmentPresenter
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.photo.EditStepPhotoFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.photo.EditStepPhotoFragmentPresenter

import dagger.Module
import dagger.Provides

/** Dagger module for EditFootprintActivity  */
@Module
class EditFootprintActivityModule(private val activity: IEditFootprintActivityCallbacks)
{
    private lateinit var photosGridFragment: PhotosGridFragment
    private lateinit var createStepFragment: EditStepPhotoFragment
    private lateinit var createStepMapFragment: EditStepMapFragment

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
    fun provideActivityModel(): EditFootprintActivityModel
    {
        return EditFootprintActivityModel()
    }

    @Provides
    @ActivityScope
    fun provideActivityPresenter(model: EditFootprintActivityModel, presentersRepository: UniversalRepository): EditFootprintActivityPresenter
    {
        return EditFootprintActivityPresenter(activity, presentersRepository, model)
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
        return PhotosGridFragmentPresenter(photosGridFragment, presentersRepository, model, bitmapService, sysInfo, InternalActivitiesCodes.EditFootprint)
    }

    @Provides
    @ActivityScope
    fun providePhotosGridFragment(): PhotosGridFragment
    {
        photosGridFragment = PhotosGridFragment.Create(InternalActivitiesCodes.EditFootprint)
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
        footprints : IFootprintsService,
        sysInfo : SystemInformationService): EditStepFragmentModel
    {
        return EditStepFragmentModel(footprints, sysInfo)
    }

    @Provides
    @ActivityScope
    fun provideCreateStepFragmentPresenter(model: EditStepFragmentModel, presentersRepository: UniversalRepository): EditStepPhotoFragmentPresenter
    {
        return EditStepPhotoFragmentPresenter(createStepFragment, presentersRepository, model)
    }

    @Provides
    @ActivityScope
    fun provideCreateStepFragment(): EditStepPhotoFragment
    {
        createStepFragment = EditStepPhotoFragment()
        return createStepFragment
    }
    // endregion

    //region CreateStepMapFragment
    @Provides
    @ActivityScope
    fun provideCreateMapStepFragmentPresenter(
        model: EditStepFragmentModel,
        presentersRepository: UniversalRepository,
        commonUI: ICommonUIHelper): EditStepMapFragmentPresenter
    {
        return EditStepMapFragmentPresenter(createStepMapFragment, presentersRepository, model, commonUI)
    }

    @Provides
    @ActivityScope
    fun provideCreateStepMapFragment(): EditStepMapFragment
    {
        createStepMapFragment = EditStepMapFragment()
        return createStepMapFragment
    }
    // endregion
}