package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.FragmentBase
import com.syleiman.myfootprints.presentationLayer.activities.InternalActivitiesCodes
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.gridCellsHandlers.PhotoGridCellSchema
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.CreateFootprintActivity
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.EditFootprintActivity
import com.syleiman.myfootprints.presentationLayer.customComponents.progress.FullScreenProgress
import com.syleiman.myfootprints.presentationLayer.externalIntents.ExternalActivitiesCodes
import kotlinx.android.synthetic.main.fr_image_grid.*
import javax.inject.Inject

/** Grid with photos  */
class PhotosGridFragment

@Inject
constructor() : FragmentBase(), IPhotosGridFragmentCallbacks
{
    @Inject lateinit var presenter: PhotosGridFragmentPresenter

    private var gridSchema: PhotoGridCellSchema? = null            // Cells schema

    /**  */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val args = arguments
        val activityCode = InternalActivitiesCodes.from(args.getInt("activityCode"))

        when (activityCode)
        {
            InternalActivitiesCodes.CreateFootprint -> App.application.getCreateFootprintActivityComponent(activity as CreateFootprintActivity).inject(this)
            InternalActivitiesCodes.EditFootprint -> App.application.getEditFootprintActivityComponent(activity as EditFootprintActivity).inject(this)
            else -> UnsupportedOperationException("This code is not supported: "+activityCode)
        }

        gridSchema = PhotoGridCellSchema(presenter)
    }

    /**  */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater!!.inflate(R.layout.fr_image_grid, container, false)
    }

    /**  */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        presenter.init()
    }

    /**  */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) = inflater!!.inflate(R.menu.menu_createfootprint_grid, menu)

    /**  */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when (item!!.itemId)
        {
            R.id.action_refresh ->
            {
                presenter.refreshGrid()
                return true
            }
            else -> return false
        }
    }

    /**  */
    override fun onResume()
    {
        super.onResume()
        applyScrollListener()
    }

    /**  */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (resultCode != Activity.RESULT_OK)
            return

        ExternalActivitiesCodes.from(requestCode)?.let {
            when (it)
            {
                ExternalActivitiesCodes.CapturePhoto -> presenter.completeCapturePhoto()
                ExternalActivitiesCodes.GetPhotoFromGallery -> presenter.completeGetPhotoFromGallery(data!!)
                else -> { Log.d("PhotosGridFragment", "onActivityResult code: "+requestCode) }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Set scroll listener for grid
     */
    private fun applyScrollListener()
    {
        val pauseOnScroll = false
        val pauseOnFling = true
        grid.setOnScrollListener(PauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling))
    }

    /** Hide photos' grid and show progressBar bar	  */
    override fun showProgress()
    {
        grid.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    /** Show full-screen progress when open photo */
    override fun setProgressFullScreenVisibility(isVisible : Boolean)
    {
        if(isVisible)
            FullScreenProgress.Show(activity, false)
        else
            FullScreenProgress.Hide()
    }

    /**
     * Hide progressBar bar and show photos' grid
     * @param photosUrls urls of photos on local device
     */
    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    override fun showGrid(photosUrls: List<String>)
    {
        grid.adapter = PhotosGridAdapter(activity, photosUrls, gridSchema!!)

        grid.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> gridSchema!!.getHandler(position).onCellClick(position) }

        progressBar.visibility = View.INVISIBLE
        grid.visibility = View.VISIBLE
    }

    companion object
    {
        /** Create fragment  */
        fun Create(activitiesCode: InternalActivitiesCodes): PhotosGridFragment
        {
            val photosGridFragment = PhotosGridFragment()

            val args = Bundle()
            args.putInt("activityCode", activitiesCode.value)

            photosGridFragment.arguments = args

            return photosGridFragment
        }
    }
}