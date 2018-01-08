package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.continuousFiltersAdapters

import android.content.Context
import android.widget.LinearLayout
import android.widget.SeekBar
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.RGB
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.IPhotoEditorPresenter

/** */
class GammaAdapter(
    context : Context,
    control : LinearLayout,
    presenter : IPhotoEditorPresenter,
    val redSeekBar: SeekBar,
    val greenSeekBar: SeekBar,
    val blueSeekBar: SeekBar) : AdapterBase(context, control, presenter), SeekBar.OnSeekBarChangeListener
{
    private var redProgressBarValue : Int = 0
    private var greenProgressBarValue : Int = 0
    private var blueProgressBarValue : Int = 0

    init
    {
        redSeekBar.setOnSeekBarChangeListener(this)
        greenSeekBar.setOnSeekBarChangeListener(this)
        blueSeekBar.setOnSeekBarChangeListener(this)
    }

    /** Init control on show
     * @param initData - [0; 48]*/
    @Suppress("UNCHECKED_CAST")
    override fun initControl(initData: Any?)
    {
        val initValues = initData as RGB<Double>

        val rProgressValue = initValues.R!!.toInt()
        val gProgressValue = initValues.G!!.toInt()
        val bProgressValue = initValues.B!!.toInt()

        redSeekBar.progress = rProgressValue
        greenSeekBar.progress = gProgressValue
        blueSeekBar.progress = bProgressValue

        redProgressBarValue = rProgressValue
        greenProgressBarValue = gProgressValue
        blueProgressBarValue = bProgressValue
    }

    /** Progress bar value change */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
    {
        when(seekBar!!)
        {
            redSeekBar -> redProgressBarValue = progress
            greenSeekBar -> greenProgressBarValue = progress
            blueSeekBar -> blueProgressBarValue = progress
        }
    }

    /** On start use progress bar */
    override fun onStartTrackingTouch(seekBar: SeekBar?)
    {
        enableOthersBars(seekBar!!, false)          // Turn off others bars
    }

    /** On complete use progress bar */
    override fun onStopTrackingTouch(seekBar: SeekBar?)
    {
        enableOthersBars(seekBar!!, true)
        presenter.onContinuousControlAction(RGB(redProgressBarValue.toDouble(), greenProgressBarValue.toDouble(), blueProgressBarValue.toDouble()))
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