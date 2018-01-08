package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.fragments.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.FragmentBase
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.createFootprintActivity.CreateFootprintActivity
import com.syleiman.myfootprints.presentationLayer.customComponents.progress.FullScreenProgress
import kotlinx.android.synthetic.main.fr_create_footprint.*
import javax.inject.Inject

/**
 * Second step - create footprint
 */
class CreateStepPhotoFragment
@Inject
constructor() : FragmentBase(), ICreateStepPhotoFragmentCallbacks
{
    @Inject internal lateinit var presenter: CreateStepPhotoFragmentPresenter
    @Inject internal lateinit var bitmapService: IBitmapService

    private var isPhotoLoadingComplete = false
    private var isShowed = false

    /**  */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        App.application.getCreateFootprintActivityComponent(activity as CreateFootprintActivity).inject(this)
        super.onCreate(savedInstanceState)
    }

    /**  */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater!!.inflate(R.layout.fr_create_footprint, container, false)
    }

    /**  */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        etComment.setTextChangeListener { text -> this.onCommentLenChanged(text) }
        tbMap.setOnClickListener { clickOnTbMap() }
        closePhotoButton.setOnClickListener { presenter.clickOnClosePhoto() }
        editPhotoButton.setOnClickListener { presenter.clickOnEditPhoto() }
        btnSave.setOnClickListener { clickOnSave() }

        presenter.init()
    }

    /** When comment text changed  */
    private fun onCommentLenChanged(text: String?)
    {
        presenter.storeComment(text) { saveButtonEnabled ->
            btnSave.isEnabled = saveButtonEnabled
        }
    }

    /**  */
    override fun onHiddenChanged(hidden: Boolean)
    {
        if (!hidden)
            updateButtonsState()

        super.onHiddenChanged(hidden)
    }

    /**  */
    override fun onResume()
    {
        updateButtonsState()
        super.onResume()
    }

    /**  */
    private fun updateButtonsState()
    {
        isShowed = false
        btnSave.isEnabled = presenter.getSaveButtonState()
        isShowed = true
    }

    /** Show photo  */
    override fun showPhoto(photoFile: IFileSingleOperation)
    {
        isPhotoLoadingComplete = false

        imageFrame!!.visibility = View.VISIBLE

        photo!!.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE

        val bmp = bitmapService.load(photoFile)

        photo!!.setImageBitmap(bmp)
        isPhotoLoadingComplete = true
        progressBar.visibility = View.INVISIBLE
        photo!!.visibility = View.VISIBLE
    }

    /** Click on map mode button  */
    fun clickOnTbMap()
    {
        if (isPhotoLoadingComplete)
            presenter.switchToMap()
    }

    /** Save footprint  */
    fun clickOnSave()
    {
        if(!presenter.save())           // Map is not loaded so we try to saveToPrivateArea from our fragment
        {
            val activity = activity

            FullScreenProgress.Show(activity, false)

            presenter.createFootprint(activity, null) { isSuccess ->
                if (!isSuccess)
                    this.showMessage(R.string.message_box_cant_save_footprint)

                FullScreenProgress.Hide()
                presenter.clickOnCloseButton(false)
            }
        }
    }
}