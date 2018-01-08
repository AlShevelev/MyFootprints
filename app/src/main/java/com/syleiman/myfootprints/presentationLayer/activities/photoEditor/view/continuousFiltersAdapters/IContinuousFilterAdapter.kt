package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.continuousFiltersAdapters

/** Interface for control of continuous filter */
interface IContinuousFilterAdapter
{
    /** Show control
     * @param initData - init values to show
     */
    fun showControl(initData: Any?)

    /** Hide controls */
    fun hideControl()
}