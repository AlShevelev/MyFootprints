package com.syleiman.myfootprints.applicationLayer.di.components

import com.syleiman.myfootprints.applicationLayer.di.modules.MyWorldActivityModule
import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.presentationLayer.activities.myWorld.MyWorldActivity

import dagger.Subcomponent

@Subcomponent(modules = arrayOf(MyWorldActivityModule::class))
@ActivityScope
interface MyWorldActivityComponent
{
    fun inject(activity: MyWorldActivity)
}
