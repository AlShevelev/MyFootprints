package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel

/** From Edited to Accept transition */
class EditedToAcceptTransition(
    stateMachine : IStateMachineForTransition,
    model : IPhotoEditorModel) : NotEmptyTransition(stateMachine, model)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        var pathToLastBitmap : String? = null
        val view = stateMachine.getView()

        view.setProgressVisibility(true)
        processInBackground(
        {
            pathToLastBitmap = model.saveAndGetPath(model.getLast(true))
            model.clear()               // Remove all temporary data
        },
        {
            view.setProgressVisibility(false)

            view.workAccepted(pathToLastBitmap!!)
            stateMachine.moveToState(States.Accept)
        })
    }
}