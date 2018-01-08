package com.syleiman.myfootprints.common.files

import java.io.File

/** Operations over directory  */
interface IFilesMultiOperation
{
    /** Remove all files with some prefix  */
    fun clear(filesPrefix: String)

    /** Do something for each file */
    fun forEach(action: (File) -> Unit)

    /** Do something for each file */
    fun forEach(filer: (File) -> Boolean, action: (File) -> Unit)
}
