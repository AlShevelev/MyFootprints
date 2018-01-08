package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync

/** Set of events  */
enum class Events(var value : Int)
{
    StartConnect(0),
    Error(1),
    Connected(2),
    ProcessOneItem(3),
    ProcessCompleted(4);

    companion object Create
    {
        fun from(findValue: Int): Events = Events.values().first { it.value == findValue }
    }
}
