package com.syleiman.myfootprints.common.debug

import com.google.android.gms.maps.model.LatLng
import com.syleiman.myfootprints.modelLayer.dto.FootprintSaveDto

import java.util.ArrayList
import java.util.Random
import java.util.UUID

/** Generate test footprints  */
object FootprintsGenerator
{
    /** Create set on random footprints based on @baseFootprint  */
    fun createRandomFootprints(baseFootprint: FootprintSaveDto, quantity: Int): List<FootprintSaveDto>
    {
        val random = Random()
        val result = ArrayList<FootprintSaveDto>(quantity)
        for (i in 0..quantity - 1)
        {
            result.add(FootprintSaveDto(
                baseFootprint.photoFile,
                baseFootprint.isPhotoFromCamera,
                UUID.randomUUID().toString(),
                LatLng((random.nextInt(180) - 90).toDouble(), (random.nextInt(359) - 180).toDouble()),
                baseFootprint.isTwitterTurnOn,
                baseFootprint.isInstagramTurnOn,
                null))
        }

        return result
    }
}