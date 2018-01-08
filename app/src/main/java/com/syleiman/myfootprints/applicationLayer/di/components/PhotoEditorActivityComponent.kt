package com.syleiman.myfootprints.applicationLayer.di.components

import com.syleiman.myfootprints.applicationLayer.di.modules.PhotoEditorModule
import com.syleiman.myfootprints.applicationLayer.di.scopes.ActivityScope
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.PhotoEditorActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(PhotoEditorModule::class))
@ActivityScope
interface PhotoEditorActivityComponent
{
    fun inject(activity: PhotoEditorActivity)
}