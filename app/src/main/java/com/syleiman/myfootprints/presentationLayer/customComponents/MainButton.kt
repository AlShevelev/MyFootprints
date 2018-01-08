package com.syleiman.myfootprints.presentationLayer.customComponents

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.syleiman.myfootprints.R

/** One button for main activity  */
class MainButton : LinearLayout
{
    private var buttonIcon: ImageView? = null
    private var buttonText: TextView? = null

    /**  */
    constructor(context: Context) : super(context)
    {
        initComponent()
    }

    /**  */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        initComponent()
        initAttributes(context, attrs)
    }

    /**   */
    private fun initComponent()
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cc_main_button, this)

        buttonIcon = findViewById(R.id.buttonIcon) as ImageView
        buttonText = findViewById(R.id.buttonText) as TextView
    }

    /** Init custom attributes  */
    private fun initAttributes(context: Context, attrs: AttributeSet)
    {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MainButton)

        val totalAttributes = a.indexCount
        (0..totalAttributes - 1)
            .map { a.getIndex(it) }
            .forEach {
                when (it)
                {
                    R.styleable.MainButton_mainButtonText -> buttonText!!.text = a.getString(it)
                    R.styleable.MainButton_mainButtonIcon ->
                    {
                        val icon = a.getDrawable(it)
                        if (icon != null)
                            buttonIcon!!.setImageDrawable(icon)
                    }
                }
            }
        a.recycle()
    }
}