package com.syleiman.myfootprints.applicationLayer.di.modules

import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.businessLayer.geoLocationService.IGeoLocationService
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.TrackingModesFacade

import dagger.Module
import dagger.Provides

/**
 * Dagger module for main activity
 */
@Module
class ServicesModule
{
    @Provides
    @ActivityScope
    fun provideModel(geoLocation : IGeoLocationService): TrackingModesFacade = TrackingModesFacade(geoLocation)
}