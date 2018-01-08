package com.syleiman.myfootprints.modelLayer.enums

enum class BitmapsQuality(var value : Int)
{
    VeryHigh(100),
    High(75),
    Moderate(50),
    Low(25),
    VeryLow(0);

    companion object Create
    {
        fun from(findValue: Int): BitmapsQuality = values().first { it.value == findValue }
    }
}