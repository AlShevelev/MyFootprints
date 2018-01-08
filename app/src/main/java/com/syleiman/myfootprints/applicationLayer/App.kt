package com.syleiman.myfootprints.applicationLayer

import android.app.Application
import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import com.activeandroid.ActiveAndroid
import com.syleiman.myfootprints.applicationLayer.di.components.*
import com.syleiman.myfootprints.applicationLayer.di.modules.*
import com.syleiman.myfootprints.applicationLayer.sync.SyncStartFacade
import com.syleiman.myfootprints.common.files.FilesMulti
import com.syleiman.myfootprints.presentationLayer.ImageLoaderOptions
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.CreateFootprintActivity
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.EditFootprintActivity
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.FootprintsGalleryActivity
import com.syleiman.myfootprints.presentationLayer.activities.gridGallery.GridGalleryActivity
import com.syleiman.myfootprints.presentationLayer.activities.main.MainActivity
import com.syleiman.myfootprints.presentationLayer.activities.myWorld.MyWorldActivity

/** Application class  */
class App : Application()
{
    private var appComponent: AppComponent? = null
    private var mainActivityComponent: MainActivityComponent? = null
    private var createFootprintActivityComponent: CreateFootprintActivityComponent? = null
    private var editFootprintActivityComponent: EditFootprintActivityComponent? = null
    private var servicesComponent: ServicesComponent? = null
    private var optionsActivityComponent: OptionsActivityComponent? = null
    private var myWorldActivityComponent: MyWorldActivityComponent? = null
    private var footprintsGalleryActivityComponent: FootprintsGalleryActivityComponent? = null
    private var gridGalleryActivityComponent: GridGalleryActivityComponent? = null
    private var photoEditorActivityComponent: PhotoEditorActivityComponent? = null

    /**   */
    override fun onCreate()
    {
        super.onCreate()

        context = applicationContext

        syncFacade = SyncStartFacade(this)      // Schedule sync by network connection established

        ActiveAndroid.setLoggingEnabled(true)
        ActiveAndroid.initialize(this)

        ImageLoaderOptions.initImageLoader()

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        //FilesMulti.inPrivate().forEach({it -> it.length()>5000}) { Log.d("FILES_SIZE", "Name: "+it.name + "; Size: "+it.length()) }
    }

    /**   */
    fun getMainActivityComponent(activity: MainActivity): MainActivityComponent
    {
        mainActivityComponent = mainActivityComponent ?: appComponent!!.plus(MainActivityModule(activity))
        return mainActivityComponent as MainActivityComponent
    }

    /**   */
    fun releaseMainActivityComponent()
    {
        mainActivityComponent = null
    }

    /**   */
    fun getCreateFootprintActivityComponent(activity: CreateFootprintActivity): CreateFootprintActivityComponent
    {
        createFootprintActivityComponent = createFootprintActivityComponent ?: appComponent!!.plus(CreateFootprintActivityModule(activity))
        return createFootprintActivityComponent as CreateFootprintActivityComponent
    }

    /**   */
    fun releaseCreateFootprintActivityComponent()
    {
        createFootprintActivityComponent = null
    }

    /**   */
    fun getEditFootprintActivityComponent(activity: EditFootprintActivity): EditFootprintActivityComponent
    {
        editFootprintActivityComponent = editFootprintActivityComponent ?: appComponent!!.plus(EditFootprintActivityModule(activity))
        return editFootprintActivityComponent as EditFootprintActivityComponent
    }

    /**   */
    fun releaseEditFootprintActivityComponent()
    {
        editFootprintActivityComponent = null
    }

    /**   */
    fun getServicesComponent(): ServicesComponent
    {
        servicesComponent = servicesComponent ?: appComponent!!.plus(ServicesModule())
        return servicesComponent as ServicesComponent
    }

    /**   */
    fun releaseServicesComponent()
    {
        servicesComponent = null
    }

    /**   */
    fun getOptionsActivityComponent(): OptionsActivityComponent
    {
        optionsActivityComponent = optionsActivityComponent ?: appComponent!!.plus(OptionsActivityModule())
        return optionsActivityComponent as OptionsActivityComponent
    }

    /**   */
    fun releaseOptionsActivityComponent()
    {
        optionsActivityComponent = null
    }

    /**   */
    fun getMyWorldActivityComponent(activity: MyWorldActivity): MyWorldActivityComponent
    {
        myWorldActivityComponent = myWorldActivityComponent ?: appComponent!!.plus(MyWorldActivityModule(activity))
        return myWorldActivityComponent as MyWorldActivityComponent
    }

    /**   */
    fun releaseMyWorldActivityComponent()
    {
        myWorldActivityComponent = null
    }

    /**   */
    fun getFootprintsGalleryActivityComponent(activity: FootprintsGalleryActivity): FootprintsGalleryActivityComponent
    {
        footprintsGalleryActivityComponent = footprintsGalleryActivityComponent ?: appComponent!!.plus(FootprintsGalleryActivityModule(activity))
        return footprintsGalleryActivityComponent as FootprintsGalleryActivityComponent
    }

    /**   */
    fun releaseFootprintsGalleryActivityComponent()
    {
        footprintsGalleryActivityComponent = null
    }

    /**   */
    fun getGridGalleryActivityComponent(activity: GridGalleryActivity): GridGalleryActivityComponent
    {
        gridGalleryActivityComponent = gridGalleryActivityComponent ?: appComponent!!.plus(GridGalleryActivityModule(activity))
        return gridGalleryActivityComponent as GridGalleryActivityComponent
    }

    /**   */
    fun releaseGridGalleryActivityComponent()
    {
        gridGalleryActivityComponent = null
    }

    /**   */
    fun getPhotoEditorActivityComponent(): PhotoEditorActivityComponent
    {
        photoEditorActivityComponent = photoEditorActivityComponent ?: appComponent!!.plus(PhotoEditorModule())
        return photoEditorActivityComponent as PhotoEditorActivityComponent
    }

    /**   */
    fun releasePhotoEditorActivityComponent()
    {
        photoEditorActivityComponent = null
    }

    companion object
    {
        private lateinit var syncFacade: SyncStartFacade

        /**   */
        lateinit var context: Context
            private set

        /**   */
        val application: App
            get() = context as App

        /**   */
        fun getStringRes(resId: Int): String
        {
            return context.getString(resId)
        }

        /**   */
        fun getColorRes(resId: Int): Int
        {
            return ResourcesCompat.getColor(context.resources, resId, null)
        }

        /** Ryn sync  */
        fun startSync()
        {
            syncFacade.startSyncManually()
        }
    }
}