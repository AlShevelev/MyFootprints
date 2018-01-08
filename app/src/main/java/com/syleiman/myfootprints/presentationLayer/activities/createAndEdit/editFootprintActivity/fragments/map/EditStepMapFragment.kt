package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map

import android.os.Bundle
import android.view.*
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.FragmentBase
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.EditFootprintActivity
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.map.googleMap.MapManager
import com.syleiman.myfootprints.presentationLayer.customComponents.progress.FullScreenProgress
import kotlinx.android.synthetic.main.fr_create_footprint_map.*
import javax.inject.Inject

/** Second step - edit footprint - map  */
class EditStepMapFragment
@Inject
constructor() : FragmentBase(), IEditStepMapFragmentCallbacks
{
    @Inject internal lateinit var presenter: EditStepMapFragmentPresenter

    private var isShowed = false

    private var mapManager: MapManager? = null

    /**  */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        App.application.getEditFootprintActivityComponent(activity as EditFootprintActivity).inject(this)

        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
    }

    /**  */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val rootView = inflater!!.inflate(R.layout.fr_create_footprint_map, container, false)
        return rootView
    }

    /**  */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        mapManager = MapManager(presenter, progressBar)           // Show map
        mapManager!!.initMap(this, R.id.mapContainer)

        tbPhoto.setOnClickListener { presenter.switchToPhoto() }
        btnSave.setOnClickListener { save() }
    }

    /**  */
    override fun onHiddenChanged(hidden: Boolean)
    {
        if (!hidden)
            onShow()

        super.onHiddenChanged(hidden)
    }

    /**  */
    override fun onResume()
    {
        onShow()
        super.onResume()
    }

    /** When map showed  */
    private fun onShow()
    {
        presenter.onShow(btnSave, context)      // Soft keyboard hided here

        isShowed = false
        btnSave.isEnabled = presenter.getSaveButtonState()
        isShowed = true

        if (!presenter.isInternetEnabled)
            showMessage(R.string.message_box_map_no_internet)
    }

    /**  */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?)
    {
        inflater!!.inflate(R.menu.menu_createfootprint_map, menu)
    }

    /**  */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when (item!!.itemId)
        {
            R.id.action_location ->
            {
                mapManager!!.processLocationMenuItem()
                return true
            }
            R.id.action_help ->
            {
                presenter.clickOnHelpButton()
                return true
            }
            else -> return false
        }
    }

    /** Save footprint  */
    override fun save()
    {
        val activity = activity

        FullScreenProgress.Show(activity, false)

        mapManager!!.getSnapshot { snapshot ->
            presenter.updateFootprint(activity, snapshot) { isSuccess ->
                if (!isSuccess)
                    this.showMessage(R.string.message_box_cant_save_footprint)

                FullScreenProgress.Hide()
                presenter.clickOnCloseButton(false, isSuccess)
            }
        }
    }
}