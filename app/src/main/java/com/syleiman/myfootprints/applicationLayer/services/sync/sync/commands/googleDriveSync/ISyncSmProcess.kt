package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync

interface ISyncSmProcess
{
    /** Process event  */
    fun process(event: Events)
}