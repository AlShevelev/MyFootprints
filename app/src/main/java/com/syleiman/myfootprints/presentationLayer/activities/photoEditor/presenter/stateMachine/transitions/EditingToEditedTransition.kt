package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import android.graphics.Bitmap
import android.util.Log
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters.IContinuousFilterProcessor
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel

/** From Editing to Edited transition */
class EditingToEditedTransition(
    stateMachine : IStateMachineForTransition,
    model : IPhotoEditorModel) : NotEmptyTransition(stateMachine, model)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        val processor = pocketData as IContinuousFilterProcessor
        val view = stateMachine.getView()

        Log.d("UPDATE_IMAGE", "view.setProgressVisibility(true)")
        view.setProgressVisibility(true)

        view.setOkCancelButtonsVisibility(false)
        processor.setControlsVisibility(view, false)

        if(processor.effectWasSet)
        {
            var result : Bitmap? = null
            processInBackground(
            {
                result = processor.getResult()
                if(!stateMachine.isUnsubscribed())
                    model.addBitmap(result!!)
            },
            {
                view.updateImage(result!!)
                restoreViewState(view)
                stateMachine.moveToState(States.Edited)
            })
        }
        else
        {
            restoreViewState(view)
            stateMachine.moveToState(States.Edited)
        }

    }

    private fun restoreViewState(view : IPhotoEditorView)
    {
        view.setImageVisibility(true)
        view.setFiltersVisibility(true)
        view.setUndoAcceptButtonsVisibility(true)
        view.setProgressVisibility(false)
        Log.d("UPDATE_IMAGE", "view.setProgressVisibility(false)")
    }
}