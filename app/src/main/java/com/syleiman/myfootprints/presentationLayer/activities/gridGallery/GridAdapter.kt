package com.syleiman.myfootprints.presentationLayer.activities.gridGallery

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.presentationLayer.ImageLoaderOptions
import com.syleiman.myfootprints.presentationLayer.StringsHelper
import kotlinx.android.synthetic.main.cc_grid_gallery_one_cell.view.*

class GridAdapter internal constructor(context: Context, private val presenter: GridGalleryActivityPresenter) : BaseAdapter()
{
    internal class ViewHolder(val imageView: ImageView, val progressBar: ProgressBar, val dateTime: TextView, val place: TextView)

    private val usingHolders: HashSet<Int> = HashSet()

    /** */
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    /** */
    private val options: DisplayImageOptions = ImageLoaderOptions.photoGridOptions

    /** */
    override fun getCount(): Int = presenter.totalItems

    /** */
    override fun getItem(position: Int): Any? = null

    /** */
    override fun getItemId(position: Int): Long = position.toLong()

    /** */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val holder: ViewHolder
        var view: View? = convertView
        if (view == null)
        {
            view = inflater.inflate(R.layout.activity_grid_gallery_one_image, parent, false)
            holder = ViewHolder(view.image, view.progress, view.dateTime, view.place)
            view!!.tag = holder
        }
        else
            holder = view.tag as ViewHolder

        val dataItem = presenter.getDataItemByPosition(position)

        // Cancel loading if holder is reused
        val holderKey = holder.imageView.hashCode()
        if(usingHolders.contains(holderKey))
            ImageLoader.getInstance().cancelDisplayTask(holder.imageView)
        else
            usingHolders.add(holderKey)

        drawPhoto(holder.imageView, holder.progressBar, dataItem)

        holder.dateTime.text = StringsHelper.DateTimeToLocaleString(dataItem.createDateTime)

        val placeText = getPlace(dataItem)
        if (placeText != null)
            holder.place.visibility = View.VISIBLE
        else
            holder.place.visibility = View.INVISIBLE
        holder.place.text = getPlace(dataItem)

        return view
    }

    /**  */
    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    private fun drawPhoto(imageView: ImageView, progressBar: ProgressBar, dataItem: GridDataItem)
    {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        ImageLoader.getInstance().displayImage(dataItem.photoUrl, imageView, options, object : SimpleImageLoadingListener()
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
        },
        { imageUri, view1, current, total -> progressBar.progress = Math.round(100.0f * current / total) })
    }

    private fun getPlace(dataItem: GridDataItem): String?
    {
        return dataItem.cityName ?: dataItem.countryName
    }
}