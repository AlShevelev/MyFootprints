package com.syleiman.myfootprints.presentationLayer.activities.photoEditor.model

/** Set of filters */
enum class Filters(val value : String, val isInstant : Boolean = true)
{
    Crop("crop", false),

    Boost("boost", false),
    Brightness("brightness", false),
    ColorDepth("colorDepth", false),
    ColorBalance("color_balance", false),
    Contrast("contrast", false),
    Emboss("emboss"),               // Instant
    Flip("flip", false),
    Gamma("gamma", false),
    GaussianBlur("gaussian"),       // Instant
    Grayscale("grayscale"),         // Instant
    Hue("hue", false),
    Invert("invert"),               // Instant
    Noise("noise"),                 // Instant
    Saturation("saturation", false),
    Sepia("sepia"),                 // Instant
    Sharpen("sharpen"),             // Instant
    Sketch("sketch"),               // Instant
    Tint("tint", false),
    Vignette("vignette");           // Instant

    companion object Create
    {
        fun from(findValue: String): Filters = values().first { it.value == findValue }
    }
}
