package com.qardio.museum.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qardio.museum.data.local.database.ArtDatabase
import com.qardio.museum.data.local.database.dao.ArtDao
import com.qardio.museum.model.ArtObjects
import com.qardio.museum.model.WebImage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

/**
 * Test class which contains the test cases for Data base actions with test db creation
 *
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ArtDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: ArtDatabase

    private lateinit var artDao: ArtDao

    @Before
    fun setup() {
        hiltRule.inject()
        artDao = database.artDao()
    }

    @Test
    fun test_insertAndFetchData() = runBlocking {

        val art = ArtObjects(
            artId = "1",
            title = "Test Art",
            principalOrFirstMaker = "Tester",
            longTitle = "Test Long Title",
            webImage = WebImage(
                guid = "123",
                url = "www.test.com",
                offsetPercentageX = 10,
                offsetPercentageY = 20,
                width = 100,
                height = 100
            )
        )
        artDao.insert(art)
        val allData = artDao.getArtData().first()
        assert(allData.contains(art))
    }

    @After
    fun tearDown() {
        database.close()
    }

}