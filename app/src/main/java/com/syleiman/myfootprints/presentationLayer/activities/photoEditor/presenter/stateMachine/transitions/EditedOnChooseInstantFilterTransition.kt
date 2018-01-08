package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import android.graphics.Bitmap
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.helpers.FilterFactory
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel

/** From Initial on ChooseInstantFilter transition */
class EditedOnChooseInstantFilterTransition(
    stateMachine : IStateMachineForTransition,
    model : IPhotoEditorModel,
    private val filterFactory : FilterFactory) : NotEmptyTransition(stateMachine, model)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        val action = filterFactory.getAction(data as Filters)         // Get action
        var filterResult : Bitmap? = null

        val view = stateMachine.getView()

        view.setProgressVisibility(true)

        processInBackground(
        {
            filterResult = action.invoke(model.getLast(false))
            if(!stateMachine.isUnsubscribed())
                model.addBitmap(filterResult!!)
        },
        {
            view.updateImage(filterResult!!)          // Success
            view.setProgressVisibility(false)

            stateMachine.moveToState(States.Edited)
        })
   }
}