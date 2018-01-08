package com.syleiman.myfootprints.common.files

/** Methods for path addition  */
interface IFileSinglePath {

    /** Add path in private area to file  */
    fun inPrivate(): IFileSingleOperation

    /** Add path in shared area to file  */
    fun inShared(): IFileSingleOperation
}
