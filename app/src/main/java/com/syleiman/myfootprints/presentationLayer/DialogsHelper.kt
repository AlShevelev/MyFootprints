package com.syleiman.myfootprints.presentationLayer

import android.content.Context
import android.support.v7.app.AlertDialog
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.App

/** Common dialogs  */

/**
 * Query dialog
 * @param messageId - message string
 */
fun Context.showQueryDialog(
        messageId: Int,
        confirmButtonTextId: Int,
        rejectButtonTextId: Int,
        confirmCallback: Function0<Unit>?,
        rejectCallback: Function0<Unit>? = null)
{
    AlertDialog.Builder(this).setMessage(this.getString(messageId)).setPositiveButton(confirmButtonTextId) { _, _ ->
        confirmCallback?.invoke()
    }.setNegativeButton(rejectButtonTextId) { _, _ ->
        rejectCallback?.invoke()
    }.show()
}

/**
 * Query dialog
 * @param messageResource - message string
 */
fun Context.showYesNoDialog(messageResource: Int, confirmCallback: Function0<Unit>)
{
    showQueryDialog(messageResource, R.string.yes_capitalize, R.string.no_capitalize, confirmCallback, null)
}

/**
 * Query dialog
 * @param messageResource - message string
 */
fun Context.showYesNoDialog(messageResource: Int, confirmCallback: Function0<Unit>, rejectCallback: Function0<Unit>)
{
    showQueryDialog(messageResource, R.string.yes_capitalize, R.string.no_capitalize, confirmCallback, rejectCallback)
}

/** Message dialog  */
fun Context.showMessageDialog(
        messageId: Int,
        confirmButtonTextId: Int,
        confirmCallback: Function0<Unit>)
{
    AlertDialog.Builder(this).setMessage(App.context.getString(messageId)).setCancelable(false).setPositiveButton(confirmButtonTextId) { _, _ ->
        confirmCallback.invoke()
    }.show()
}