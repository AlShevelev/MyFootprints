package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model

/** Enum with color components */
enum class ColorComponents(val value : Int)
{
    R(1),
    G(2),
    B(3);

    companion object Create
    {
        fun from(findValue: Int): ColorComponents = values().first { it.value == findValue }
    }
}
