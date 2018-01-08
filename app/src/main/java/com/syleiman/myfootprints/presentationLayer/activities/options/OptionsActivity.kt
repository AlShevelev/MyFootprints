package com.syleiman.myfootprints.presentationLayer.activities.options

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.syleiman.myfootprints.BuildConfig
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.activities.ActivityBase
import kotlinx.android.synthetic.main.activity_options.*
import javax.inject.Inject

class OptionsActivity : ActivityBase()
{
    @Inject internal lateinit var presenter: OptionsActivityPresenter

    companion object
    {
        /** Start this activity   */
        fun start(parentActivity: Activity)
        {
            val intent = Intent(parentActivity, OptionsActivity::class.java)
            parentActivity.startActivity(intent)
        }
    }

    /**  */
    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        App.application.getOptionsActivityComponent().inject(this)

        syncOnlyViaWifiOptions.isChecked = presenter.syncOnlyViaWifiOptions
        syncOnlyViaWifiOptions.setOnCheckedChangeListener { compoundButton, isChecked ->  presenter.syncOnlyViaWifiOptions = isChecked}

        version_label.text = BuildConfig.VERSION_NAME

        //button2.setOnClickListener { App.startSync() }
    }

    /**  */
    override fun onDestroy()
    {
        App.application.releaseOptionsActivityComponent()
        super.onDestroy()
    }
}
