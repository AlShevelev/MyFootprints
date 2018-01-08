package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.continuousFiltersAdapters

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.IPhotoEditorPresenter

/** */
abstract class AdapterBase(
    protected val context : Context,
    val control : LinearLayout,
    var presenter : IPhotoEditorPresenter)  : IContinuousFilterAdapter
{
    /** Hide controls */
    final override fun hideControl()
    {
        control.visibility = View.GONE
    }

    /** Show control
     * @param initData - init values to show
     */
    final override fun showControl(initData: Any?)
    {
        initControl(initData)
        control.visibility = View.VISIBLE
    }

    /** Init control on show */
    protected abstract fun initControl(initData: Any?)
}