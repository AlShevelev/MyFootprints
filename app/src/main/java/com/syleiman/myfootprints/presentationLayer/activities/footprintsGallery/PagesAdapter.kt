package com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter

/** Adapter for pages  */
class PagesAdapter(fm: FragmentManager, private val pagesLogic: IFootprintsGalleryActivityPresenterPages) : FragmentStatePagerAdapter(fm)
{
    /**  */
    override fun getItem(position: Int): Fragment = PageFragment.newInstance(pagesLogic.getInPosition(position))

    /**  */
    override fun getCount(): Int = pagesLogic.totalPages

    /** Simulate page deletion to avoid update page problems (from here: http://stackoverflow.com/questions/7369978/how-to-force-viewpager-to-re-instantiate-its-items) */
    override fun getItemPosition(`object`: Any?): Int = PagerAdapter.POSITION_NONE
}