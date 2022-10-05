package com.qardio.museum

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.qardio.museum.util.launchFragmentInHiltContainer
import com.qardio.museum.view.ArtListFragment
import com.qardio.museum.view.MainActivity
import com.qardio.museum.view.adapter.ArtListAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

/**
 * Test class which contains the test cases for Recyler view UI actions
 *
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ArtListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val mockNavController = mock(NavController::class.java)

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Before
    fun setup() {
        hiltRule.inject()

        launchFragmentInHiltContainer<ArtListFragment> {
            mockNavController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(requireView(), mockNavController)
        }
    }

    @Test
    fun test_isListVisible() {
        onView(withId(R.id.recylerViewArts)).check(matches(isDisplayed()))
    }

    @Test
    fun test_listScroll() {

        onView(withId(R.id.recylerViewArts)).check(matches(isDisplayed())).
            perform(RecyclerViewActions.scrollToPosition<ArtListAdapter.ArtViewHolder>(10))
    }

    @Test
    fun test_selectListItem_isDetailsVisible() {

        onView(withId(R.id.recylerViewArts)).perform(RecyclerViewActions.actionOnItemAtPosition<ArtListAdapter.ArtViewHolder>(1, click()))
        mockNavController.currentDestination?.id?.let { assert(it == R.id.details_fragment) }
    }
}