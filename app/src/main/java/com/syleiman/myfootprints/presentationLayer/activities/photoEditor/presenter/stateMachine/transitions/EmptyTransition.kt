package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitions

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.IStateMachineForTransition
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack.ITransitionsStackExtract

/** Empty transition */
class EmptyTransition(stateMachine : IStateMachineForTransition) : TransitionBase(stateMachine)
{
    override fun process(fromState : States, transitionsStack: ITransitionsStackExtract, data: Any?, pocketData: Any?)
    {
        stateMachine.moveToState(fromState, false)            // Do nothing
    }
}