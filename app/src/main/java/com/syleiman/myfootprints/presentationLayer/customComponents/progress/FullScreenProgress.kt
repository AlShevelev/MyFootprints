package com.syleiman.myfootprints.presentationLayer.customComponents.progress

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager

import com.syleiman.myfootprints.R

/** Full-screen progressBar with blocking user input  */
object FullScreenProgress
{
    private var dialog: ProgressDialog? = null

    /**  */
    fun Show(activity: Activity, isFulScreen : Boolean)
    {
        if (dialog != null)
            return

        dialog = object : ProgressDialog(activity, R.style.full_screen_dialog)
        {
            override fun onCreate(savedInstanceState: Bundle?)
            {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.progressl_dialog)

                window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                if(isFulScreen)     // Full-screen mode
                    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }

        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    /**  */
    fun Hide()
    {
        if (dialog == null)
            return

        dialog!!.dismiss()
        dialog = null
    }
}