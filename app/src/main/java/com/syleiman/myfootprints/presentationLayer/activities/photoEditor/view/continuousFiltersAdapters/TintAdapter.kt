package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.continuousFiltersAdapters

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.SeekBar
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.controls.VerticalSeekBar
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.IPhotoEditorPresenter

/** */
class TintAdapter(
    context : Context,
    control : LinearLayout,
    presenter : IPhotoEditorPresenter,
    val redSeekBar: SeekBar,
    val greenSeekBar: SeekBar,
    val blueSeekBar: SeekBar) : AdapterBase(context, control, presenter), SeekBar.OnSeekBarChangeListener
{
    private var inDirectProgressUpdate : Boolean = false

    init
    {
        redSeekBar.setOnSeekBarChangeListener(this)
        greenSeekBar.setOnSeekBarChangeListener(this)
        blueSeekBar.setOnSeekBarChangeListener(this)
    }

    /** Init control on show */
    override fun initControl(initData: Any?) = updateProgress(initData as Int)

    /** On select tint color */
    fun onColorSelected(color : Int)
    {
        updateProgress(color)
        presenter.onContinuousControlAction(color)
    }

    /** */
    private fun updateProgress(color : Int)
    {
        inDirectProgressUpdate = true

        if(redSeekBar is VerticalSeekBar)
            redSeekBar.setProgressAndThumb(Color.red(color))
        else
            redSeekBar.progress = Color.red(color)

        if(greenSeekBar is VerticalSeekBar)
            greenSeekBar.setProgressAndThumb(Color.green(color))
        else
            greenSeekBar.progress = Color.green(color)

        if(blueSeekBar is VerticalSeekBar)
            blueSeekBar.setProgressAndThumb(Color.blue(color))
        else
            blueSeekBar.progress = Color.blue(color)

        inDirectProgressUpdate = false
    }

    /** Progress bar value change */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
    {
        // do nothing
    }

    /** On start use progress bar */
    override fun onStartTrackingTouch(seekBar: SeekBar?)
    {
        if(!inDirectProgressUpdate)
            enableOthersBars(seekBar!!, false)          // Turn off others bars
    }

    /** On complete use progress bar */
    override fun onStopTrackingTouch(seekBar: SeekBar?)
    {
        if(!inDirectProgressUpdate)
        {
            enableOthersBars(seekBar!!, true)
            presenter.onContinuousControlAction(Color.rgb(redSeekBar.progress, greenSeekBar.progress, blueSeekBar.progress))
        }
    }

    /** Lock not current bars */
    private fun enableOthersBars(currentBar : SeekBar, isEnabled: Boolean)
    {
        when(currentBar)
        {
            redSeekBar ->
            {
                greenSeekBar.isEnabled = isEnabled
                blueSeekBar.isEnabled = isEnabled
            }
            greenSeekBar ->
            {
                redSeekBar.isEnabled = isEnabled
                blueSeekBar.isEnabled = isEnabled
            }
            blueSeekBar ->
            {
                redSeekBar.isEnabled = isEnabled
                greenSeekBar.isEnabled = isEnabled
            }
        }
    }
}