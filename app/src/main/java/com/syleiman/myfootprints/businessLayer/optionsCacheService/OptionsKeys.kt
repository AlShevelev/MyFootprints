package com.syleiman.myfootprints.businessLayer.optionsCacheService

/** Options keys */
enum class OptionsKeys(var value: Int)
{
    TotalFootprints(0),
    LastPhotoFileName(1),

    /** Sync data only via WiFi */
    SyncOnlyViaWifiOptions(2),

    LastFootprintId(3),

    /** Creation moment of last processed external sync file */
    ExternalLastSyncDate(4),

    /** If of current app install */
    InstallId(5);

    companion object Create
    {
        fun from(findValue: Int): OptionsKeys = OptionsKeys.values().first { it.value == findValue }
    }
}