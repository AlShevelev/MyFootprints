package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine

/** Set of states for state machine */
enum class States(var value : Int)
{
    /** User do some action on continuous filter control */
    Initial(0),

    /** Final state - user ends filtering and gets result */
    Accept(1),

    /** Final state - user cancels filtering */
    Cancel(2),

    /** Close dialog is on screen */
    CloseDialogShowed(3),

    /** Filters panel and top buttons have been hided */
    FilterPanelHided(4),

    /** Some of the filters was user */
    Edited(5),

    /** Croping in process */
    Croping(6),

    /** Continuous editing in process */
    Editing(7);

    companion object Create
    {
        fun from(findValue: Int): UserEvents = UserEvents.values().first { it.value == findValue }
    }
}