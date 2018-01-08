package com.syleiman.myfootprints.presentationLayer

import android.support.v4.app.Fragment

/**
 * Base class for all fragments
 */
open class FragmentBase : Fragment()
{
    /**
     * Show an error
     */
    fun showMessage(stringResId: Int)
    {
        context.showToast(stringResId, ToastsParams.Position.Center)
    }

    /**
     * Show Query dialog
     * @param messageResource - message string
     */
    fun showQuery(messageResource: Int, confirmCallback: Function0<Unit>)
    {
        context.showYesNoDialog(messageResource, confirmCallback)
    }
}
