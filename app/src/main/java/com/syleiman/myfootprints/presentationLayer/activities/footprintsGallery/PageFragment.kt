package com.syleiman.myfootprints.presentationLayer.activities.footprintsGallery

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.common.files.FileSingle
import com.syleiman.myfootprints.modelLayer.dto.FootprintForGalleryDto
import com.syleiman.myfootprints.presentationLayer.StringsHelper

/** Fragment for one page  */
class PageFragment : Fragment()
{
    private var footprint: FootprintForGalleryDto? = null

    companion object
    {
        internal val arg_footprint_data = "arg_footprint_data"

        /** Create new instance of fragment  */
        fun newInstance(footprint: FootprintForGalleryDto): PageFragment
        {
            val pageFragment = PageFragment()
            val arguments = Bundle()
            arguments.putParcelable(arg_footprint_data, footprint)
            pageFragment.arguments = arguments
            return pageFragment
        }
    }

    /**  */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        footprint = arguments.getParcelable<FootprintForGalleryDto>(arg_footprint_data)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater!!.inflate(R.layout.fr_gallery_one_page, null)

        val photo = view.findViewById(R.id.photo_gallery) as ImageView
        val snapshot = view.findViewById(R.id.snapshot_gallery) as ImageView
        val comment = view.findViewById(R.id.comment_gallery) as TextView
        val location = view.findViewById(R.id.lat_long_gallery) as TextView
        val place = view.findViewById(R.id.place_gallery) as TextView
        val createDateTime = view.findViewById(R.id.datetime_gallery) as TextView

        displayImage(footprint!!.photoFileName, photo)

        if (footprint!!.mapSnapshotFileName != null)
            displayImage(footprint!!.mapSnapshotFileName!!, snapshot)

        val locationText = StringsHelper.LocationToLocaleString(footprint!!.location)
        val placeText = getPlaceText()

        comment.text = footprint!!.comment
        location.text = locationText
        place.text = placeText ?: locationText

        createDateTime.text = StringsHelper.DateTimeToLocaleString(footprint!!.createDateTime)

        return view
    }

    /**  */
    private fun displayImage(imageFileName: String, controlToDisplay: ImageView)
    {
        val bmp = BitmapFactory.decodeFile(FileSingle.withName(imageFileName).inPrivate().getPath())
        controlToDisplay.setImageBitmap(bmp)
    }

    private fun getPlaceText(): String?
    {
        if (footprint!!.countryName == null && footprint!!.cityName == null)
            return null

        if (footprint!!.countryName == null)
            return footprint!!.cityName

        if (footprint!!.cityName == null)
            return footprint!!.countryName

        return footprint!!.cityName + " / " + footprint!!.countryName
    }
}
