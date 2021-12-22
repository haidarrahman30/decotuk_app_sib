package com.example.decotuk_app_capstone.ui.auth.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.decotuk_app_capstone.MainActivity
import com.example.decotuk_app_capstone.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    //Launch test must be from Login Activity (User have to logout first)
    @Test
    fun loadLogin(){
        onView(withId(R.id.editTextTextEmailAddress)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextTextPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.button2)).check(matches(isDisplayed()))
    }

    @Test
    fun loadSignUp(){
        onView(withId(R.id.textView13)).perform(click())
        onView(withId(R.id.editTextTextPersonName)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextTextEmailAddress2)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextTextPassword2)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextTextPassword3)).check(matches(isDisplayed()))
        onView(withId(R.id.button5)).check(matches(isDisplayed()))
    }
}