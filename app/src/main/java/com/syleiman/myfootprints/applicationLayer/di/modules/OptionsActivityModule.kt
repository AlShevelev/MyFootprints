package com.syleiman.myfootprints.applicationLayer.di.modules

import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.businessLayer.optionsCacheService.IOptionsCacheService
import com.syleiman.myfootprints.presentationLayer.activities.options.OptionsActivityModel

import dagger.Module
import dagger.Provides

/** Dagger module for Options activity  */
@Module
class OptionsActivityModule
{
    @Provides
    @ActivityScope
    fun provideModel(options : IOptionsCacheService): OptionsActivityModel = OptionsActivityModel(options)
}
