package com.syleiman.myfootprints.presentationLayer

import android.content.Context
import android.view.Gravity
import android.widget.Toast

/** Helper function for toasts */
object ToastsParams
{
    enum class Duration
    {
        Long,
        Short
    }

    enum class Position
    {
        Bottom,
        Center,
        Top
    }
}

fun Context.showToast(text: CharSequence, duration: ToastsParams.Duration, position: ToastsParams.Position)
{
    val toast = Toast.makeText(this, text, if (duration == ToastsParams.Duration.Long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)

    var gravity = 0
    when (position)
    {
        ToastsParams.Position.Bottom -> gravity = Gravity.BOTTOM
        ToastsParams.Position.Center -> gravity = Gravity.CENTER
        ToastsParams.Position.Top -> gravity = Gravity.TOP
    }

    toast.setGravity(gravity, 0, 0)

    toast.show()
}

fun Context.showToast(textResId: Int, duration: ToastsParams.Duration, position: ToastsParams.Position)
{
    showToast(this.resources.getText(textResId), duration, position)
}

fun Context.showToast(textResId: Int, position: ToastsParams.Position)
{
    showToast(textResId, ToastsParams.Duration.Short, position)
}

fun Context.showToast(text: CharSequence, position: ToastsParams.Position)
{
    showToast(text, ToastsParams.Duration.Short, position)
}