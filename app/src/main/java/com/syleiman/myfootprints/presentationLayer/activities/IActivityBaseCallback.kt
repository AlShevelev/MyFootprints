package com.syleiman.myfootprints.presentationLayer.activities

import com.syleiman.myfootprints.presentationLayer.ToastsParams

/**
 * Base interface for inject in presenter
 */
interface IActivityBaseCallback
{
    /** Set title of this activity  */
    fun setActivityTitle(resourceId: Int)

    /** Show a message  */
    fun showMessage(stringResId: Int)

    /** Show a message  */
    fun showMessage(stringResId: Int, duration: ToastsParams.Duration)

    /**
     * Show Query dialog
     * @param messageResource - message string
     */
    fun showQueryDialog(messageResource: Int, confirmButtonResource: Int, rejectButtonResource: Int, confirmCallback: Function0<Unit>)

    /**
     * Show Query dialog
     * @param messageResource - message string
     */
    fun showQueryDialog(messageResource: Int, confirmCallback: Function0<Unit>)

    /**
     * Show Query dialog
     * @param messageResource - message string
     */
    fun showQueryDialog(messageResource: Int, confirmCallback: Function0<Unit>, rejectCallback: Function0<Unit>)

    /** Show Message dialog  */
    fun showOkDialog(messageId: Int, confirmCallback: Function0<Unit>)
}
