package com.syleiman.myfootprints.common

import java.util.TreeMap

class UniversalRepository
{
    private val entities: MutableMap<String, Any>
    private val tempValues: MutableMap<String, Any>         // Temp values for entities

    init
    {
        this.entities = TreeMap<String, Any>()
        this.tempValues = TreeMap<String, Any>()
    }

    fun <T> registerEntity(type: Class<T>, itemToRegister: Any) = entities.put(getKey(type), itemToRegister)

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntity(type: Class<T>): T? = entities[getKey(type)] as T?

    fun <T> getTempValue(type: Class<T>): Any? = tempValues.remove(getKey(type))         // Extract and remove

    fun <T> setTempValue(type: Class<T>, value: Any) = tempValues.put(getKey(type), value)

    private fun getKey(type: Class<*>): String = type.simpleName
}
