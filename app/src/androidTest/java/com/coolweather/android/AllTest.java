package com.coolweather.android;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.coolweather.android.WeatherActivityTest.withCustomConstraints;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.test.SimpleIdlingResource;
import com.coolweather.android.util.Utility;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AllTest{

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,true);
    @Rule
    public ActivityTestRule<WeatherActivity> weatherRule = new ActivityTestRule<>(WeatherActivity.class,true);

    public MainActivityTest mainActivityTest = new MainActivityTest();
    public WeatherActivityTest weatherActivityTest = new WeatherActivityTest();
    private SimpleIdlingResource simpleIdlingResource;

    @Before
    public void setUp(){
        weatherActivityTest.setup(weatherRule,simpleIdlingResource);
    }

    @Test
    public void allTest() throws Exception {
        mainActivityTest.mainTest(rule);
        weatherActivityTest.mainUITest();
    }

    @Test
    public void titleTextShowTest(){
        weatherActivityTest.titleTextShowTest(weatherRule);
    }

    @Test
    public void nowWeatherTextShowTest(){
        weatherActivityTest.nowWeatherTextShowTest(weatherRule);
    }

    @Test
    public void forecastWeatherTextShowTest(){
        weatherActivityTest.forecastWeatherTextShowTest(weatherRule);
    }

    @Test
    public void AQITextShowTest(){
        weatherActivityTest.AQITextShowTest(weatherRule);
    }

    @Test
    public void suggestionTextTest(){
        weatherActivityTest.suggestionTextTest(weatherRule);
    }

    @Test
    public void loadImage() throws Exception {
        weatherActivityTest.loadImage(weatherRule);
    }

    @Test
    public void refreshTest() throws InterruptedException {
        onView(withId(R.id.swipe_refresh)).perform(withCustomConstraints(swipeDown(),isDisplayingAtLeast(85)));//下拉刷新
        Thread.sleep(1000);
        titleTextShowTest();
        nowWeatherTextShowTest();
        forecastWeatherTextShowTest();
        suggestionTextTest();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(weatherRule.getActivity());
        Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
        BaseTestLogUtility.judgeResponse(weather.status);
    }

    @After
    public void finishTest() throws Exception {
        weatherActivityTest.tearDown(simpleIdlingResource);
    }

}
