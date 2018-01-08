package com.syleiman.myfootprints.common.files

import com.syleiman.myfootprints.common.iif
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/** Working with one file  */
class FileSingle
/**  */
private constructor() : IFileSinglePath, IFileSingleOperation
{
    private lateinit var fileName: String
    private lateinit var privacyType: FilePrivacyType

    companion object Create
    {
        val TEMP_FILES_PREFIX = "tmp_"
        val THUMBNAIL_FILES_PREFIX = "thb_"

        /** Create file with name  */
        fun withName(fileName: String): IFileSinglePath
        {
            val result = FileSingle()
            result.fileName = fileName
            return result
        }

        /** Create file with random name  */
        fun withRandomName(prefix: String, ext: String): IFileSinglePath
        {
            val result = FileSingle()
            result.fileName = prefix + UUID.randomUUID().toString() + "." + ext
            return result
        }

        /** Create file based on existed with prefix before file's name  */
        fun fromExistWithPrefix(exist: IFileSingleOperation, prefix: String): IFileSingleOperation
        {
            return FileSingle.withName(prefix+exist.getName()).
                let { (exist.getPrivacy()==FilePrivacyType.IsPrivate).iif({it.inPrivate()}, {it.inShared()})}
        }

        /** Create file from its Url  */
        fun fromUrl(urlToFile: String): IFileSingleOperation
        {
            val result = FileSingle()
            result.fileName = urlToFile.replace("file:/", "")
            return result
        }

        /** Create file from its path  */
        fun fromPath(filePath: String): IFileSingleOperation
        {
            val result = FileSingle()
            result.fileName = filePath
            return result
        }
    }

    /** Add path in private area to file  */
    override fun inPrivate(): IFileSingleOperation
    {
        fileName = FileCommon.addPathToFileName(fileName, FileCommon.privatePathAsString)
        privacyType = FilePrivacyType.IsPrivate
        return this
    }

    /** Add path in shared area to file  */
    override fun inShared(): IFileSingleOperation
    {
        fileName = FileCommon.addPathToFileName(fileName, FileCommon.sharedPathAsString)
        privacyType = FilePrivacyType.IsShared
        return this
    }

    /** Add path in shared area to file  */
    override fun getFile(): File = File(fileName)

    /**
     * Delete file
     * @return true - success
     */
    override fun delete(): Boolean = getFile().delete()

    /** Get url to file  */
    override fun getUrl(): String = "file:/" + fileName

    /** Get path to file  */
    override fun getPath(): String = fileName

    /** Get name of file without path  */
    override fun getName(): String = getFile().name

    /** Get file's privacy */
    override fun getPrivacy(): FilePrivacyType = privacyType

    /**
     * Copy data from source file
     * @param sourceFilePath path to source file
     */
    override fun copyFrom(sourceFilePath: String): Boolean
    {
        try
        {
            FileInputStream(fromPath(sourceFilePath).getFile()).use { input ->
                FileOutputStream(getFile()).use { output ->
                    val buf = ByteArray(1024)
                    var len: Int = input.read(buf)

                    while (len > 0) {
                        output.write(buf, 0, len)
                        len = input.read(buf)
                    }

                    return true
                }
            }
        }
        catch (ex: IOException)
        {
            ex.printStackTrace()
            return false
        }
    }

    /** Check if file is exist and call @operationIfSuccess in case of success or @operationIfNotExist in case of unsuccess  */
    override fun <T> ifExists(operationIfExist: Function1<IFileSingleOperation, T>, operationIfNotExist: Function1<IFileSingleOperation, T>): T
    {
        if (isExists())
            return operationIfExist.invoke(this)
        return operationIfNotExist.invoke(this)
    }

    /** Check if file is exist */
    override fun isExists(): Boolean = getFile().exists()
}