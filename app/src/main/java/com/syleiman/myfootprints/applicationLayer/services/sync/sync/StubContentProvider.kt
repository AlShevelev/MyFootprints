package com.syleiman.myfootprints.applicationLayer.services.sync.sync

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/** Content provider for used in Android sync framework
 * Stub (as long as we process sync data manually)  */
class StubContentProvider : ContentProvider()
{
    /**  */
    override fun onCreate(): Boolean = true

    /**  */
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? = null

    /**  */
    override fun getType(uri: Uri): String? = null

    /**  */
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    /**  */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    /**  */
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
}
