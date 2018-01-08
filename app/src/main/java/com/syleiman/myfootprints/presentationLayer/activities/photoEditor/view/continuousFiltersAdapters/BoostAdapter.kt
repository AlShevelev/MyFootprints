package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.continuousFiltersAdapters

import android.content.Context
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.BoostInfo
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.ColorComponents
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.IPhotoEditorPresenter

/** */
class BoostAdapter(
    context : Context,
    control : LinearLayout,
    presenter : IPhotoEditorPresenter,
    val seekBar: SeekBar,
    redButton : Button,
    greenButton : Button,
    blueButton : Button) : AdapterBase(context, control, presenter), SeekBar.OnSeekBarChangeListener
{
    private var progressBarValue : Int = 0
    private var selectedColor : ColorComponents = ColorComponents.R

    init
    {
        redButton.setOnClickListener { onColorButtonClick(ColorComponents.R) }
        greenButton.setOnClickListener { onColorButtonClick(ColorComponents.G) }
        blueButton.setOnClickListener { onColorButtonClick(ColorComponents.B) }

        seekBar.setOnSeekBarChangeListener(this)
    }

    /** Init control on show */
    override fun initControl(initData: Any?)
    {
        val progressBarValue = (initData as BoostInfo).percent.toInt()
        seekBar.progress = progressBarValue

        this.progressBarValue = progressBarValue
        selectedColor = initData.colorComponent
    }

    /** Click on color button */
    private fun onColorButtonClick(color : ColorComponents)
    {
        selectedColor = color

        presenter.onContinuousControlAction(BoostInfo(selectedColor, progressBarValue.toFloat()))
    }

    /** Progress bar value change */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
    {
        progressBarValue = progress
    }

    /** On start use progress bar */
    override fun onStartTrackingTouch(seekBar: SeekBar?) { }        // do nothing

    /** On complete use progress bar */
    override fun onStopTrackingTouch(seekBar: SeekBar?) = presenter.onContinuousControlAction(BoostInfo(selectedColor, progressBarValue.toFloat()))
}