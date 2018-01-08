package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

import com.syleiman.myfootprints.R

/** One cell in create/edit grid  */
class PhotosGridCellControl : FrameLayout
{
    /**  */
    constructor(context: Context) : super(context)
    {
        initComponent()
    }

    /**  */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        initComponent()
    }

    /**   */
    private fun initComponent()
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cc_create_edit_grid_cell, this)
    }

    /** To create square cells  */
    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
