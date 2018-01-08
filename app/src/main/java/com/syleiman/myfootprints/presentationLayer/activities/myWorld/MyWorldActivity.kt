package com.syleiman.myfootprints.presentationLayer.activities.myWorld

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.activities.ActivityBase
import com.syleiman.myfootprints.presentationLayer.activities.InternalActivitiesCodes
import com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery.FootprintsGalleryActivity
import com.syleiman.myfootprints.presentationLayer.activities.myWorld.googleMap.MapController
import kotlinx.android.synthetic.main.activity_my_world.*
import javax.inject.Inject

class MyWorldActivity : ActivityBase(), IMyWorldActivityCallbacks
{
    @Inject internal lateinit var presenter: MyWorldActivityPresenter
    @Inject internal lateinit var mapController: MapController

    /**  */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_world)

        App.application.getMyWorldActivityComponent(this).inject(this)

        presenter.init()
        mapController.initMap(progressBar, R.id.mapContainer)
    }

    /**  */
    override fun onDestroy()
    {
        if(isFinishing)
        {
            presenter.onDestroyUi()    // Activity destroyed finally
            App.application.releaseMyWorldActivityComponent()
        }
        super.onDestroy()
    }

    /**  */
    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_my_world_activity, menu)
        return true
    }

    /**  */
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_help ->
            {
                presenter.onHelpButtonClick()
                return true
            }
            else -> return false
        }
    }

    /**  */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    {
        if (InternalActivitiesCodes.from(requestCode) == InternalActivitiesCodes.FootprintsGallery && resultCode == Activity.RESULT_OK)
            mapController.processGalleryResult(FootprintsGalleryActivity.parseResult(data))
    }

    /**
     * Sync was completed
     * @param needUpdate need update UI
     */
    override fun syncCompleted(needUpdate: Boolean)
    {
        if (needUpdate)
            mapController.reloadMarkers()
    }

    companion object
    {

        /** Start this activity   */
        fun start(parentActivity: Activity)
        {
            val intent = Intent(parentActivity, MyWorldActivity::class.java)
            parentActivity.startActivity(intent)
        }
    }
}