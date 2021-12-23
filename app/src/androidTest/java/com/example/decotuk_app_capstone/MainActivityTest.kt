package com.example.decotuk_app_capstone

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.decotuk_app_capstone.util.EspressoIdlingResource
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun tearsDown(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    //Launch test must be from MainActivity after User Login
    @Test
    fun loadCovidCases(){
        onView(withId(R.id.navigation_covid_stat)).perform(click())
        onView(withId(R.id.today_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_confirmed)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_recovery)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_death)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_total_case)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_total_recovery)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_total_death)).check(matches(isDisplayed()))
    }

    @Test
    fun loadProfile(){
        onView(withId(R.id.navigation_profile)).perform(click())
        onView(withId(R.id.iv_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_username)).check(matches(isDisplayed()))
        onView(withId(R.id.textView8)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_profile)).perform(click())
        onView(withId(R.id.edt_name)).check(matches(isDisplayed()))
        onView(withId(R.id.edt_email)).check(matches(isDisplayed()))
        onView(withId(R.id.edt_password)).check(matches(isDisplayed()))
        onView(withId(R.id.button5)).check(matches(isDisplayed()))
        onView(isRoot()).perform(ViewActions.pressBack())
        onView(withId(R.id.kirim_laporan)).perform(click())
        onView(withId(R.id.edt_email)).check(matches(isDisplayed()))
        onView(withId(R.id.edt_saran)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_kirim)).check(matches(isDisplayed()))
        onView(isRoot()).perform(ViewActions.pressBack())
        onView(withId(R.id.logout)).check(matches(isDisplayed()))
    }

    @Test
    fun loadRecordHistory(){
        onView(withId(R.id.navigation_history)).perform(click())
        //onView(withId(R.id.rv_records)).check(matches(isDisplayed()))
    }

    @Test
    fun loadRecord(){
        onView(withId(R.id.navigation_record)).perform(click())
        onView(withId(R.id.elapsed_time)).check(matches(isDisplayed()))
        onView(withId(R.id.bt_record)).check(matches(isDisplayed()))
    }
}