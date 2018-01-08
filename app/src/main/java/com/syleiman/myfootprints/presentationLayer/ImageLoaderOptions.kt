package com.syleiman.myfootprints.presentationLayer

import android.graphics.Bitmap
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.syleiman.myfootprints.applicationLayer.App

/** Options for ImageLoader  */
object ImageLoaderOptions
{
    /** Common options  */
    fun initImageLoader()
    {
        val config = ImageLoaderConfiguration.Builder(App.context)
        config.threadPriority(Thread.NORM_PRIORITY - 2)
        config.denyCacheImageMultipleSizesInMemory()
        config.diskCacheFileNameGenerator(Md5FileNameGenerator())
        config.diskCacheSize(200 * 1024 * 1024) // 200 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO)
        config.threadPoolSize(2)
        config.memoryCache(WeakMemoryCache())
        config.diskCacheExtraOptions(480, 320, null)

        //config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build())
    }

    /** Typical options for one image  */
    val singleImageOptions: DisplayImageOptions
        get() = DisplayImageOptions.Builder()
            .cacheInMemory(false)
            .cacheOnDisk(false)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true).
            imageScaleType(ImageScaleType.EXACTLY)
            .build()

    /** Options for photo grid  */
    val photoGridOptions: DisplayImageOptions
        get() = DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build()
}
