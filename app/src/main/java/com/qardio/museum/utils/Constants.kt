package com.qardio.museum.utils

/*
* class contains the constants used in  application
 */
class Constants {
    companion object {
        const val API_KEY = "key"
        const val REQUEST_TIME_OUT : Long = 15 // seconds
        const val DEFAULT_PAGE_INDEX = 1

        const val PREF_KEY_UPDATE_TIME = "LAST_UPDATED_TIME"
        const val DATA_REFRESH_INTERVAL : Long = 5 // minutes

        const val CREATOR_NAME_TO_QUERY = "Artus Quellinus (I)"

        const val PREFERENCE_NAME = "ART_PREFERENCE"
    }

    /* class contains the constants used in Database */
    class DBConstants {
        companion object {
            const val DATABASE_NAME = "art_db"
            const val TABLE_ART = "art"
        }
    }

}