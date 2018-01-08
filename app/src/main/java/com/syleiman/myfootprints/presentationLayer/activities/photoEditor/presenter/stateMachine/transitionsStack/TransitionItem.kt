package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States

/** One transition item */
data class TransitionItem(val from : States, val to : States)
