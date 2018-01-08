package com.syleiman.myfootprints.businessLayer.localDbService.repositories

import com.activeandroid.query.Select
import com.syleiman.myfootprints.common.letNull
import com.syleiman.myfootprints.businessLayer.localDbService.TransactionWrapper
import com.syleiman.myfootprints.modelLayer.dbEntities.DbKeyValueData
import java.sql.SQLException

class KeyValueStorageRepository : IKeyValueStorageRepository
{
    /** @return null - no value with such key
     */
    @Throws(SQLException::class)
    override fun getValue(key: Int): String? = TransactionWrapper.processWithoutTransaction<String?> {
        Select().from(DbKeyValueData::class.java).where("Key = ?", key).executeSingle<DbKeyValueData>()?.value
    }

    /**  */
    @Throws(SQLException::class)
    override fun setValue(key: Int, value: String)
    {
        TransactionWrapper.processInTransaction {
            Select().from(DbKeyValueData::class.java).where("Key = ?", key).executeSingle<DbKeyValueData>().letNull({
                it.value = value
                it.save()
            },
            { DbKeyValueData(key, value).save() })
        }
    }
}