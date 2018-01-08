package com.syleiman.myfootprints.common.files

import java.io.File

/** Working with many files  */
class FilesMulti
/**  */
private constructor(private val directory: File) : IFilesMultiOperation
{
    /** Remove all files with some prefix  */
    override fun clear(filesPrefix: String) = directory.listFiles().filter { it.name.startsWith(filesPrefix) && it.isFile }.forEach { it.delete() }

    /** Do something for each file */
    override fun forEach(action: (File) -> Unit) = forEach({true}, action)

    /** Do something for each file */
    override fun forEach(filer: (File) -> Boolean, action: (File) -> Unit) = directory.listFiles().forEach { if(filer(it)) action(it)  }

    companion object {

        /** Add path in private area to file  */
        fun inPrivate(): IFilesMultiOperation = FilesMulti(FileCommon.privatePathAsFile)

        /** Add path in shared area to file  */
        fun inShared(): IFilesMultiOperation = FilesMulti(FileCommon.sharedPathAsFile)
    }
}