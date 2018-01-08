package com.syleiman.myfootprints.common.files

import com.syleiman.myfootprints.applicationLayer.App

import java.io.File

/** Common files functions  */
internal object FileCommon {
    /** Get path to private area  */
    val privatePathAsString: String
        get() = privatePathAsFile.absolutePath

    val sharedPathAsString: String
        get() = sharedPathAsFile.absolutePath

    /** Get path to private area  */
    val privatePathAsFile: File
        get() = App.context.filesDir

    val sharedPathAsFile: File
        get() = App.context.externalCacheDir ?: App.context.externalCacheDirs[0]

    /** If @fileName has't got path - and path to private area  */
    fun addPathToFileName(fileName: String, path: String): String = path + File.separator + fileName
}