package com.syleiman.myfootprints.presentationLayer.activities

import android.support.v7.app.AppCompatActivity
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.*

/** Base class for all activity  */
abstract class ActivityBase : AppCompatActivity(), IActivityBaseCallback
{
    /** Set title of this activity  */
    override fun setActivityTitle(resourceId: Int)
    {
        setTitle(resourceId)
    }

    /** Show a message  */
    override fun showMessage(stringResId: Int)
    {
        showToast(stringResId, ToastsParams.Position.Center)
    }

    /** Show a message  */
    override fun showMessage(stringResId: Int, duration: ToastsParams.Duration)
    {
        showToast(stringResId, duration, ToastsParams.Position.Center)
    }

    /**
     * Show Query dialog
     * @param messageResource - message string
     */
    override fun showQueryDialog(messageResource: Int, confirmButtonResource: Int, rejectButtonResource: Int, confirmCallback: Function0<Unit>)
    {
        showQueryDialog(messageResource, confirmButtonResource, rejectButtonResource, confirmCallback, null)
    }

    /**
     * Show Query dialog
     * @param messageResource - message string
     */
    override fun showQueryDialog(messageResource: Int, confirmCallback: Function0<Unit>)
    {
        showYesNoDialog(messageResource, confirmCallback)
    }

    /**
     * Show Query dialog
     * @param messageResource - message string
     */
    override fun showQueryDialog(messageResource: Int, confirmCallback: Function0<Unit>, rejectCallback: Function0<Unit>)
    {
        showYesNoDialog(messageResource, confirmCallback, rejectCallback)
    }

    /** Show Message dialog  */
    override fun showOkDialog(messageId: Int, confirmCallback: Function0<Unit>)
    {
        showMessageDialog(messageId, R.string.ok_capitalize, confirmCallback)
    }
}
