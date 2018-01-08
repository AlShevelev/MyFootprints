package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import android.graphics.Bitmap
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.helpers.FilterFactory
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel

/** From Initial to Editing transition */
class InitialToEditingTransition(
    stateMachine : IStateMachineForTransition,
    model : IPhotoEditorModel,
    private val filterFactory : FilterFactory) : NotEmptyTransition(stateMachine, model)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        val processor = filterFactory.getContinuousProcessor(data as Filters)
        processor.init(model.getLast(false))

        val view = stateMachine.getView()

        stateMachine.updatePocket(processor)   // Memorize processor

        view.setFiltersVisibility(false)

        processor.setControlsVisibility(view, true)

        if(processor.needSetEffectOnStart())
        {
            view.setProgressVisibility(true)

            var filterResult : Bitmap? = null
            processInBackground(
            {
                filterResult = processor.setEffectOnStart()
            },
            {
                view.updateImage(filterResult!!)
                view.setOkCancelButtonsVisibility(true)

                view.setProgressVisibility(false)
                stateMachine.moveToState(States.Editing)
            })
        }
        else
        {
            view.setOkCancelButtonsVisibility(true)
            stateMachine.moveToState(States.Editing)
        }
    }
}