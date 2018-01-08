package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel

/** From Croping on Back or Cancel button transition */
class CropingOnBackOrCancelTransition(
    stateMachine : IStateMachineForTransition,
    model : IPhotoEditorModel) : NotEmptyTransition(stateMachine, model)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        val priorState = transitionsStack.findPrior(States.Croping)!!
        val view = stateMachine.getView()

        when(priorState)
        {
            States.Initial ->
            {
                view.setCropImageVisibility(false)
                view.setOkCancelButtonsVisibility(false)

                view.setImageVisibility(true)
                view.setFiltersVisibility(true)
            }
            States.Edited ->
            {
                view.setCropImageVisibility(false)
                view.setOkCancelButtonsVisibility(false)

                view.setImageVisibility(true)
                view.setFiltersVisibility(true)
                view.setUndoAcceptButtonsVisibility(true)
            }
            else ->
            {
                throw UnsupportedOperationException("This state is not supported: "+ transitionsStack)
            }
        }

        stateMachine.moveToState(priorState, false)
    }
}