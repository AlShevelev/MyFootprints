package com.syleiman.myfootprints.common

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.util.Date
import java.util.Locale

/** Convert data to string and from string for network transfer  */
object DataToString
{
    private var decimalFormat: DecimalFormat? = null

    init
    {
        decimalFormat = DecimalFormat.getNumberInstance() as DecimalFormat
        decimalFormat!!.maximumFractionDigits = 8
        decimalFormat!!.decimalFormatSymbols = DecimalFormatSymbols(Locale.US)
    }

    /**  */
    fun toString(value: Double): String
    {
        return decimalFormat!!.format(value)
    }

    /**  */
    fun toString(value: Long): String
    {
        return value.toString()
    }

    /**  */
    fun toString(value: Date): String
    {
        return toString(value.time)
    }

    /**  */
    fun toString(value: Int): String
    {
        return value.toString()
    }

    /**  */
    fun fromStringToDouble(value: String, defaultValue: Double): Double
    {
        try
        {
            return decimalFormat!!.parse(value).toDouble()
        }
        catch (e: ParseException)
        {
            return defaultValue
        }

    }

    /**  */
    fun fromStringToLong(value: String): Long
    {
        return java.lang.Long.parseLong(value)
    }

    /**  */
    fun fromStringToDate(value: String): Date
    {
        return Date(fromStringToLong(value))
    }

    /**  */
    fun fromStringToInt(value: String): Int
    {
        return Integer.parseInt(value)
    }
}