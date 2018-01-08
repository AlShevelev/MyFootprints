package com.syleiman.myfootprints.businessLayer.sysInfoService

import android.content.Context
import android.graphics.Point

/** Common information about system  */
interface ISystemInformationService
{
    /** Is geolocation enabled on device  */
    fun isGeoLocationEnabled(): Boolean

    /** Is google play services available on device  */
    fun isGooglePlayServicesAvailable(): Boolean

    /** Get size of device screen  */
    fun getScreenSize(context: Context): Point

    /** Get unique id of device  */
    fun getInstallId(): String

    /** */
    fun dpToPx(dp: Int): Int

    /** */
    fun pxToDp(px: Int): Int

}
