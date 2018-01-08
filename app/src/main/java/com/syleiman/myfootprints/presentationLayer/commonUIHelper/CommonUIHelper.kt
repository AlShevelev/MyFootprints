package com.syleiman.myfootprints.presentationLayer.commonUIHelper

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


/** */
class CommonUIHelper : ICommonUIHelper
{
    /**
     * @view some view (not important what exactly)
     */
    override fun closeSoftKeyboard(view : View, context : Context)
    {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}