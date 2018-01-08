package com.syleiman.myfootprints.businessLayer.localDbService.repositories

import java.sql.SQLException

interface IKeyValueStorageRepository
{
    /** @return null - no value with such key */
    @Throws(SQLException::class)
    fun getValue(key: Int): String?

    /**  */
    @Throws(SQLException::class)
    fun setValue(key: Int, value: String)
}
