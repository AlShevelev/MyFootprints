package com.syleiman.myfootprints.presentationLayer.activities.gridGallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.activities.ActivityBase
import com.syleiman.myfootprints.presentationLayer.activities.InternalActivitiesCodes
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.FootprintsGalleryActivity
import kotlinx.android.synthetic.main.activity_grid_gallery.*
import javax.inject.Inject

class GridGalleryActivity : ActivityBase(), IGridGalleryActivityCallbacks
{
    @Inject internal lateinit var presenter: GridGalleryActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_gallery)

        App.application.getGridGalleryActivityComponent(this).inject(this)

        presenter.init()
    }

    /**  */
    override fun onDestroy()
    {
        if(isFinishing)
        {
            presenter.onDestroyUi()
            App.application.releaseGridGalleryActivityComponent()
        }
        super.onDestroy()
    }

    /** Hide photos' grid and show progressBar bar	  */
    override fun showProgress()
    {
        grid.visibility = View.INVISIBLE
        noFootprints.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    /** Hide progressBar bar  */
    override fun hideProgress()
    {
        progressBar.visibility = View.INVISIBLE
    }

    /** Hide progressBar bar and show photos' grid  */
    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    override fun showGrid(startPosition: Int)
    {
        grid.adapter = GridAdapter(this, presenter)

        grid.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> this.clickOnItem(position) }

        grid.smoothScrollToPosition(startPosition)

        progressBar.visibility = View.INVISIBLE
        noFootprints.visibility = View.INVISIBLE
        grid.visibility = View.VISIBLE
    }

    /** Show No footprints label  */
    override fun showNoFootprints()
    {
        progressBar.visibility = View.INVISIBLE
        grid.visibility = View.INVISIBLE
        noFootprints.visibility = View.VISIBLE
    }

    /**
     * Sync was completed
     * @param needUpdate need update UI
     */
    override fun syncCompleted(needUpdate: Boolean)
    {
        if (needUpdate)
            presenter.refreshGrid(true)
    }

    /** Click on item  */
    fun clickOnItem(position: Int) = presenter.clickOnItem(this, position)

    /**  */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    {
        if (InternalActivitiesCodes.from(requestCode) === InternalActivitiesCodes.FootprintsGallery && resultCode == Activity.RESULT_OK)
            presenter.processGalleryResult(FootprintsGalleryActivity.parseResult(data))
    }

    companion object
    {
        /** Start this activity   */
        fun start(parentActivity: Activity)
        {
            val intent = Intent(parentActivity, GridGalleryActivity::class.java)
            parentActivity.startActivity(intent)
        }
    }
}