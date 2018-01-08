package com.syleiman.myfootprints

import com.syleiman.myfootprints.common.UniversalRepository

import org.junit.Test

import org.junit.Assert.*

/** To work on unit tests, switch the Test Artifact in the Build Variants view. */
class UniversalRepositoryTests {
    @Test
    @Throws(Exception::class)
    fun simpleTest() {
        val repository = UniversalRepository()
        repository.registerEntity(Int::class.java, 5)
        repository.registerEntity(String::class.java, "Test")

        val testInt = repository.getEntity(Int::class.java)
        val testString = repository.getEntity(String::class.java)

        assertTrue(testInt == 5)
        assertTrue(testString == "Test")
    }
}