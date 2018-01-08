package com.syleiman.myfootprints.presentationLayer.activities.myWorld.googleMap

//import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.businessLayer.bitmapService.IBitmapService
import com.syleiman.myfootprints.businessLayer.sysInfoService.ISystemInformationService
import com.syleiman.myfootprints.common.files.FileSingle

/** Custom renderer for markers  */
internal class MarkersRenderer(
    private val context: Context,
    private val bitmapService: IBitmapService,
    private val sysInfo : ISystemInformationService,
    map: GoogleMap,
    clusterManager: ClusterManager<MarkerBase>) : DefaultClusterRenderer<MarkerBase>(context, map, clusterManager)
{
    private companion object Constants
    {
        val MAX_MARKER_WIDTH = 0.25f                // Percent of screen size in pixels
        val MAX_MARKER_TEXT_LINES = 3
    }

    private val generator : IconGenerator = IconGenerator(context).apply { setStyle(IconGenerator.STYLE_WHITE) }

    /** */
    override fun onBeforeClusterItemRendered(item: MarkerBase?, markerOptions: MarkerOptions?)
    {
        val image = ImageView(context).apply {
            setImageBitmap(createMarketBitmap(item!!))
        }
        generator.setContentView(image)
        markerOptions!!.icon(BitmapDescriptorFactory.fromBitmap(generator.makeIcon()))
    }

    /** Create source bitmap for marker */
    private fun createMarketBitmap(item: MarkerBase) : Bitmap
    {
        val marker = item as DefaultMarker
        val textBitmap = textToBitmap(marker)           // Create bitmap for text part
        return createMarker(marker, textBitmap)
    }

    /** Create bitmap bases on marker text */
    private fun textToBitmap(marker: DefaultMarker) : Bitmap
    {
        val text = TextView(context).apply {
            gravity = Gravity.CENTER_VERTICAL
            maxLines = Constants.MAX_MARKER_TEXT_LINES
            maxWidth = getMarkerBitmapWidth()
            ellipsize = TextUtils.TruncateAt.END
            text = marker.description
            setTextColor(App.getColorRes(R.color.darkGray))
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_LOW
            textSize = 12f
        }

        val measureSpec = View.MeasureSpec.makeMeasureSpec(0, 0)
        text.measure(measureSpec, measureSpec)
        val measuredWidth = text.measuredWidth
        val measuredHeight = text.measuredHeight

        text.layout(0, 0, measuredWidth, measuredHeight)

        text.isDrawingCacheEnabled = true
        return text.drawingCache
    }

    /** */
    private fun createMarker(marker: DefaultMarker, textBitmap : Bitmap) : Bitmap
    {
        val resultWidth : Int = maxOf(textBitmap.width, getMarkerBitmapWidth())

        val photoFile = FileSingle.withName(marker.footprintPhotoFileName).inPrivate()
        val thumbnailFile = FileSingle.fromExistWithPrefix(photoFile, FileSingle.THUMBNAIL_FILES_PREFIX)

        if(!thumbnailFile.isExists())
            bitmapService.createThumbnail(photoFile, thumbnailFile)

        val photoBitmap = bitmapService.loadAndInscribeQuick(thumbnailFile, resultWidth, 0)

        val photoBitmapDrawWidth : Float = resultWidth.toFloat()
        val photoBitmapDrawHeight : Float = (photoBitmap.height * (photoBitmapDrawWidth /photoBitmap.width))

        val result = Bitmap.createBitmap(resultWidth, textBitmap.height+photoBitmapDrawHeight.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(result)
        canvas.drawBitmap(photoBitmap, null, RectF(0f, 0f, photoBitmapDrawWidth, photoBitmapDrawHeight), null)
        canvas.drawBitmap(textBitmap, 0f, photoBitmapDrawHeight, null)

        return result
    }

    /** Max width of marker bitmap*/
    private fun getMarkerBitmapWidth() = (sysInfo.getScreenSize(context).x*Constants.MAX_MARKER_WIDTH).toInt()
}