package com.syleiman.myfootprints.presentationLayer.customComponents.editTextTwitter

import android.content.Context
import android.graphics.Color
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.syleiman.myfootprints.R

/** Component for edit text in twitter-style */
class EditTextTwitter : LinearLayout
{
    private var mainTextEdit: EditText? = null
    private var twitterLenLabel: TextView? = null

    private var currentTextLen = 0

    // Attributes
    private var hint: String? = null                   // Text hint for EditText
    private lateinit var twitterLenLabelTemplate: String
    private var unmarkedTextLen: Int = 0               // max len of unmarked text
    private var maxLines: Int = 0
    private var maxTextLen: Int = 0
    private var textInRangeLabelColor: Int = 0
    private var textOutOfRangeLabelColor: Int = 0
    private var extraTextBackColor: Int = 0

    /** Listener of text changing  */
    private lateinit var textChangeListener: (String?) -> Unit

    /**  */
    constructor(context: Context) : super(context)
    {
        initComponent()
    }

    /**  */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        initAttributes(context, attrs)
        initComponent()
    }

    /** Set of listener for text len changing  */
    fun setTextChangeListener(listener: (String?) -> Unit)
    {
        textChangeListener = listener
    }

    /** Init custom attributes  */
    private fun initAttributes(context: Context, attrs: AttributeSet)
    {
        hint = ""           // Default values
        twitterLenLabelTemplate = "Twitter: %1\$s / %2\$s"
        unmarkedTextLen = 140
        maxLines = 5
        maxTextLen = 500
        textInRangeLabelColor = Color.DKGRAY
        textOutOfRangeLabelColor = Color.RED
        extraTextBackColor = Color.parseColor("#ADD0FF")           // Light blue

        val a = context.obtainStyledAttributes(attrs, R.styleable.EditTextTwitter)

        val totalAttributes = a.indexCount
        (0..totalAttributes - 1)
            .map { a.getIndex(it) }
            .forEach {
                when (it)
                {
                    R.styleable.EditTextTwitter_hint -> hint = a.getString(it)
                    R.styleable.EditTextTwitter_twitterLenLabelTemplate -> twitterLenLabelTemplate = a.getString(it)
                    R.styleable.EditTextTwitter_unmarkedTextLen -> unmarkedTextLen = a.getInteger(it, 0)
                    R.styleable.EditTextTwitter_maxLines -> maxLines = a.getInteger(it, 0)
                    R.styleable.EditTextTwitter_maxTextLen -> maxTextLen = a.getInteger(it, 0)
                    R.styleable.EditTextTwitter_textInRangeLabelColor -> textInRangeLabelColor = a.getInteger(it, 0)
                    R.styleable.EditTextTwitter_textOutOfRangeLabelColor -> textOutOfRangeLabelColor = a.getInteger(it, 0)
                    R.styleable.EditTextTwitter_extraTextBackColor -> extraTextBackColor = a.getInteger(it, 0)
                }
            }
        a.recycle()
    }

    /**   */
    private fun initComponent()
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cc_edit_text_twitter, this)

        mainTextEdit = findViewById(R.id.etMain) as EditText
        twitterLenLabel = findViewById(R.id.tvTwitterLen) as TextView

        mainTextEdit!!.addTextChangedListener(MarkLastCharsTextWatcher(unmarkedTextLen, extraTextBackColor,
        {
            currentTextLen = it.length                // Text change listener
            this.updateTwitterLabel()

            textChangeListener.invoke(it)
        }))

        mainTextEdit!!.hint = hint
        mainTextEdit!!.maxLines = maxLines
        mainTextEdit!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxTextLen))

        updateTwitterLabel()
    }

    /**  */
    private fun updateTwitterLabel()
    {
        twitterLenLabel!!.text = String.format(twitterLenLabelTemplate, currentTextLen, unmarkedTextLen)

        if (currentTextLen > unmarkedTextLen)
            twitterLenLabel!!.setTextColor(textOutOfRangeLabelColor)
        else
            twitterLenLabel!!.setTextColor(textInRangeLabelColor)
    }

    /** Get text from control */
    fun getText(): String = mainTextEdit!!.text.toString()
    /** Set text in control */
    fun setText(text: String) = mainTextEdit!!.setText(text)
}