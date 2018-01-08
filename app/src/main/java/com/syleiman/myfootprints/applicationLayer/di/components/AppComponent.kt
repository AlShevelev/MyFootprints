package com.syleiman.myfootprints.applicationLayer.di.components

import com.syleiman.myfootprints.applicationLayer.di.modules.*
import com.syleiman.myfootprints.applicationLayer.di.scopes.ApplicationScope

import dagger.Component

/** Application level component - for injecting global objects */
@Component(modules = arrayOf(AppModule::class))
@ApplicationScope
interface AppComponent
{
    operator fun plus(mainActivityModule: MainActivityModule): MainActivityComponent
    operator fun plus(module: CreateFootprintActivityModule): CreateFootprintActivityComponent
    operator fun plus(module: EditFootprintActivityModule): EditFootprintActivityComponent
    operator fun plus(module: ServicesModule): ServicesComponent
    operator fun plus(module: OptionsActivityModule): OptionsActivityComponent
    operator fun plus(module: MyWorldActivityModule): MyWorldActivityComponent
    operator fun plus(module: FootprintsGalleryActivityModule): FootprintsGalleryActivityComponent
    operator fun plus(module: GridGalleryActivityModule): GridGalleryActivityComponent
    operator fun plus(module: PhotoEditorModule): PhotoEditorActivityComponent
}
