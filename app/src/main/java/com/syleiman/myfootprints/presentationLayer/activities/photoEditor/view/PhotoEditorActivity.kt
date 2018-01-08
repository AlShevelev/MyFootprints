package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.SeekBar
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.common.files.IFileSingleOperation
import com.syleiman.myfootprints.presentationLayer.activities.InternalActivitiesCodes
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model.Filters
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.presenter.IPhotoEditorPresenter
import com.syleiman.myfootprints.presentationLayer.activities.photoEditor.view.continuousFiltersAdapters.*
import com.syleiman.myfootprints.presentationLayer.customComponents.progress.FullScreenProgress
import com.syleiman.myfootprints.presentationLayer.showQueryDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_photo_editor.*
import kotlinx.android.synthetic.main.photo_edit_holder_apply_filter.*
import kotlinx.android.synthetic.main.photo_edit_holder_boost.*
import kotlinx.android.synthetic.main.photo_edit_holder_brightness.*
import kotlinx.android.synthetic.main.photo_edit_holder_cbalance.*
import kotlinx.android.synthetic.main.photo_edit_holder_cdepth.*
import kotlinx.android.synthetic.main.photo_edit_holder_contrast.*
import kotlinx.android.synthetic.main.photo_edit_holder_filters.*
import kotlinx.android.synthetic.main.photo_edit_holder_flip.*
import kotlinx.android.synthetic.main.photo_edit_holder_gamma.*
import kotlinx.android.synthetic.main.photo_edit_holder_hue.*
import kotlinx.android.synthetic.main.photo_edit_holder_saturation.*
import kotlinx.android.synthetic.main.photo_edit_holder_save.*
import kotlinx.android.synthetic.main.photo_edit_holder_tint.*
import javax.inject.Inject

class PhotoEditorActivity : AppCompatActivity(), IPhotoEditorView
{
    @Inject internal lateinit var presenter : IPhotoEditorPresenter
    @Inject internal lateinit var bitmapService : IBitmapService

    private lateinit var continuousFiltersAdapters: Map<Filters, IContinuousFilterAdapter>

    object Consts
    {
        var PATH_TO_RESULT_FILE = "PATH_TO_RESULT_FILE"
        var PATH_TO_SOURCE_FILE = "PATH_TO_SOURCE_FILE"
    }

    companion object
    {
        /** Start this activity   */
        fun start(parentActivity: Activity, photo: IFileSingleOperation)
        {
            val intent = Intent(parentActivity, PhotoEditorActivity::class.java)
            intent.putExtra(Consts.PATH_TO_SOURCE_FILE, photo.getPath())
            parentActivity.startActivityForResult(intent, InternalActivitiesCodes.PhotoEditor.value)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)         // Full-screen mode

        setContentView(R.layout.activity_photo_editor)

        App.application.getPhotoEditorActivityComponent().inject(this)

        continuousFiltersAdapters = mapOf(              // Create adapters for continuous controls
            Pair(Filters.Boost, BoostAdapter(this, holder_boost, presenter, boost_value as SeekBar, boost_red, boost_green, boost_blue)),
            Pair(Filters.Brightness, BrightnessAdapter(this, holder_brightness, presenter, brightness_value as SeekBar)),
            Pair(Filters.ColorDepth, ColorDepthAdapter(this, holder_cdepth, presenter, cdepth_value as SeekBar)),
            Pair(Filters.ColorBalance, ColorBalanceAdapter(this, holder_cbalance, presenter, (bRed_value as SeekBar), (bGreen_value as SeekBar), (bBlue_value as SeekBar))),
            Pair(Filters.Contrast, ContrastAdapter(this, holder_contrast, presenter, contrast_value as SeekBar)),
            Pair(Filters.Flip, FlipAdapter(this, holder_flip, presenter, flip_v, flip_h)),
            Pair(Filters.Gamma, GammaAdapter(this, holder_gamma, presenter, (gRed_value as SeekBar), (gGreen_value as SeekBar), (gBlue_value as SeekBar))),
            Pair(Filters.Hue, HueAdapter(this, holder_hue, presenter, hue_value as SeekBar)),
            Pair(Filters.Saturation, SaturationAdapter(this, holder_saturation, presenter, sat_value as SeekBar)),
            Pair(Filters.Tint, TintAdapter(this, holder_tint, presenter, (tRed_value as SeekBar), (tGreen_value as SeekBar), (tBlue_value as SeekBar))))

        presenter.attachView(this)
        if (savedInstanceState == null)
            loadImage()
        else
        {
            presenter.initAfterRestart(this)
            source_image.setImageBitmap(presenter.getLastBitmap())          // Get from model
        }
    }

