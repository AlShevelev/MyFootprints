package com.syleiman.myfootprints.applicationLayer.di.modules

import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.IPhotoEditorPresenter
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.PhotoEditorPresenter
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.helpers.FilterFactory
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.PhotoEditorModel
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.bitmaps.BitmapsIO
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.bitmaps.IBitmapsIO
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.bitmaps.BitmapsRepository
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.bitmaps.IBitmapsRepository
import dagger.Module
import dagger.Provides

/** Dagger module for PhotoEditorActivity activity  */
@Module
class PhotoEditorModule
{
    @Provides
    @ActivityScope
    fun provideBitmapIO(bitmapsService : IBitmapService): IBitmapsIO = BitmapsIO(bitmapsService)

    @Provides
    @ActivityScope
    fun provideBitmapRepository(bitmapIO : IBitmapsIO): IBitmapsRepository = BitmapsRepository(bitmapIO)

    @Provides
    @ActivityScope
    fun providePhotoEditorModel(bitmapRepository : BitmapsRepository, io : IBitmapsIO): IPhotoEditorModel = PhotoEditorModel(bitmapRepository, io)

    @Provides
    @ActivityScope
    fun providePhotoEditorPresenter(model : IPhotoEditorModel, bitmapService : IBitmapService): IPhotoEditorPresenter
        = PhotoEditorPresenter(model, FilterFactory(bitmapService))
}