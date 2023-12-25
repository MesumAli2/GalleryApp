package com.mesum.galleryapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest

@RunWith(AndroidJUnit4::class)
class AlbumFragmentTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun recyclerView_isDisplayed() {
        onView(withId(R.id.albumsRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun recyclerView_loadsAlbums() {
        onView(withId(R.id.albumsRecyclerView))
            .check(matches(hasMinimumChildCount(1)))
    }


}




