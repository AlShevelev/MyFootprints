package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters.IContinuousFilterProcessor
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel


/** From FilterPanelHidedOnTap on TapOnImage transition */
class FilterPanelHidedOnTapOnImageTransition(
    stateMachine : IStateMachineForTransition,
    model : IPhotoEditorModel) : NotEmptyTransition(stateMachine, model)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        val priorState = transitionsStack.findPrior(States.FilterPanelHided)!!
        val view = stateMachine.getView()

        when(priorState)
        {
            States.Initial ->
            {
                view.setFiltersVisibility(true)
            }
            States.Edited ->
            {
                view.setFiltersVisibility(true)
                view.setUndoAcceptButtonsVisibility(true)
            }
            States.Editing ->
            {
                val processor = pocketData as IContinuousFilterProcessor                // Extract processor

                processor.setControlsVisibility(view, true)
                view.setOkCancelButtonsVisibility(true)
            }
            else ->
            {
                throw UnsupportedOperationException("This state is not supported: "+ transitionsStack)
            }
        }

        stateMachine.moveToState(priorState, false)
    }
}