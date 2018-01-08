package com.syleiman.myfootprints.presentationLayer.activities

/**
 * Request codes of activities
 */
enum class InternalActivitiesCodes(var value : Int)
{
    FootprintsGallery(0),
    EditFootprint(1),
    CreateFootprint(2),
    PhotoEditor(3);

    companion object Create
    {
        fun from(findValue: Int): InternalActivitiesCodes? = InternalActivitiesCodes.values().firstOrNull { it.value == findValue }
    }
}
