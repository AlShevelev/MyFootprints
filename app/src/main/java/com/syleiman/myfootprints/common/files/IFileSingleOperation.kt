package com.syleiman.myfootprints.common.files

import java.io.File

/** Operations over file  */
interface IFileSingleOperation {
    /**
     * Delete file
     * @return true - success
     */
    fun delete(): Boolean

    /** Add path in shared area to file  */
    fun getFile(): File

    /** Get url to file  */
    fun getUrl(): String

    /** Get path to file  */
    fun getPath(): String

    /** Get name of file without path  */
    fun getName(): String

    /** Get file's privacy */
    fun getPrivacy(): FilePrivacyType

    /**
     * Copy data from source file
     * @param sourceFilePath path to source file
     * @return true - success
     */
    fun copyFrom(sourceFilePath: String): Boolean

    /** Check if file is exist and call @operationIfSuccess in case of success or @operationIfNotExist in case of unsuccess  */
    fun <T> ifExists(operationIfExist: Function1<IFileSingleOperation, T>, operationIfNotExist: Function1<IFileSingleOperation, T>): T

    /** Check if file is exist */
    fun isExists(): Boolean
}
