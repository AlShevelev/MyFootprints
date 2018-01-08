package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.continuousFiltersAdapters

import android.content.Context
import android.widget.LinearLayout
import android.widget.SeekBar
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.IPhotoEditorPresenter

/** */
class ColorDepthAdapter(
    context : Context,
    control : LinearLayout,
    presenter : IPhotoEditorPresenter,
    val seekBar: SeekBar) : AdapterBase(context, control, presenter), SeekBar.OnSeekBarChangeListener
{
    private var progressBarValue : Int = 0
    private var oldProgressBarValue : Int = 0

    private val possibleValues : Array<Int> = arrayOf(16, 32, 64, 128)

    init
    {
        seekBar.setOnSeekBarChangeListener(this)
    }

    /** Init control on show
     * @param initData 16, 32, 64, 128 */
    override fun initControl(initData: Any?)
    {
        progressBarValue = possibleValues.indexOf(initData)         // It's more accurate than using logarithm
        oldProgressBarValue = progressBarValue

        seekBar.progress = progressBarValue
    }

    /** Progress bar value change */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
    {
        progressBarValue = progress
    }

    /** On start use progress bar */
    override fun onStartTrackingTouch(seekBar: SeekBar?) { }        // do nothing

    /** On complete use progress bar */
    override fun onStopTrackingTouch(seekBar: SeekBar?)
    {
        if(oldProgressBarValue!=progressBarValue)
        {
            oldProgressBarValue = progressBarValue
            presenter.onContinuousControlAction(possibleValues[progressBarValue])
        }
    }
}