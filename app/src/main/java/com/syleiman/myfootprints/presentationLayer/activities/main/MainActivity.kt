package com.syleiman.myfootprints.presentationLayer.activities.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.LogTags
import com.syleiman.myfootprints.presentationLayer.activities.ActivityBase
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.CreateFootprintActivity
import com.syleiman.myfootprints.presentationLayer.activities.gridGallery.GridGalleryActivity
import com.syleiman.myfootprints.presentationLayer.activities.myWorld.MyWorldActivity
import com.syleiman.myfootprints.presentationLayer.activities.options.OptionsActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : ActivityBase(), IMainActivityCallbacks
{
    @Inject internal lateinit var presenter: MainActivityPresenter

    /**  */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.application.getMainActivityComponent(this).inject(this)

        ibNewFootprint.setOnClickListener { CreateFootprintActivity.start(this) }       // Start activity for footprint creation
        ibMyWorld.setOnClickListener { MyWorldActivity.start(this) }                    // Start My World activity
        ibGallery.setOnClickListener { GridGalleryActivity.start(this) }                // Show grid gallery
        ibOptions.setOnClickListener { OptionsActivity.start(this) }                    // Show options

        presenter.init()
    }

    /**  */
    override fun onStart()
    {
        presenter.updateCover()
        super.onStart()
    }

    /**  */
    override fun onDestroy()
    {
        if(isFinishing)
        {
            presenter.onDestroyUi()    // Activity destroyed finally
            App.application.releaseMainActivityComponent()
        }

        super.onDestroy()
    }

    /**
     * Set cover image
     * @param image
     */
    override fun setCover(image: Drawable)
    {
        ivCover.setImageDrawable(image)
        pbCover.visibility = View.INVISIBLE
        ivCover.visibility = View.VISIBLE
    }

    /**
     * Set total footprints label
     * @param totalFootprintsQuantity
     */
    override fun setTotalFootprints(totalFootprintsQuantity: Int)
    {
        tvTotalFootprints.text = String.format(App.getStringRes(R.string.footprints_total_label), totalFootprintsQuantity)
    }

    /**
     * Sync was completed
     * @param needUpdate need update UI
     */
    override fun syncCompleted(needUpdate: Boolean)
    {
        Log.d(LogTags.SYNC_PROCESS, "Main activity. Sync complete. Need update: " + needUpdate)

        if (needUpdate)
            presenter.updateCover()
    }
}