package com.syleiman.myfootprints.applicationLayer.di.modules

import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.businessLayer.mainActivityCoverService.IMainActivityCoverService
import com.syleiman.myfootprints.presentationLayer.activities.main.MainActivity
import com.syleiman.myfootprints.presentationLayer.activities.main.MainActivityModel
import com.syleiman.myfootprints.presentationLayer.activities.main.MainActivityPresenter

import dagger.Module
import dagger.Provides

/** Dagger module for main activity  */
@Module
class MainActivityModule(private val activity: MainActivity)
{
    @Provides
    @ActivityScope
    fun providePresenter(model: MainActivityModel): MainActivityPresenter = MainActivityPresenter(activity, model)

    @Provides
    @ActivityScope
    fun provideModel(mainActivityCover : IMainActivityCoverService): MainActivityModel = MainActivityModel(mainActivityCover)
}
