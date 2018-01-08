package com.syleiman.myfootprints.businessLayer.localDbService

import com.activeandroid.ActiveAndroid
import java.sql.SQLException

/** Wrapper for transaction working  */
object TransactionWrapper
{
    /**
     * Call without business result
     * @param action action to call
     * @return true - success
     */
    @Throws(SQLException::class)
    fun processInTransaction(action: () -> Unit)
    {
        ActiveAndroid.beginTransaction()

        try
        {
            action()
            ActiveAndroid.setTransactionSuccessful()
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            throw SQLException(ex.message)
        }
        finally
        {
            ActiveAndroid.endTransaction()
        }
    }

    /**
     * Call with business-value result
     * @param action action to call
     * @return true - success
     */
    @Throws(SQLException::class)
    fun <T> processInTransaction(action: () -> T): T
    {
        ActiveAndroid.beginTransaction()

        try
        {
            val callResult = action()

            ActiveAndroid.setTransactionSuccessful()
            return callResult
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            throw SQLException(ex.message)
        }
        finally
        {
            ActiveAndroid.endTransaction()
        }
    }

    /**
     * Call without transaction
     * @param action action to call
     * @return true - success
     */
    @Throws(SQLException::class)
    fun <T> processWithoutTransaction(action: () -> T): T
    {
        try
        {
            return action()
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            throw SQLException(ex.message)
        }

    }

    /**
     * Call without transaction
     * @param action action to call
     * @return true - success
     */
    @Throws(SQLException::class)
    fun processWithoutTransaction(action: () -> Unit)
    {
        try
        {
            action()
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            throw SQLException(ex.message)
        }
    }
}