package com.syleiman.myfootprints.applicationLayer.services.sync.auth

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

import com.syleiman.myfootprints.common.LogTags

/** A bound Service that instantiates the authenticator when started.  */
class AuthenticatorService : Service()
{
    private var authenticator: StubAuthenticator? = null

    /**  */
    override fun onCreate()
    {
        Log.d(LogTags.SYNC_PROCESS, "Start authenticator service")
        authenticator = StubAuthenticator(this)
    }

    /** When the system binds to this Service to make the RPC call return the authenticator's IBinder.  */
    override fun onBind(intent: Intent): IBinder? = authenticator!!.iBinder
}
