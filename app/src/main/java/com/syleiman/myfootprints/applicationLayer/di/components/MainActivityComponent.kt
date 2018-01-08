package com.syleiman.myfootprints.applicationLayer.di.components

import com.syleiman.myfootprints.applicationLayer.di.modules.MainActivityModule
import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.presentationLayer.activities.main.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(MainActivityModule::class))
@ActivityScope
interface MainActivityComponent
{
    fun inject(mainActivity: MainActivity)
}
