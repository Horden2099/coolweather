package com.coolweather.android;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.runner.lifecycle.Stage.RESUMED;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public WeatherActivity weatherActivity;
    public WeatherActivityTest weatherActivityTest = new WeatherActivityTest();

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class){
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
        }

        @Override
        protected void afterActivityLaunched() {
            super.afterActivityLaunched();
            weatherActivity = (WeatherActivity) getActivityInstance();
        }

        @Override
        protected void afterActivityFinished() {
            super.afterActivityFinished();
        }
    };


    @Test
    public void mainTest() throws Exception {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivityTestRule.getActivity());
        //第一次加载则进入地址选择
        if (prefs.getString("weather",null) == null){
            chooseAreaTest1();
        }
        Thread.sleep(1000);
//        weatherActivityTest.mainUITest();
//        weatherActivityTest.testDialog(weatherActivity);
    }

    @Test
    public void weatherActivityTest() throws Exception {
        weatherActivityTest.mainUITest();
        weatherActivityTest.testDialog(weatherActivity);
    }


    /**
     * 测试选择地点导航
     */
    private void chooseAreaTest1() throws InterruptedException {
        onData(hasToString(startsWith("北京")))
                .inAdapterView(withId(R.id.list_view)).atPosition(0)
                .perform(click());
        Thread.sleep(1000);
        onData(hasToString(startsWith("北京")))
                .inAdapterView(withId(R.id.list_view)).atPosition(0)
                .perform(click());
        Thread.sleep(1000);
        onData(hasToString(startsWith("北京")))
                .inAdapterView(withId(R.id.list_view)).atPosition(0)
                .perform(click());
    }

    /**
     * 用于测试listview配置Matcher
     */
    public static Matcher<Object> withItemContent(String expectedText) {
        checkNotNull(expectedText);
        return withItemContent(equalTo(expectedText));
    }

    /**
     * 获取下一个activity
     */
    public void navigate() {
//        onView(withId(R.id.btn_login)).perform(click());
        Activity activity = getActivityInstance();
        assertTrue(activity instanceof WeatherActivity);
        // do more
    }

    public Activity getActivityInstance() {
        final Activity[] activity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Activity currentActivity = null;
                Collection resumedActivities =
                        ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity = (Activity) resumedActivities.iterator().next();
                    activity[0] = currentActivity;
                }
            }
        });
        return activity[0];
    }


}
