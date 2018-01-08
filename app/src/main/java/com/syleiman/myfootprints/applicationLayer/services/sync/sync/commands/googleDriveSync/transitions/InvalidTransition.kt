package com.syleiman.myfootprints.applicationLayer.services.sync.sync.commands.googleDriveSync.transitions

/** Not supported transition  */
class InvalidTransition : ITransition
{
    /** Do something useful  */
    override fun process() = throw UnsupportedOperationException()
}
