package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments

/** Base interface for fragments' callbacks */
interface ICreateStepFragmentCallbacksBase
{
    /**
     * Show Query dialog
     * @param messageResource - message string
     */
    fun showQuery(messageResource: Int, confirmCallback: Function0<Unit>)

    /** Show an error  */
    fun showMessage(stringResId: Int)
}
