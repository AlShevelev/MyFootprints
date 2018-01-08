package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter

import android.graphics.Bitmap
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.helpers.FilterFactory
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.StateMachine
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.UserEvents
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel
import javax.inject.Inject

/** Presenter for PhotoEditorActivity */
class PhotoEditorPresenter
@Inject
constructor(
    private val model : IPhotoEditorModel,
    filterFactory : FilterFactory) : IPhotoEditorPresenter
{
    private var view : IPhotoEditorView? = null

    private var stateMachine : StateMachine = StateMachine(model, filterFactory)

    /** Set start bitmap */
    override fun setSource(bitmap: Bitmap) = model.setSource(bitmap)

    /** User tap on image */
    override fun onTapOnImage() = stateMachine.process(UserEvents.TapOnImage)

    /** Back button pressed */
    override fun onBackPressed() = stateMachine.process(UserEvents.BackPressed)

    /** Yes button in close dialog */
    override fun onYesCloseButton() = stateMachine.process(UserEvents.YesCloseButton)

    /** No button in close dialog */
    override fun onNoCloseButton() = stateMachine.process(UserEvents.NoCloseButton)

    /** User choose one of the filters (instant or continuous)*/
    override fun onChooseFilter(filter: Filters)
    {
        if(filter== Filters.Crop)
            stateMachine.process(UserEvents.ChooseCropFilter)
        else
            stateMachine.process(if (filter.isInstant) UserEvents.ChooseInstantFilter else UserEvents.ChooseContinuousFilter, filter)
    }

    /** Accept button (user ends editing and gets result) */
    override fun onAcceptButton() = stateMachine.process(UserEvents.AcceptButton)

    /** User press Undo button */
    override fun onUndoButton() = stateMachine.process(UserEvents.UndoButton)

    /** Ok button in continuous filter */
    override fun onOkContinuousButton()
    {
        when(stateMachine.currentState)
        {
            States.Croping -> stateMachine.process(UserEvents.CropCompleted, view!!.getCroppedBitmap())
            States.Editing -> stateMachine.process(UserEvents.OkContinuousButton)
            else -> throw UnsupportedOperationException("This state is not supported")
        }
    }

    /** Cancel button in continuous filter */
    override fun onCancelContinuousButton() = stateMachine.process(UserEvents.CancelContinuousButton)

    /** User do some action on continuous filter control */
    override fun onContinuousControlAction(actionInfo: Any) = stateMachine.process(UserEvents.ContinuousControlAction, actionInfo)

    /** Get last stored bitmap */
    override fun getLastBitmap(): Bitmap = model.getLast(true)

    /** Reset states before restart */
    override fun resetBeforeRestart()
    {
        stateMachine.cancelLongOperation()          // Cancel long-running operation if needed

        when(stateMachine.currentState)
        {
            States.Croping -> stateMachine.process(UserEvents.CancelContinuousButton)
            States.Editing -> stateMachine.process(UserEvents.CancelContinuousButton)
            States.CloseDialogShowed -> stateMachine.process(UserEvents.NoCloseButton)
            States.FilterPanelHided -> stateMachine.process(UserEvents.TapOnImage)
            else -> { }         // do nothing
        }
        view!!.setProgressVisibility(false)
   }

    /** Set new view */
    override fun initAfterRestart(view: IPhotoEditorView)
    {
        when(stateMachine.currentState)
        {
            States.Edited -> view.setUndoAcceptButtonsVisibility(true)
            States.Initial -> {}            // do nothing
            else -> throw UnsupportedOperationException("This state is not supported")
        }
    }

    /** */
    override fun attachView(view: IPhotoEditorView)
    {
        this.view = view
        stateMachine.attachView(view)
    }

    /** */
    override fun detachView()
    {
        stateMachine.detachView()
        view = null
    }
}