package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity

import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.UniversalRepository
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.CreateEditFootprintActivityStates
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.ICreateEditStepPhotoFragmentPresenter
import javax.inject.Inject

class EditFootprintActivityPresenter
@Inject
constructor(
    private val activityCallbacks: IEditFootprintActivityCallbacks,
    private val presentersRepository: UniversalRepository,              // Flexible link of presenters
    private val model: EditFootprintActivityModel) : IEditFootprintActivityPresenterExt
{
    init
    {
        this.presentersRepository.registerEntity(IEditFootprintActivityPresenterExt::class.java, this)
    }

    /** When activity created  */
    fun init(footprintId: Long?)
    {
        presentersRepository.setTempValue(ICreateEditStepPhotoFragmentPresenter::class.java, footprintId!!)           // value for presenter with phot
        switchToEditStepPhotoFromPhotoGrid()          // Show photo and comment
    }

    /**
     * Called from activity when it destroyed finally
     */
    fun onDestroyUi()
    {
        // do nothing
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
    override fun switchToEditStepPhotoFromPhotoGrid()
    {
        model.currentState = CreateEditFootprintActivityStates.InPhotoMode
        activityCallbacks.setActivityTitle(model.title)
        activityCallbacks.switchToEditStepPhotoFromPhotoGrid()
    }

    /** Switch to map  */
    override fun switchToEditStepMapFromPhoto()
    {
        model.currentState = CreateEditFootprintActivityStates.InMapMode
        activityCallbacks.setActivityTitle(model.title)
        activityCallbacks.switchToEditStepMapFromPhoto()
    }

    /** Switch to photo  */
    override fun switchToEditStepPhotoFromMap()
    {
        model.currentState = CreateEditFootprintActivityStates.InPhotoMode
        activityCallbacks.setActivityTitle(model.title)
        activityCallbacks.switchToEditStepPhotoFromMap()
    }

    /** Back button pressed  */
    fun onBackPressed()
    {
        activityCallbacks.showQueryDialog(R.string.edit_footprint_back_to_gallery, R.string.yes_capitalize, R.string.no_capitalize) {
            this.close(false)
        }
    }

    /**
     * Finish activity
     * @param saved true if footprint was saved
     */
    override fun close(saved: Boolean) = activityCallbacks.close(saved)

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