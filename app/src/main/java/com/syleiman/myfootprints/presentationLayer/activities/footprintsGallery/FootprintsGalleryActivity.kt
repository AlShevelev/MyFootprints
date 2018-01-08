package com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.activities.ActivityBase
import com.syleiman.myfootprints.presentationLayer.activities.InternalActivitiesCodes
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.result.FootprintsGalleryActivityResult
import com.syleiman.myfootprints.presentationLayer.customComponents.progress.FullScreenProgress
import kotlinx.android.synthetic.main.activity_footprints_gallery.*
import javax.inject.Inject

class FootprintsGalleryActivity : ActivityBase(), IFootprintsGalleryActivityCallbacks
{
    @Inject internal lateinit var presenter: FootprintsGalleryActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_footprints_gallery)

        App.application.getFootprintsGalleryActivityComponent(this).inject(this)

        presenter.init(intent.getLongExtra("startFootprintId", -1), intent.getBooleanExtra("normalSortOrder", true))
    }

    /**  */
    override fun onDestroy()
    {
        if(isFinishing)
            App.application.releaseFootprintsGalleryActivityComponent()

        super.onDestroy()
    }

    /**  */
    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_footprint_gallery, menu)
        return true
    }

    /**  */
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_edit ->
            {
                presenter.startEditFootprint(this, pager.currentItem)
                return true
            }
            R.id.action_delete ->
            {
                presenter.deleteFootprint(pager.currentItem)
                return true
            }
            R.id.action_share ->
            {
                presenter.sendToExternal(this, pager.currentItem)
                return true
            }
            else -> return false
        }
    }

    /**
     * Is is init completed
     * @param isSuccess
     */
    override fun initCompleted(isSuccess: Boolean, currentPosition: Int)
    {
        if (isSuccess)
        {
            val adapter = PagesAdapter(supportFragmentManager, presenter)
            pager.adapter = adapter
            pager.setCurrentItem(currentPosition, false)
            progressBar.visibility = View.GONE
        }
        else
        {
            showMessage(R.string.message_box_cant_read_footprints)
            finish()
        }
    }

    /** Close activity  */
    override fun close()
    {
        setResult()
        finish()
    }

    /**  */
    override fun onBackPressed()
    {
        setResult()
        super.onBackPressed()
    }

    /**  */
    override fun showFullScreenProgress()
    {
        FullScreenProgress.Show(this, false)
    }

    /**  */
    override fun hideFullScreenProgress()
    {
        FullScreenProgress.Hide()
    }

    /** Page deleting completed  */
    override fun pageDeleted(newCurrentPageIndex: Int)
    {
        pager.adapter.notifyDataSetChanged()
        pager.setCurrentItem(newCurrentPageIndex, false)
    }

    /** Redraw current page after update  */
    override fun redrawAfterUpdate()
    {
        pager.adapter.notifyDataSetChanged()
    }

    /** Set activity result  */
    private fun setResult()
    {
        val intent = Intent()
        intent.putExtra("FootprintsGalleryActivityResult", presenter.activityResult)
        setResult(Activity.RESULT_OK, intent)
    }

    /**  */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (InternalActivitiesCodes.from(requestCode) === InternalActivitiesCodes.EditFootprint)
            presenter.completeEditFootprint(resultCode, pager.currentItem)
    }

    companion object
    {

        /** Start this activity   */
        fun start(parentActivity: Activity, startFootprintId: Long, normalSortOrder: Boolean)
        {
            val intent = Intent(parentActivity, FootprintsGalleryActivity::class.java)
            intent.putExtra("startFootprintId", startFootprintId)
            intent.putExtra("normalSortOrder", normalSortOrder)
            parentActivity.startActivityForResult(intent, InternalActivitiesCodes.FootprintsGallery.value)
        }

        /** Parse activity result   */
        fun parseResult(intent: Intent): FootprintsGalleryActivityResult
        {
            return intent.getParcelableExtra<FootprintsGalleryActivityResult>("FootprintsGalleryActivityResult")
        }
    }
}