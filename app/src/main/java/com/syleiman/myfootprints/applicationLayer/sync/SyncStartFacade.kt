package com.syleiman.myfootprints.applicationLayer.sync

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.syleiman.myfootprints.R
import com.syleiman.myfootprints.applicationLayer.App
import com.syleiman.myfootprints.common.LogTags

/** Facade for starting sync operations */
class SyncStartFacade(context: Context)
{
    private var syncAccount: Account

    /** Constructor */
    init
    {
        syncAccount = createSyncAccount(context)
    }

    /** Create account for Android sync framework  */
    private fun createSyncAccount(context: Context): Account
    {
        val account = Account(App.getStringRes(R.string.sync_account), App.getStringRes(R.string.sync_account_type))
        val accountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

        if (accountManager.addAccountExplicitly(account, null, null))
            scheduleSync(account)
        else
            Log.d(LogTags.SYNC_PROCESS, "Account already added")

        return account
    }

    /** Run sync manually */
    fun startSyncManually()
    {
        val settingsBundle = Bundle()
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)

        Log.d(LogTags.SYNC_PROCESS, "Sync request")
        ContentResolver.requestSync(syncAccount, App.getStringRes(R.string.sync_authority), settingsBundle)
    }

    /** Set up sync by connection established */
    private fun scheduleSync(account: Account)
    {
        // Inform the system that this account supports sync
        ContentResolver.setIsSyncable(account, App.getStringRes(R.string.sync_authority), 1);

        // Inform the system that this account is eligible for auto sync when the network is up
        ContentResolver.setSyncAutomatically(account, App.getStringRes(R.string.sync_authority), true);

        ContentResolver.addPeriodicSync(account, App.getStringRes(R.string.sync_authority), Bundle.EMPTY, 3601);
    }
}
