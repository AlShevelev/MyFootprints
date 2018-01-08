package com.syleiman.myfootprints.modelLayer.dbEntities

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

/**
 * Table for key-value storage
 */
@Table(name = "KeyValueData")
class DbKeyValueData() : Model()
{
    @Column(name = "Key", index = true)
    var key: Int = 0

    @Column(name = "Value")
    var value: String? = null

    constructor(key: Int, value: String) : this()
    {
        this.key = key
        this.value = value
    }
}
