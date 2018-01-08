package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.viewModel.IPhotoEditorModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/** Transition for some job */
abstract class NotEmptyTransition(
    stateMachine: IStateMachineForTransition,
    protected val model : IPhotoEditorModel) : TransitionBase(stateMachine)
{
    protected fun processInBackground(backgroundThreadAction : ()->Unit, uiThreadAction : ()->Unit)
    {
        val operation = Observable.fromCallable {
            backgroundThreadAction.invoke()
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            stateMachine.registerLongOperation(null)
            uiThreadAction.invoke()
        },
        { e ->                          // Error
            stateMachine.registerLongOperation(null)
            e.printStackTrace()
            throw e
        })
        stateMachine.registerLongOperation(operation)
    }
}