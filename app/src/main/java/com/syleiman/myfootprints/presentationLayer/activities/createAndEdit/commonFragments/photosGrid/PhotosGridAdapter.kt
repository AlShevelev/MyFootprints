package com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.ImageLoaderOptions
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.gridCellsHandlers.PhotoGridCellHandlerBase
import com.syleiman.myfootprints.presentationLayer.activities.createAndEdit.commonFragments.photosGrid.gridCellsHandlers.PhotoGridCellSchema
import kotlinx.android.synthetic.main.cc_create_edit_grid_cell.view.*

/** Adapter for PhotosGridFragment */
internal class PhotosGridAdapter(context: Context, private val photosUrls: List<String>, private val gridSchema: PhotoGridCellSchema) : BaseAdapter()
{
    class ViewHolder(val imageView: ImageView, val progressBar: ProgressBar)
    {
        init
        {
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val options: DisplayImageOptions = ImageLoaderOptions.photoGridOptions
    private val usingHolders: HashSet<Int> = HashSet()

    override fun getCount(): Int
    {
        return photosUrls.size + 2
    }

    override fun getItem(position: Int): Any?
    {
        return null
    }

    override fun getItemId(position: Int): Long
    {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val holder: ViewHolder
        var view: View? = convertView
        if (view == null)
        {
            view = inflater.inflate(R.layout.item_image_grid, parent, false)
            holder = ViewHolder(view.image, view.progress)
            view.tag = holder
        }
        else
            holder = view.tag as ViewHolder

        val cellHandler = gridSchema.getHandler(position)
        val cellType = cellHandler.getCellType()

        // Cancel loading if holder is reused
        val holderKey = holder.imageView.hashCode()
        if(usingHolders.contains(holderKey))
            ImageLoader.getInstance().cancelDisplayTask(holder.imageView)
        else
            usingHolders.add(holderKey)

        when (cellType)
        {
            PhotoGridCellHandlerBase.CellType.Camera -> drawCamera(holder.imageView, holder.progressBar)
            PhotoGridCellHandlerBase.CellType.Gallery -> drawGallery(holder.imageView, holder.progressBar)
            PhotoGridCellHandlerBase.CellType.Photo -> drawPhoto(holder.imageView, holder.progressBar, position - cellHandler.getCellOffset())
        }

        return view!!
    }

    /**  */
    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    private fun drawPhoto(imageView: ImageView, progressBar: ProgressBar, photoIndex: Int)
    {
        ImageLoader.getInstance().displayImage(photosUrls[photoIndex], imageView, options, object : SimpleImageLoadingListener()
        {
            override fun onLoadingStarted(imageUri: String?, view: View?)
            {
                progressBar.progress = 0
                progressBar.visibility = View.VISIBLE
            }

            override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?)
            {
                progressBar.visibility = View.GONE
                usingHolders.remove(imageView.hashCode())
            }

            override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?)
            {
                progressBar.visibility = View.GONE
                usingHolders.remove(imageView.hashCode())
            }
        }
        ) { imageUri, view1, current, total -> progressBar.progress = Math.round(100.0f * current / total) }
    }

    /**  */
    private fun drawCamera(imageView: ImageView, progressBar: ProgressBar)
    {
        imageView.scaleType = ImageView.ScaleType.CENTER
        imageView.setImageResource(R.drawable.ic_photo_camera_black_48dp)
        progressBar.visibility = View.INVISIBLE
        usingHolders.remove(imageView.hashCode())
    }

    /**  */
    private fun drawGallery(imageView: ImageView, progressBar: ProgressBar)
    {
        imageView.scaleType = ImageView.ScaleType.CENTER
        imageView.setImageResource(R.drawable.ic_photo_library_black_48dp)
        progressBar.visibility = View.INVISIBLE
        usingHolders.remove(imageView.hashCode())
    }
}