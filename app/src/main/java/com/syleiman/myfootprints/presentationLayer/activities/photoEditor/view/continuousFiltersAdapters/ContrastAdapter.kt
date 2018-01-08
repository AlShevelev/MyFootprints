package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.continuousFiltersAdapters

import android.content.Context
import android.widget.LinearLayout
import android.widget.SeekBar
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.IPhotoEditorPresenter

/** */
class ContrastAdapter(
    context : Context,
    control : LinearLayout,
    presenter : IPhotoEditorPresenter,
    val seekBar: SeekBar) : AdapterBase(context, control, presenter), SeekBar.OnSeekBarChangeListener
{
    private var progressBarValue : Int = 0

    init
    {
        seekBar.setOnSeekBarChangeListener(this)
    }

    /** Init control on show
     * @param initData - [-50; +50]*/
    override fun initControl(initData: Any?)
    {
        val progressBarValue = (initData as Double).toInt()+50

        seekBar.progress = progressBarValue

        this.progressBarValue = progressBarValue
    }

    /** Progress bar value change */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
    {
        progressBarValue = progress
    }

    /** On start use progress bar */
    override fun onStartTrackingTouch(seekBar: SeekBar?) { }        // do nothing

    /** On complete use progress bar */
    override fun onStopTrackingTouch(seekBar: SeekBar?) = presenter.onContinuousControlAction((progressBarValue-50).toDouble())
}