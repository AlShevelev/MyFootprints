package com.syleiman.myfootprints.applicationLayer.di.components

import com.syleiman.myfootprints.applicationLayer.di.modules.ServicesModule
import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.GeoLocationTrackingService
import com.syleiman.myfootprints.applicationLayer.services.sync.sync.SyncAdapter

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ServicesModule::class))
@ActivityScope
interface ServicesComponent
{
    fun inject(service: GeoLocationTrackingService)
    fun inject(syncAdapter: SyncAdapter)
}