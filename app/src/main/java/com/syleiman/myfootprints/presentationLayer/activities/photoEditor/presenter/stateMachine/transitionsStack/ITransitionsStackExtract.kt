package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States

/** Interface of TransitionsStack for transitions */
interface ITransitionsStackExtract
{
    /** Get prior state for current. Returns null if prior state not found */
    fun findPrior(current : States) : States?
}