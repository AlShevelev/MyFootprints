package com.syleiman.myfootprints.common.broadcast

interface IActionsCollectionEdit<T>
{
    /** Add action to collection  */
    fun addAction(actionToAdd: ActionBase<T>): IActionsCollectionEdit<*>
}
