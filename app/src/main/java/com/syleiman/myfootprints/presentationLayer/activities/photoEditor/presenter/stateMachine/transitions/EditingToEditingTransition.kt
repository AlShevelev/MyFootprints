package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import android.graphics.Bitmap
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions.continuousFilters.IContinuousFilterProcessor
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel

/** From Editing to Editing transition */
class EditingToEditingTransition(
    stateMachine : IStateMachineForTransition,
    model : IPhotoEditorModel) : NotEmptyTransition(stateMachine, model)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        val processor = pocketData as IContinuousFilterProcessor

        var processedBitmap : Bitmap? = null

        val view = stateMachine.getView()

        view.setProgressVisibility(true)
        processInBackground(
        {
            processedBitmap = processor.setEffect(data!!)
        },
        {
            view.updateImage(processedBitmap!!)
            view.setProgressVisibility(false)

            stateMachine.moveToState(States.Editing)
        })
    }
}