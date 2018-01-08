package com.syleiman.myfootprints.common

/** Common functions */

/** check object to null value and call @notNullBlock if object if null or @nullBlock otherwise */
inline fun <T, R> T.letNull(notNullBlock: (T) -> R, nullBlock: () -> R): R {
    if(this!=null)
        return notNullBlock(this)
    else
        return nullBlock()
}

/** Analog for ? : operator (can't find in Kotlin) */
inline fun <R> Boolean.iif(trueBlock: () -> R, falseBlock: () -> R): R {
    if(this)
        return trueBlock()
    else
        return falseBlock()
}

inline fun <reified T> create2DArray(rows: Int, cols: Int, noinline init: (Int)->T): Array<Array<T>> = Array(rows) { Array(cols, init) }
