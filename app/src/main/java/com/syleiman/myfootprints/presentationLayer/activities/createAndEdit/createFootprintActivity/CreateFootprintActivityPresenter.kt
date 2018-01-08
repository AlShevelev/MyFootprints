package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity

import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.services.geoLocationTracking.GeoLocationTrackingService
import com.syleiman.myfootprints.common.UniversalRepository
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.CreateEditFootprintActivityStates
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.ICreateEditStepPhotoFragmentPresenter
import javax.inject.Inject

class CreateFootprintActivityPresenter
@Inject
constructor(
    private val activityCallbacks: ICreateFootprintActivityCallbacks,
    private val presentersRepository: UniversalRepository,      // Flexible link of presenters
    private val model: CreateFootprintActivityModel) : ICreateFootprintActivityPresenterExt
{
    init
    {
        this.presentersRepository.registerEntity(ICreateFootprintActivityPresenterExt::class.java, this)
    }

    /** Called from activity when it destroyed finally */
    fun onDestroyUi()
    {
        // do nothing
    }

    /** When activity created  */
    fun init()
    {
        switchToPhotoGrid()          // Show photos grid
        activityCallbacks.setActivityTitle(model.title)
        turnOnGeoLocation()          // Try to turn on geolocation
    }

    /** When activity started  */
    fun start()
    {
        GeoLocationTrackingService.startInActiveMode()   // Switch service to active mode
    }

    /** When activity paused  */
    fun pause()
    {
        GeoLocationTrackingService.startInPassiveMode()   // Switch service to passive mode
    }

    /** Try to turn on geolocation  */
    private fun turnOnGeoLocation()
    {
        if (!model.isGeoLocationEnabled)
            activityCallbacks.showQueryDialog(R.string.create_footprint_dialog_back_turn_on_location, R.string.go_to_settings, R.string.not_now) {
                activityCallbacks.showGeoLocationSettings()
            }
    }

    /** Show photos grid  */
    override fun switchToPhotoGrid()
    {
        val oldState = model.currentState

        model.currentState = CreateEditFootprintActivityStates.InGalleryMode
        activityCallbacks.setActivityTitle(model.title)
        activityCallbacks.switchToPhotoGrid(oldState!!)
    }

    /** Show second step - create fragment  */
    override fun switchToCreateStepPhotoFromPhotoGrid()
    {
        model.currentState = CreateEditFootprintActivityStates.InPhotoMode
        activityCallbacks.setActivityTitle(model.title)
        activityCallbacks.switchToCreateStepPhotoFromPhotoGrid()
    }

    /** Switch to map  */
    override fun switchToCreateStepMapFromPhoto()
    {
        model.currentState = CreateEditFootprintActivityStates.InMapMode
        activityCallbacks.setActivityTitle(model.title)
        activityCallbacks.switchToCreateStepMapFromPhoto()
    }

    /** Switch to photo  */
    override fun switchToCreateStepPhotoFromMap()
    {
        model.currentState = CreateEditFootprintActivityStates.InPhotoMode
        activityCallbacks.setActivityTitle(model.title)
        activityCallbacks.switchToCreateStepPhotoFromMap()
    }

    /** Back button pressed  */
    fun onBackPressed()
    {
        activityCallbacks.showQueryDialog(R.string.create_footprint_dialog_cancel_footprint) {
            activityCallbacks.close()
         }
    }

    /** finish activity  */
    override fun close()
    {
        activityCallbacks.close()
    }

    /** Show Photo editor  */
    override fun showEditPhotoActivity(photo: IFileSingleOperation)
    {
        activityCallbacks.showEditPhotoActivity(photo)
    }

    /** Update photo after edit by EditPhotoActivity */
    override fun updatePhotoAfterEdit(photo: IFileSingleOperation)
    {
        this.presentersRepository.getEntity(ICreateEditStepPhotoFragmentPresenter::class.java)!!.updatePhotoAfterEdit(photo)
    }
}