package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.activities.ActivityBase
import com.syleiman.myfootprints.presentationLayer.activities.InternalActivitiesCodes
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.CreateEditFootprintActivityStates
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.PhotosGridFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.map.CreateStepMapFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.photo.CreateStepPhotoFragment
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.PhotoEditorActivity
import dagger.Lazy
import java.security.InvalidParameterException
import javax.inject.Inject

class CreateFootprintActivity : ActivityBase(), ICreateFootprintActivityCallbacks
{
    @Inject internal lateinit var presenter: CreateFootprintActivityPresenter
    private var stateSaved: Boolean = false

    @Inject internal lateinit var photosGridFragmentInit: Lazy<PhotosGridFragment>
    private var photosGridFragment: Fragment? = null
    @Inject internal lateinit var createStepPhotoFragmentInit: Lazy<CreateStepPhotoFragment>
    private var createStepPhotoFragment: Fragment? = null
    @Inject internal lateinit var createStepMapFragmentInit: Lazy<CreateStepMapFragment>
    private var createStepMapFragment: Fragment? = null

    /**  */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_footprint)

        stateSaved = false
        App.application.getCreateFootprintActivityComponent(this).inject(this)

        presenter.init()
    }

    /**  */
    override fun onStart()
    {
        presenter.start()
        super.onStart()
    }

    /**  */
    override fun onPause()
    {
        presenter.pause()
        super.onPause()
    }

    /**  */
    /*
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        stateSaved = true;
        super.onSaveInstanceState(outState);
    }
*/

    /**  */
    override fun onDestroy()
    {
        if (isFinishing)        // Activity destroyed finally
        {
            presenter.onDestroyUi()
            App.application.releaseCreateFootprintActivityComponent()
        }

        super.onDestroy()
    }

    /** Back button pressed  */
    override fun onBackPressed()
    {
        presenter.onBackPressed()
    }

    /**
     * Show photos grid
     * @param oldState - state in switch moment
     */
    override fun switchToPhotoGrid(oldState: CreateEditFootprintActivityStates)
    {
        val from: Fragment?
        when (oldState)
        {
            CreateEditFootprintActivityStates.InPhotoMode -> from = createStepPhotoFragment
            CreateEditFootprintActivityStates.InMapMode -> from = createStepMapFragment
            CreateEditFootprintActivityStates.None -> from = null
            else -> throw InvalidParameterException("Can't support this state: " + oldState)
        }

        switchFragments(from, photosGridFragment) {
            photosGridFragment = photosGridFragmentInit.get()
            photosGridFragment as PhotosGridFragment
        }
    }

    /** Show second step - create fragment (if current fragment is photo grid)  */
    override fun switchToCreateStepPhotoFromPhotoGrid()
    {
        switchFragments(photosGridFragment, createStepPhotoFragment) {
            createStepPhotoFragment = createStepPhotoFragmentInit.get()
            createStepPhotoFragment as CreateStepPhotoFragment
        }
    }

    /** Switch from map to photo  */
    override fun switchToCreateStepPhotoFromMap()
    {
        switchFragments(createStepMapFragment, createStepPhotoFragment) {
            createStepPhotoFragment = createStepPhotoFragmentInit.get()
            createStepPhotoFragment as CreateStepPhotoFragment
        }
    }

    /** Switch from photo to map */
    override fun switchToCreateStepMapFromPhoto()
    {
        switchFragments(createStepPhotoFragment, createStepMapFragment) {
            createStepMapFragment = createStepMapFragmentInit.get()
            createStepMapFragment as CreateStepMapFragment
        }
    }

    /** Switch from one fragment to another   */
    private fun switchFragments(from: Fragment?, to: Fragment?, createTo: Function0<Fragment>)
    {
        val transaction = supportFragmentManager.beginTransaction()

        if (from != null)
            transaction.hide(from)

        if (to == null)
            transaction.add(R.id.mainContainer, createTo.invoke())
        else
            transaction.show(to)

        transaction.commit()
    }

    /** finish activity  */
    override fun close()
    {
        finish()
    }

    /** Show geolocation settings of device  */
    override fun showGeoLocationSettings()
    {
        startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    /** Show Photo editor  */
    override fun showEditPhotoActivity(photo: IFileSingleOperation)
    {
        PhotoEditorActivity.start(this, photo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (resultCode != Activity.RESULT_OK)
            return

        InternalActivitiesCodes.from(requestCode)?.let {
            when (it)
            {
                InternalActivitiesCodes.PhotoEditor ->
                {
                    val editedPhotoPath = data!!.getStringExtra(PhotoEditorActivity.Consts.PATH_TO_RESULT_FILE)
                    presenter.updatePhotoAfterEdit(FileSingle.fromPath(editedPhotoPath))
                }
                else -> { Log.d("CreateFootprintActivity", "onActivityResult code: "+requestCode) }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object
    {

        /** Start this activity   */
        fun start(parentActivity: Activity)
        {
            val intent = Intent(parentActivity, CreateFootprintActivity::class.java)
            parentActivity.startActivity(intent)
        }
    }
}