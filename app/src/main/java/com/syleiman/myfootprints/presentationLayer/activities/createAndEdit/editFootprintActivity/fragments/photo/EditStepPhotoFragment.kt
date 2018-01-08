package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.fragments.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.FragmentBase
import com.syleiman.myfootprints.presentationLayer.ImageLoaderOptions
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.editFootprintActivity.EditFootprintActivity
import com.syleiman.myfootprints.presentationLayer.customComponents.progress.FullScreenProgress
import kotlinx.android.synthetic.main.fr_create_footprint.*
import javax.inject.Inject

/** First step of editing - photo and comment  */
class EditStepPhotoFragment
@Inject
constructor() : FragmentBase(), IEditStepPhotoFragmentCallbacks
{
    @Inject internal lateinit var presenter: EditStepPhotoFragmentPresenter
    @Inject internal lateinit var bitmapService: IBitmapService

    private var options: DisplayImageOptions? = null

    private var isPhotoLoadingComplete = false
    private var isShowed = false

    /**  */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        App.application.getEditFootprintActivityComponent(activity as EditFootprintActivity).inject(this)

        options = ImageLoaderOptions.singleImageOptions

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
        btnSave.setOnClickListener { clickOnSave() }
        closePhotoButton.setOnClickListener { clickOnClosePhoto() }
        editPhotoButton.setOnClickListener { presenter.clickOnEditPhoto() }

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

        imageFrame.visibility = View.VISIBLE

        photo.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE

        val bmp = bitmapService.load(photoFile)

        photo.setImageBitmap(bmp)
        isPhotoLoadingComplete = true
        progressBar.visibility = View.INVISIBLE
        photo.visibility = View.VISIBLE
    }

    /** Set a comment  */
    override fun setComment(comment: String)
    {
        etComment!!.setText(comment)
    }

    /** Click on map mode button  */
    fun clickOnTbMap()
    {
        if (isPhotoLoadingComplete)        // Only if photo was loaded
            presenter.switchToMap()
    }

    /** Save footprint  */
    fun clickOnSave()
    {
        if(!presenter.save())
        {
            val activity = activity          // Map is not loaded so we try to saveToPrivateArea from our fragment

            FullScreenProgress.Show(activity, false)

            presenter.updateFootprint(activity, null
            ) { isSuccess ->
                if (!isSuccess)
                    this.showMessage(R.string.message_box_cant_save_footprint)

                FullScreenProgress.Hide()
                presenter.clickOnCloseButton(false, isSuccess)
            }
        }
    }

    /** Close photo  */
    fun clickOnClosePhoto()
    {
        presenter.clickOnClosePhoto()
    }
}