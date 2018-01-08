package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel

/** From Edited to Croping transition */
class EditedToCropingTransition(
    stateMachine : IStateMachineForTransition,
    model : IPhotoEditorModel) : NotEmptyTransition(stateMachine, model)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        val view = stateMachine.getView()

        view.updateCropImage(model.getLast(false))

        view.setImageVisibility(false)
        view.setUndoAcceptButtonsVisibility(false)
        view.setFiltersVisibility(false)

        view.setCropImageVisibility(true)
        view.setOkCancelButtonsVisibility(true)

        stateMachine.moveToState(States.Croping)
    }
}