package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync

/** Set of states  */
enum class States(var value : Int)
{
    Initial(0),
    Connecting(1),
    GetFiles(2),
    SendLogRecords(3),
    ProcessExtRecords(4),
    Final(5);

    companion object Create
    {
        fun from(findValue: Int): States = States.values().first { it.value == findValue }
    }
}
