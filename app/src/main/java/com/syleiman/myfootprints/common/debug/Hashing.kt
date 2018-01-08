package com.syleiman.myfootprints.common.debug

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**   */
object Hashing
{
    /** Calculate SHA-1 hash  */
    fun calculateSHA(data: ByteArray): String
    {
        try
        {
            val md: MessageDigest = MessageDigest.getInstance("SHA-1")
            return bytes2Hex(md.digest(data))
        } catch (e: NoSuchAlgorithmException)
        {
            e.printStackTrace()
        }

        return "No hash"
    }

    /** Convert bytes to Hex string  */
    private fun bytes2Hex(data: ByteArray): String
    {
        val stringBuilder = StringBuilder()

        for (i in data.indices)
        {
            if (i != 0)
                stringBuilder.append(" ")
            stringBuilder.append(String.format("%02x", data[i]))
        }

        return stringBuilder.toString()
    }
}
