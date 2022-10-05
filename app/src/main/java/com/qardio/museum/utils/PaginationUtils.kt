package com.qardio.museum.utils

import com.qardio.museum.utils.Constants.Companion.DATA_REFRESH_INTERVAL
import java.util.concurrent.TimeUnit

/**
 * Class holds the Utils functions which used in Pagination
 */
object PaginationUtils {

    /**
     * Calculate the time difference between last updated time and
     * Current time and compare it with Data refresh interval [DATA_REFRESH_INTERVAL]
     * Returns is refresh interval expired or not
     */
    fun isRefreshIntervalExpired(lastUpdatedTime : Long) : Boolean {
        val diffInMs = System.currentTimeMillis() - lastUpdatedTime
        val minutesExpired : Long = TimeUnit.MILLISECONDS.toMinutes(diffInMs)
        return minutesExpired >= DATA_REFRESH_INTERVAL
    }
}