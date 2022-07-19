package com.coolweather.android;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class IntentTest {

    @Rule
    public IntentsTestRule<MainActivity> mIntentTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void testJump() throws Exception {
        // Build the result to return when the activity is launched.
        Intent resultData = new Intent();
        String phoneNumber = "123-345-6789";
        resultData.putExtra("phone", phoneNumber);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Set up result stubbing when an intent sent to "contacts" is seen.
        intending(toPackage("com.android.contacts")).respondWith(result);

        // Assert that the data we set up above is shown.
//        onView(withId(R.id.phoneNumber)).check(matches(withText(phoneNumber)));
    }

}
