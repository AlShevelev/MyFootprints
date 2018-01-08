package com.syleiman.myfootprints.applicationLayer.di.components

import com.syleiman.myfootprints.applicationLayer.di.modules.OptionsActivityModule
import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.presentationLayer.activities.options.OptionsActivity

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(OptionsActivityModule::class))
@ActivityScope
interface OptionsActivityComponent
{
    fun inject(activity: OptionsActivity)
}
