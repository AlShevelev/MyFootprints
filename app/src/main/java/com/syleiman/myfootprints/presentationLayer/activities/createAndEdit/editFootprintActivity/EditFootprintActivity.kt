package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.activities.ActivityBase
import com.syleiman.myfootprints.presentationLayer.activities.InternalActivitiesCodes
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.CreateEditFootprintActivityStates
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.PhotosGridFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map.EditStepMapFragment
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.photo.EditStepPhotoFragment
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.PhotoEditorActivity
import dagger.Lazy
import java.security.InvalidParameterException
import javax.inject.Inject

class EditFootprintActivity : ActivityBase(), IEditFootprintActivityCallbacks
{
    @Inject internal lateinit var presenter: EditFootprintActivityPresenter
    private var stateSaved: Boolean = false

    @Inject internal lateinit var photosGridFragmentInit: Lazy<PhotosGridFragment>
    private var photosGridFragment: Fragment? = null
    @Inject internal lateinit var editStepPhotoFragmentInit: Lazy<EditStepPhotoFragment>
    private var editStepPhotoFragment: Fragment? = null
    @Inject internal lateinit var editStepMapFragmentInit: Lazy<EditStepMapFragment>
    private var editStepMapFragment: Fragment? = null

    /**  */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_footprint)

        stateSaved = false
        App.application.getEditFootprintActivityComponent(this).inject(this)

        presenter.init(intent.getLongExtra("footprintId", -1))
    }

    /**  */
    override fun onDestroy()
    {
        if (isFinishing)        // Activity destroyed finally
        {
            presenter.onDestroyUi()
            App.application.releaseEditFootprintActivityComponent()
        }

        super.onDestroy()
    }

    /** Back button pressed  */
    override fun onBackPressed() = presenter.onBackPressed()

    /**
     * Show photos grid
     * @param oldState - state in switch moment
     */
    override fun switchToPhotoGrid(oldState: CreateEditFootprintActivityStates)
    {
        val from: Fragment?
        when (oldState)
        {
            CreateEditFootprintActivityStates.InPhotoMode -> from = editStepPhotoFragment
            CreateEditFootprintActivityStates.InMapMode -> from = editStepMapFragment
            CreateEditFootprintActivityStates.None -> from = null
            else -> throw InvalidParameterException("Can't support this state: " + oldState)
        }

        switchFragments(from, photosGridFragment) {
            photosGridFragment = photosGridFragmentInit.get()
            photosGridFragment as PhotosGridFragment
        }
    }

    /** Show second step - create fragment (if current fragment is photo grid)  */
    override fun switchToEditStepPhotoFromPhotoGrid()
    {
        switchFragments(photosGridFragment, editStepPhotoFragment) {
            editStepPhotoFragment = editStepPhotoFragmentInit.get()
            editStepPhotoFragment as EditStepPhotoFragment
        }
    }

    /** Switch from map to photo  */
    override fun switchToEditStepPhotoFromMap()
    {
        switchFragments(editStepMapFragment, editStepPhotoFragment) {
            editStepPhotoFragment = editStepPhotoFragmentInit.get()
            editStepPhotoFragment as EditStepPhotoFragment
        }
    }

    /** Switch from photo to map */
    override fun switchToEditStepMapFromPhoto()
    {
        switchFragments(editStepPhotoFragment, editStepMapFragment) {
            editStepMapFragment = editStepMapFragmentInit.get()
            editStepMapFragment as EditStepMapFragment
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

    /**
     * Finish activity
     * @param saved true if footprint was saved
     */
    override fun close(saved: Boolean)
    {
        setResult(if (saved) Activity.RESULT_OK else Activity.RESULT_CANCELED)
        finish()
    }

    /** Show Photo editor  */
    override fun showEditPhotoActivity(photo: IFileSingleOperation)
    {
        PhotoEditorActivity.start(this, photo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (InternalActivitiesCodes.from(requestCode) === InternalActivitiesCodes.PhotoEditor && resultCode == Activity.RESULT_OK)
        {
            val editedPhotoPath = data!!.getStringExtra(PhotoEditorActivity.Consts.PATH_TO_RESULT_FILE)
            presenter.updatePhotoAfterEdit(FileSingle.fromPath(editedPhotoPath))
        }
    }

    companion object
    {
        /** Start this activity   */
        fun start(parentActivity: Activity, footprintId: Long)
        {
            val intent = Intent(parentActivity, EditFootprintActivity::class.java)
            intent.putExtra("footprintId", footprintId)
            parentActivity.startActivityForResult(intent, InternalActivitiesCodes.EditFootprint.value)
        }
    }
}