package com.syleiman.myfootprints.presentationLayer.customComponents.editTextTwitter

import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan

/** Mark last chars in EditText by color  */
class MarkLastCharsTextWatcher(
        private val maxUnmarkedTextLen: Int,
        extraTextBackColor: Int,
        private val onTextChanged: Function1<String, Unit>) : TextWatcher
{
    private var currentSpan: Any? = null
    private val spanToUse: Any

    init
    {
        this.spanToUse = BackgroundColorSpan(extraTextBackColor)
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int)
    {

    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) = onTextChanged.invoke(charSequence.toString())

    override fun afterTextChanged(editable: Editable)
    {
        if (currentSpan != null)
        {
            editable.removeSpan(currentSpan)
            currentSpan = null
        }

        val currentTextLen = editable.length
        if (currentTextLen > maxUnmarkedTextLen)
        {
            currentSpan = spanToUse
            editable.setSpan(currentSpan, maxUnmarkedTextLen, currentTextLen, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }
}