    /** Load image first time and init presenter */
    private fun loadImage()
    {
        toolbox.visibility = GONE               // Hide all filters, buttons and so on
        setProgressVisibility(true)

        Observable.fromCallable {
            bitmapService.load(FileSingle.fromPath(intent.getStringExtra(Consts.PATH_TO_SOURCE_FILE)))
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({loadResult ->
                run {
                    source_image.setImageBitmap(loadResult)
                    presenter.setSource(loadResult!!)

                    toolbox.visibility = VISIBLE               // Hide all filters, buttons and so on
                    setProgressVisibility(false)
                }
            },
            { e ->                          // Error
                e.printStackTrace()
                throw e
            })
    }

    /** Show and hide progress */
    override fun setProgressVisibility(isVisible: Boolean)
    {
        if(isVisible)
            FullScreenProgress.Show(this, true)
        else
            FullScreenProgress.Hide()
    }

    /** Show and hide filters */
    override fun setFiltersVisibility(isVisible: Boolean)
    {
        effects_holder.visibility = if (isVisible) VISIBLE else GONE
    }

    /** Show controls for continuous filter
     * @param values - init values for filter */
    override fun showControls(filter: Filters, values: Any?) = continuousFiltersAdapters[filter]!!.showControl(values)

    /** Hide controls for continuous filter */
    override fun hideControls(filter: Filters) = continuousFiltersAdapters[filter]!!.hideControl()

    /** Show and hide Ok and Cancel buttons */
    override fun setOkCancelButtonsVisibility(isVisible: Boolean)
    {
        effect_set_box.visibility = if (isVisible) VISIBLE else GONE
    }

    /** Show and hide Undo and Accept buttons */
    override fun setUndoAcceptButtonsVisibility(isVisible: Boolean)
    {
        btn_holder.visibility = if (isVisible) VISIBLE else GONE
    }

    /** Update image by new bitmap */
    override fun updateImage(image: Bitmap)
    {
        source_image.setImageBitmap(image)
    }

    /** Update image by new bitmap */
    override fun updateCropImage(image: Bitmap)
    {
        crop_source_image.imageBitmap = image
    }

    /** Show and hide image */
    override fun setImageVisibility(isVisible: Boolean)
    {
        source_image.visibility = if (isVisible) VISIBLE else GONE
    }

    /** Show and hide image */
    override fun setCropImageVisibility(isVisible: Boolean)
    {
        crop_source_image.visibility = if (isVisible) VISIBLE else GONE
    }

    /** Show close dialog */
    override fun showCloseDialog() = showQueryDialog(
        R.string.photo_edit_exit_prompt,
        R.string.photo_edit_yes,
        R.string.photo_edit_no,
        {presenter.onYesCloseButton()},
        {presenter.onNoCloseButton()})

    /** User accepts editing
     * @param pathToBitmap - path to bitmap file on disk*/
    override fun workAccepted(pathToBitmap : String)
    {
        val processingResult = Intent().apply {
            putExtra(Consts.PATH_TO_RESULT_FILE, pathToBitmap)
        }
        setResult(Activity.RESULT_OK, processingResult)
        finish()
    }

    /** User cancels editing */
    override fun workCanceled()
    {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    /**  */
    override fun onDestroy()
    {
        super.onDestroy()

        if(isFinishing)
            App.application.releasePhotoEditorActivityComponent()
        else
            presenter.resetBeforeRestart()           // Rotation

        presenter.detachView()
    }


    /** Tap on source image */
    @Suppress("unused")
    fun onSourceImageClick(@Suppress("UNUSED_PARAMETER") view: View)
    {
        presenter.onTapOnImage()
    }

    /** On back button pressed */
    override fun onBackPressed()
    {
        // super.onBackPressed()
        presenter.onBackPressed()
    }

    /**Tint color was selected */
    @Suppress("unused")
    fun applyTint(view: View)
    {               // It's sad, but necessary :)
        (continuousFiltersAdapters[Filters.Tint]!! as TintAdapter).onColorSelected(Color.parseColor(view.tag.toString()))
    }

    /** Set continuous effect */
    @Suppress("unused")
    fun setSelectedEffect(@Suppress("UNUSED_PARAMETER") view: View) = presenter.onOkContinuousButton()

    /** Cancel continuous effect */
    @Suppress("unused")
    fun cancelSelectedEffect(@Suppress("UNUSED_PARAMETER") view: View) = presenter.onCancelContinuousButton()

    /** On click Undo button */
    @Suppress("unused")
    fun onClickUndo(@Suppress("UNUSED_PARAMETER") view: View) = presenter.onUndoButton()

    /** On click Save button */
    @Suppress("unused")
    fun onClickSave(@Suppress("UNUSED_PARAMETER") view: View) = presenter.onAcceptButton()

    /** Click on filter button */
    @Suppress("unused")
    fun onClickEffectButton(view: View) = presenter.onChooseFilter(Filters.from(view.tag.toString()))

    /** Get cropped bitmap */
    override fun getCroppedBitmap(): Bitmap = crop_source_image.croppedBitmap
}