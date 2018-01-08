package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.transitionsStack

import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine.States
import java.util.*

/** Stack of transitions */
class TransitionsStack : ITransitionsStackExtract
{
    val transitions: Stack<TransitionItem> = Stack()

    /** Add states into stack */
    fun add(from : States, to : States)
    {
        if(from!=to)                        // Ignore loops
            transitions.push(TransitionItem(from, to))
    }

    /** Get prior state for current. Returns null if prior state not found */
    override fun findPrior(current : States) : States?
    {
        while (!transitions.empty())
        {
            val item = transitions.pop()        // Extract item

            if(item.to == current)
                return item.from
        }
        return null
    }
}