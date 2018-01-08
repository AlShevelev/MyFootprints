package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.stateMachine

/** Set of events from user */
enum class UserEvents(val value : Int)
{
    /** User tap on image */
    TapOnImage(0),

    /** Back button pressed */
    BackPressed(1),

    /** Yes button in close dialog */
    YesCloseButton(2),

    /** No button in close dialog */
    NoCloseButton(3),

    /** User choose one of the instant filters */
    ChooseInstantFilter(4),

    /** User choose one of the continuous filters */
    ChooseContinuousFilter(5),

    /** User choose the crop filter */
    ChooseCropFilter(6),

    /** Accept button (user ends editing and gets result) */
    AcceptButton(7),

    /** User press Undo button */
    UndoButton(8),

    /** Ok button in continuous filter */
    OkContinuousButton(9),

    /** Cancel button in continuous filter */
    CancelContinuousButton(10),

    /** User do some action on continuous filter control */
    ContinuousControlAction(11),

    /** User complete Crop operation by Ok button */
    CropCompleted(12);

    companion object Create
    {
        fun from(findValue: Int): UserEvents = values().first { it.value == findValue }
    }
}