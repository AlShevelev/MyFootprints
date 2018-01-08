package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine

import android.util.Log
import com.syleiman.myfootprints.common.create2DArray
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.helpers.FilterFactory
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.*
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.TransitionsStack
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel
import io.reactivex.disposables.Disposable

/** State machine for PhotoEditorPresenter */
class StateMachine(
    private val model : IPhotoEditorModel,
    private var filterFactory : FilterFactory) : IStateMachineForTransition
{
    private var longOperation: Disposable? = null             // Long running Rx operation

    private var viewInternal : IPhotoEditorView? = null

    var currentState = States.Initial
        private set(value) {field = value}

    private val transitionsStack = TransitionsStack()

    private lateinit var transitionsMatrix: Array<Array<TransitionBase>>        // Transitions matrix. Row - states; cols - events

    private var pocketData : Any? = null       // some trans-transitions data

    init
    {
        createMatrix()
    }

    /** Create transitions matrix */
    private fun createMatrix()
    {
        transitionsMatrix = create2DArray(8, 13){ EmptyTransition(this) }

        transitionsMatrix[States.Initial.value][UserEvents.ChooseContinuousFilter.value] = InitialToEditingTransition(this, model, filterFactory)
        transitionsMatrix[States.Initial.value][UserEvents.ChooseCropFilter.value] = InitialToCropingTransition(this, model)
        transitionsMatrix[States.Initial.value][UserEvents.BackPressed.value] = ToCloseDialogShowedTransition(this, model)
        transitionsMatrix[States.Initial.value][UserEvents.TapOnImage.value] = InitialToFilterPanelHidedTransition(this, model)
        transitionsMatrix[States.Initial.value][UserEvents.ChooseInstantFilter.value] = InitialToEditedTransition(this, model, filterFactory)

        transitionsMatrix[States.CloseDialogShowed.value][UserEvents.YesCloseButton.value] = CloseDialogShowedToCancelTransition(this, model)
        transitionsMatrix[States.CloseDialogShowed.value][UserEvents.NoCloseButton.value] = CloseDialogShowedOnNoTransition(this, model)

        transitionsMatrix[States.FilterPanelHided.value][UserEvents.BackPressed.value] = ToCloseDialogShowedTransition(this, model)
        transitionsMatrix[States.FilterPanelHided.value][UserEvents.TapOnImage.value] = FilterPanelHidedOnTapOnImageTransition(this, model)

        transitionsMatrix[States.Edited.value][UserEvents.UndoButton.value] = EditedOnUndoTransition(this, model)
        transitionsMatrix[States.Edited.value][UserEvents.TapOnImage.value] = EditedToFilterPanelHidedTransition(this, model)
        transitionsMatrix[States.Edited.value][UserEvents.BackPressed.value] = ToCloseDialogShowedTransition(this, model)
        transitionsMatrix[States.Edited.value][UserEvents.ChooseCropFilter.value] = EditedToCropingTransition(this, model)
        transitionsMatrix[States.Edited.value][UserEvents.ChooseContinuousFilter.value] = EditedToEditingTransition(this, model, filterFactory)
        transitionsMatrix[States.Edited.value][UserEvents.AcceptButton.value] = EditedToAcceptTransition(this, model)
        transitionsMatrix[States.Edited.value][UserEvents.ChooseInstantFilter.value] = EditedOnChooseInstantFilterTransition(this, model, filterFactory)

        transitionsMatrix[States.Croping.value][UserEvents.BackPressed.value] = CropingOnBackOrCancelTransition(this, model)
        transitionsMatrix[States.Croping.value][UserEvents.CancelContinuousButton.value] = CropingOnBackOrCancelTransition(this, model)
        transitionsMatrix[States.Croping.value][UserEvents.CropCompleted.value] = CropingToEditedTransition(this, model)

        transitionsMatrix[States.Editing.value][UserEvents.TapOnImage.value] = EditingToFilterPanelHidedTransition(this, model)
        transitionsMatrix[States.Editing.value][UserEvents.OkContinuousButton.value] = EditingToEditedTransition(this, model)
        transitionsMatrix[States.Editing.value][UserEvents.ContinuousControlAction.value] = EditingToEditingTransition(this, model)
        transitionsMatrix[States.Editing.value][UserEvents.BackPressed.value] = EditingOnBackOrCancelTransition(this, model)
        transitionsMatrix[States.Editing.value][UserEvents.CancelContinuousButton.value] = EditingOnBackOrCancelTransition(this, model)
    }

    /** Process event */
    fun process(event: UserEvents, data : Any? = null)
    {
        Log.d("STATE_MACHINE", "Event is: $event; Current state is: $currentState")

        try
        {
            val transitionInfo = transitionsMatrix[currentState.value][event.value]
            transitionInfo.process(currentState, transitionsStack, data, pocketData)
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
        }

    }

    /** */
    override fun moveToState(state: States, needPlaceToRollbackStack : Boolean)
    {
        Log.d("STATE_MACHINE", "New state is: $state; needPlaceToRollbackStack: $needPlaceToRollbackStack")

        if(needPlaceToRollbackStack)
            transitionsStack.add(currentState, state)

        currentState = state
    }

    /** */
    override fun updatePocket(data: Any?)
    {
        pocketData = data
    }

    /** Get view */
    override fun getView(): IPhotoEditorView = viewInternal!!

    /** Memorize current long-running operation */
    override fun registerLongOperation(longOperation: Disposable?)
    {
        synchronized(this)
        {
            this.longOperation = longOperation
        }
    }

    /** Cancel long-running operation */
    fun cancelLongOperation()
    {
        synchronized(this)
        {
            longOperation?.dispose()
        }
    }

    /** Is long-running operation was unsubscribed */
    override fun isUnsubscribed(): Boolean
    {
        return synchronized(this)
        {
            longOperation?.isDisposed ?: false
        }
    }

    /** */
    fun attachView(view : IPhotoEditorView)
    {
        this.viewInternal = view
    }

    /** */
    fun detachView()
    {
        this.viewInternal = null
    }
}