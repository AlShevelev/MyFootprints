package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import android.graphics.Bitmap
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel


/** From Edited on Undo button pressed transition */
class EditedOnUndoTransition(
    stateMachine : IStateMachineForTransition,
    model : IPhotoEditorModel) : NotEmptyTransition(stateMachine, model)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        var lastBitmap : Bitmap? = null

        val view = stateMachine.getView()

        processInBackground(
        {
            lastBitmap = model.removeAndGetLast()
        },
        {
            view.setProgressVisibility(true)
            view.updateImage(lastBitmap!!)
            view.setProgressVisibility(false)

            if(!model.hasUndo())
            {
                view.setUndoAcceptButtonsVisibility(false)
                stateMachine.moveToState(States.Initial)
            }
            else
                stateMachine.moveToState(States.Edited)
        })
    }
}