package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract

/** Base class for all transitions */
abstract class TransitionBase(protected val stateMachine : IStateMachineForTransition)
{
    /**
     *  Process transition
     *  @param fromState - the state before current
     *  @param data - some data
     *  @param pocketData - some trans-transitions data
     */
    abstract fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data : Any? = null, pocketData : Any? = null)
}