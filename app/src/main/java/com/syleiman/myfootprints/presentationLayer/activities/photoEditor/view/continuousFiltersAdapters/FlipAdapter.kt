package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.continuousFiltersAdapters

import android.content.Context
import android.widget.Button
import android.widget.LinearLayout
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.FlipInfo
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.IPhotoEditorPresenter

/** */
class FlipAdapter(
    context : Context,
    control : LinearLayout,
    presenter : IPhotoEditorPresenter,
    flipV : Button,
    flipH : Button) : AdapterBase(context, control, presenter)
{
    private var flipVValue = false
    private var flipHValue = false

    init
    {
        flipV.setOnClickListener { onFlipV() }
        flipH.setOnClickListener { onFlipH() }
    }

    /** Init control on show */
    override fun initControl(initData: Any?)
    {
        flipVValue = false
        flipHValue = false
    }

    /** Vertical flip */
    private fun onFlipV()
    {
        flipVValue=!flipVValue
        presenter.onContinuousControlAction(FlipInfo(flipVValue, null))
    }

    /** Horizontal flip */
    private fun onFlipH()
    {
        flipHValue=!flipHValue
        presenter.onContinuousControlAction(FlipInfo(null, flipHValue))
    }
}