package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters.IContinuousFilterProcessor
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel

/** From Editing on Back or cancel button */
class EditingOnBackOrCancelTransition(
    stateMachine : IStateMachineForTransition,
    model : IPhotoEditorModel) : NotEmptyTransition(stateMachine, model)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        val processor = pocketData as IContinuousFilterProcessor
        val priorState = transitionsStack.findPrior(States.Editing)!!

        val view = stateMachine.getView()

        view.updateImage(model.getLast(true))

        view.setOkCancelButtonsVisibility(false)
        processor.setControlsVisibility(view, false)

        view.setFiltersVisibility(true)

        when(priorState)
        {
            States.Edited -> view.setUndoAcceptButtonsVisibility(true)
            States.Initial -> {}            // Do nothing
            else -> throw UnsupportedOperationException("This state is not supported: "+ transitionsStack)
        }

        stateMachine.moveToState(priorState, false)
   }
}