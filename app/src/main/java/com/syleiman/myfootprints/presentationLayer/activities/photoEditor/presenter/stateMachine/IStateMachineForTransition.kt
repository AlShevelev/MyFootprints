package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.IPhotoEditorView
import io.reactivex.disposables.Disposable

/** Interface of state machine  for transition */
interface IStateMachineForTransition
{
    /** Move SM to some state */
    fun moveToState(state : States, needPlaceToRollbackStack : Boolean = true)

    /** Update some trans-transitions data (so we can use in one transition data from some prior) */
    fun updatePocket(data : Any?)

    /** Get view */
    fun getView() : IPhotoEditorView

    /** Memorize current long-running operation */
    fun registerLongOperation(longOperation: Disposable?)

    /** Is long-running operation was unsubscribed */
    fun isUnsubscribed() : Boolean
}