package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments

/**
 * Base interface for fragments' callbacks
 */
interface IEditStepFragmentCallbacksBase
{
    /**
     * Show Query dialog
     * @param messageResource - message string
     */
    fun showQuery(messageResource: Int, confirmCallback: Function0<Unit>)

    /** Show an error  */
    fun showMessage(stringResId: Int)
}
