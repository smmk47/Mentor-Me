package com.example.myapplication1


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Test2{

    @Before
    fun setUp() {
        // Start the MainActivity before each test
        ActivityScenario.launch(MainActivity2::class.java)
    }

    @Test
    fun signupbutton() {
        // Click on the login button
        onView(withId(R.id.textView3)).perform(click())

        // Add assertions to verify the expected behavior after clicking the login button
        // For example, you can assert that the activity corresponding to login is launched
        // You can add more assertions based on your application's behavior
    }
}
