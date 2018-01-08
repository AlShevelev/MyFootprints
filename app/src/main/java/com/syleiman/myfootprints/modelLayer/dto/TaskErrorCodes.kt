package com.syleiman.myfootprints.modelLayer.dto

/** Task's error codes  */
enum class TaskErrorCodes(var value : Int)
{
    /** Get city and country by location  */
    None(0);

    companion object Create
    {
        fun from(findValue: Int): TaskErrorCodes = TaskErrorCodes.values().first { it.value == findValue }
    }
}
