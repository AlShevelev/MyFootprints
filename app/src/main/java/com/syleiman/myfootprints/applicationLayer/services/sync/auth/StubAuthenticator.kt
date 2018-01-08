package com.syleiman.myfootprints.applicationLayer.services.sync.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.NetworkErrorException
import android.content.Context
import android.os.Bundle

/** Authenticator to access Android sync framework
 * Stub (as long as we have'nt got any user account)  */
class StubAuthenticator
/**  */
(context: Context) : AbstractAccountAuthenticator(context)
{
    /**  */
    override fun editProperties(response: AccountAuthenticatorResponse, accountType: String): Bundle? = null

    /**  */
    @Throws(NetworkErrorException::class)
    override fun addAccount(response: AccountAuthenticatorResponse, accountType: String, authTokenType: String, requiredFeatures: Array<String>, options: Bundle): Bundle? = null

    /**  */
    @Throws(NetworkErrorException::class)
    override fun confirmCredentials(response: AccountAuthenticatorResponse, account: Account, options: Bundle): Bundle? = null

    /**  */
    @Throws(NetworkErrorException::class)
    override fun getAuthToken(response: AccountAuthenticatorResponse, account: Account, authTokenType: String, options: Bundle): Bundle? = null

    /**  */
    override fun getAuthTokenLabel(authTokenType: String): String? = null

    /**  */
    @Throws(NetworkErrorException::class)
    override fun updateCredentials(response: AccountAuthenticatorResponse, account: Account, authTokenType: String, options: Bundle): Bundle? = null

    /**  */
    @Throws(NetworkErrorException::class)
    override fun hasFeatures(response: AccountAuthenticatorResponse, account: Account, features: Array<String>): Bundle? = null
}