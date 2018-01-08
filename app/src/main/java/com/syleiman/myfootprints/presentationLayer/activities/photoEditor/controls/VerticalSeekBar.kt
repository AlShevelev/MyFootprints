package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.controls

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.SeekBar

class VerticalSeekBar : SeekBar
{
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int)
    {
        super.onSizeChanged(h, w, oldH, oldW)
    }

    @Synchronized override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(c: Canvas)
    {
        c.rotate(-90f)
        c.translate((-height).toFloat(), 0f)

        super.onDraw(c)
    }

    private var onChangeListener: OnSeekBarChangeListener? = null

    override fun setOnSeekBarChangeListener(onChangeListener: OnSeekBarChangeListener)
    {
        this.onChangeListener = onChangeListener
    }

    private var lastProgress = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        if (!isEnabled)
        {
            return false
        }

        when (event.action)
        {
            MotionEvent.ACTION_DOWN ->
            {
                onChangeListener!!.onStartTrackingTouch(this)
                isPressed = true
                isSelected = true
            }
            MotionEvent.ACTION_MOVE ->
            {
                super.onTouchEvent(event)
                var progress = max - (max * event.y / height).toInt()

                // Ensure progress stays within boundaries
                if (progress < 0)
                {
                    progress = 0
                }
                if (progress > max)
                {
                    progress = max
                }
                setProgress(progress)  // Draw progress
                if (progress != lastProgress)
                {
                    // Only enact listener if the progress has actually changed
                    lastProgress = progress
                    onChangeListener!!.onProgressChanged(this, progress, true)
                }


                onSizeChanged(width, height, 0, 0)
                isPressed = true
                isSelected = true
            }
            MotionEvent.ACTION_UP ->
            {
                onChangeListener!!.onStopTrackingTouch(this)
                isPressed = false
                isSelected = false
            }
            MotionEvent.ACTION_CANCEL ->
            {
                super.onTouchEvent(event)
                isPressed = false
                isSelected = false
            }
        }
        return true
    }

    @Synchronized fun setProgressAndThumb(progress: Int)
    {
        setProgress(progress)
        onSizeChanged(width, height, 0, 0)
        if (progress != lastProgress)
        {
            // Only enact listener if the progress has actually changed
            lastProgress = progress
            onChangeListener!!.onProgressChanged(this, progress, true)
        }
    }
}