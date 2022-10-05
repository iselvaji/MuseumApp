package com.qardio.museum

import com.qardio.museum.utils.PaginationUtils
import org.junit.Test

import org.junit.Assert.*

/**
 * Test class which contains the test cases for Util class [PaginationUtils] used in app
 *
 */

class UtilsTest {

    @Test
    fun test_isRefreshIntervalNotExpired() {

        val lastUpdated = 0L
        val isRefreshIntervalExpired = PaginationUtils.isRefreshIntervalExpired(lastUpdated)
        assertTrue(isRefreshIntervalExpired)
    }

    @Test
    fun test_isRefreshIntervalExpired() {

        val lastUpdated = System.currentTimeMillis() - 360000L // 6 Minutes which more than REFRESH Interval [5 mins]
        val isRefreshIntervalExpired = PaginationUtils.isRefreshIntervalExpired(lastUpdated)
        assertTrue(isRefreshIntervalExpired)
    }

    @Test
    fun test_isRefreshIntervalWithinTimeout() {

        val lastUpdated = System.currentTimeMillis() - 240000L // 4 Minutes which less than REFRESH Interval [5 mins]
        val isRefreshIntervalExpired = PaginationUtils.isRefreshIntervalExpired(lastUpdated)
        assertFalse(isRefreshIntervalExpired)
    }


}