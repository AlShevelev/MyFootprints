package com.syleiman.myfootprints.modelLayer.dto

/** Types of task  */
enum class TaskTypes(var value : Int)
{
    /** Get city and country by location  */
    Geocoding(1);

    companion object Create
    {
        fun from(findValue: Int): TaskTypes = TaskTypes.values().first { it.value == findValue }
    }
}
