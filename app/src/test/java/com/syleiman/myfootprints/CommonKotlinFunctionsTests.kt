package com.syleiman.myfootprints

import com.syleiman.myfootprints.common.create2DArray
import com.syleiman.myfootprints.common.iif
import com.syleiman.myfootprints.common.letNull
import org.junit.Test
import org.junit.Assert.*

/** Tests for common Kotlin functions. */
class CommonKotlinFunctionsTests {

    /** Test for @letNull function - case when object is not null */
    @Test
    @Throws(Exception::class)
    fun letNullNotNullableTest()
    {
        val testStr : String? = "test string"

        testStr.letNull({assertTrue(true)}, {assertTrue(false)})
    }

    /** Test for @letNull function - case when object is null */
    @Test
    @Throws(Exception::class)
    fun letNullNullableTest()
    {
        val testStr : String? = null

        testStr.letNull({assertTrue(false)}, {assertTrue(true)})
    }

    /** Test for @iif function */
    @Test
    @Throws(Exception::class)
    fun iifTest()
    {
        var bool = true
        assertTrue(bool.iif({true}, {false}))

        bool = false
        assertTrue(bool.iif({false}, {true}))

        val i = 5
        assertTrue((i>0).iif({true}, {false}))
        assertTrue((i<0).iif({false}, {true}))
    }

    @Test
    @Throws(Exception::class)
    fun create2DArrayTest()
    {
        val array = create2DArray(2, 3){"S"}

        assertEquals(array[0][0], "S")
        assertEquals(array[1][2], "S")

        try
        {
            assertEquals(array[2][3], "S")
            assertTrue(false)
        }
        catch (ex : ArrayIndexOutOfBoundsException) { assertTrue(true) }
    }
}
