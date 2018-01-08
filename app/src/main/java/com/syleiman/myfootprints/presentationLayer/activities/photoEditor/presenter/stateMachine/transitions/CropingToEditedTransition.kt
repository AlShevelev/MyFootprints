package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import android.graphics.Bitmap
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel

/** From Croping to Edited transition */
class CropingToEditedTransition(
    stateMachine : IStateMachineForTransition,
    model : IPhotoEditorModel) : NotEmptyTransition(stateMachine, model)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        val view = stateMachine.getView()

        view.setProgressVisibility(true)

        val cropedBitmap = data!! as Bitmap
        val currentBitmap = model.getLast(true)

        if(cropedBitmap.width != currentBitmap.width || cropedBitmap.height!=currentBitmap.height)      // Image was croped
        {
            processInBackground(
            {
                model.addBitmap(cropedBitmap)
            },
            {
                view.updateImage(cropedBitmap)
                restoreViewState(view)
                view.setProgressVisibility(false)

                stateMachine.moveToState(States.Edited)
            })
        }
        else
        {
            restoreViewState(view)
            view.setProgressVisibility(false)

            stateMachine.moveToState(States.Edited)
        }
    }

    private fun restoreViewState(view : IPhotoEditorView)
    {
        view.setCropImageVisibility(false)
        view.setOkCancelButtonsVisibility(false)

        view.setImageVisibility(true)
        view.setUndoAcceptButtonsVisibility(true)
        view.setFiltersVisibility(true)
    }
}