package com.coolweather.android;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.test.SimpleIdlingResource;
import com.coolweather.android.util.Utility;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WeatherActivityTest {

    @Rule
    public ActivityTestRule<WeatherActivity> activityTestRule = new ActivityTestRule<>(WeatherActivity.class,true);

    SimpleIdlingResource mIdlingResource;

    @Before
    public void setup(){
        WeatherActivity activity = activityTestRule.getActivity();
        mIdlingResource = new SimpleIdlingResource(activity);//如果要测试需要去SimpleIdlingResource更改对应参数类
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void mainUITest() throws InterruptedException {
        //测试代码
        onView(withId(R.id.swipe_refresh)).perform(withCustomConstraints(swipeDown(),isDisplayingAtLeast(85)));//下拉刷新
        Thread.sleep(1000);
        onView(withId(R.id.nav_button)).perform(click());
        onView(withId(R.id.list_view)).perform(swipeUp());
        Thread.sleep(1000);
        chooseAreaTest1();
        Thread.sleep(1000);
        chooseAreaTest2();
        onView(withId(R.id.swipe_refresh)).perform(withCustomConstraints(swipeDown(),isDisplayingAtLeast(85)));//下拉刷新
        Thread.sleep(1000);
    }

    /**
     * 下拉刷新测试
     */
    @Test
    public void refreshTest() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.swipe_refresh)).perform(withCustomConstraints(swipeDown(),isDisplayingAtLeast(85)));//下拉刷新
        Thread.sleep(1000);
        titleTextShowTest();
        nowWeatherTextShowTest();
        forecastWeatherTextShowTest();
        suggestionTextTest();

        if (TestUtility.networkJudge(activityTestRule.getActivity().getBaseContext())){
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activityTestRule.getActivity());
            Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
            BaseTestLogUtility.judgeResponse(weather.status);
        }else {
            failGetInfo();
        }

        Thread.sleep(1000);
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
        if (!TestUtility.networkJudge(activityTestRule.getActivity().getBaseContext())){
            failGetInfo();
        }
    }

    /**
     * 测试选择导航返回键
     */
    private void chooseAreaTest2() throws InterruptedException {
        onView(withId(R.id.nav_button)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.back_button)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.back_button)).perform(click());
        Thread.sleep(1000);
        onData(hasToString(startsWith("上海")))
                .inAdapterView(withId(R.id.list_view)).atPosition(0)
                .perform(click());
        Thread.sleep(1000);
        onData(hasToString(startsWith("上海")))
                .inAdapterView(withId(R.id.list_view)).atPosition(0)
                .perform(click());
        Thread.sleep(1000);
        onData(hasToString(startsWith("上海")))
                .inAdapterView(withId(R.id.list_view)).atPosition(0)
                .perform(click());
        if (!TestUtility.networkJudge(activityTestRule.getActivity().getBaseContext())){
            failGetInfo();
        }
    }

    /**
     * sleep它有时有助于帮助.潜在的原因是，
     * 待刷的视图对用户仅有89％，而espresso的刷卡行动在内部要求90％.
     * 因此，解决方案是将滑动操作包装到另一个动作中并用手覆盖这些约束
     */
    public static ViewAction withCustomConstraints(final ViewAction action, final Matcher<View> constraints) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return constraints;
            }

            @Override
            public String getDescription() {
                return action.getDescription();
            }

            @Override
            public void perform(UiController uiController, View view) {
                action.perform(uiController, view);
            }
        };
    }
    /**
     * 用于测试listview配置Matcher
     * !!!暂时没用！！！
     */
    public static Matcher<Object> withItemContent(String expectedText) {
        checkNotNull(expectedText);
        return withItemContent(equalTo(expectedText));
    }
    /*
     * 以下为UI界面测试单元
     */
    /**
     * 测试城市名称文本显示
     */
    @Test
    public void titleTextShowTest(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activityTestRule.getActivity());
        Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
        onView(withId(R.id.title_city)).check(matches(withText(weather.basic.cityName)));
    }

    /**
     * 当前天气显示测试
     */
    @Test
    public void nowWeatherTextShowTest(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activityTestRule.getActivity());
        Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
        onView(withId(R.id.degree_text)).check(matches(withText(weather.now.temperature+"℃")));
        onView(withId(R.id.weather_info_text)).check(matches(withText(weather.now.more.info)));

        BaseTestLogUtility.judgeTem("实时温度", Integer.parseInt(weather.now.temperature));
        BaseTestLogUtility.judgeInfo("实时天气",weather.now.more.info);
    }

    /**
     * 预报显示测试
     */
    @Test
    public void forecastWeatherTextShowTest(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activityTestRule.getActivity());
        Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
        for (Forecast data : weather.forecastList){
            onView(withText(data.date)).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.max_text),hasSibling(withText(data.date)))).check(matches(withText(data.temperature.max)));
            onView(allOf(withId(R.id.min_text),hasSibling(withText(data.date)))).check(matches(withText(data.temperature.min)));
            onView(allOf(withId(R.id.info_text),hasSibling(withText(data.date)))).check(matches(withText(data.more.info)));

            BaseTestLogUtility.judgeTem("预测温度最大温度", Integer.parseInt(data.temperature.max));
            BaseTestLogUtility.judgeTem("预测温度最小温度", Integer.parseInt(data.temperature.min));
            BaseTestLogUtility.judgeInfo("预测天气",weather.now.more.info);
        }
    }

    /**
     * 空气质量显示测试
     */
    @Test
    public void AQITextShowTest(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activityTestRule.getActivity());
        Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
        onView(withId(R.id.aqi_text)).check(matches(withText(weather.aqi.city.aqi)));
        onView(withId(R.id.pm25_text)).check(matches(withText(weather.aqi.city.pm25)));
    }

    /**
     * 建议显示测试
     */
    @Test
    public void suggestionTextTest(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activityTestRule.getActivity());
        Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
        onView(withId(R.id.comfort_text)).check(matches(withText("舒适度：" + weather.suggestion.comfort.info)));
        onView(withId(R.id.car_wash_text)).check(matches(withText("洗车指数：" + weather.suggestion.carWash.info)));
        onView(withId(R.id.sport_text)).check(matches(withText("运行建议：" + weather.suggestion.sport.info)));
    }

    /**
     * 图片加载测试
     * @throws Exception
     */
    @Test
    public void loadImage() throws Exception {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activityTestRule.getActivity());
//        String url = "http://cn.bing.com/th?id=OHR.OmijimaIsland_ROW2080465862_1920x1080.jpg&rf=LaDigue_1920x1081920x1080.jpg";
        String url = prefs.getString("bing_pic",null);
        onView(withId(R.id.bing_pic_img)).check(matches(withContentDescription(url)));
        Thread.sleep(1000);
        onView(withId(R.id.nav_button)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }


    //////////////////////////////////////////////////////////////////////////////
    //********************重载测试函数以提供函数完成整体测试**************************//
    //////////////////////////////////////////////////////////////////////////////

    public void setup(ActivityTestRule rule,SimpleIdlingResource SIR){
        WeatherActivity activity = (WeatherActivity) rule.getActivity();
        SIR = new SimpleIdlingResource(activity);//如果要测试需要去SimpleIdlingResource更改对应参数类
        IdlingRegistry.getInstance().register(SIR);
    }
    /**
     * 测试城市名称文本显示
     */
    public void titleTextShowTest(ActivityTestRule rule){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(rule.getActivity());
        Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
        onView(withId(R.id.title_city)).check(matches(withText(weather.basic.cityName)));
    }

    /**
     * 当前天气显示测试
     */
    public void nowWeatherTextShowTest(ActivityTestRule rule){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(rule.getActivity());
        Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
        onView(withId(R.id.degree_text)).check(matches(withText(weather.now.temperature+"℃")));
        onView(withId(R.id.weather_info_text)).check(matches(withText(weather.now.more.info)));

        BaseTestLogUtility.judgeTem("实时温度", Integer.parseInt(weather.now.temperature));
        BaseTestLogUtility.judgeInfo("实时天气",weather.now.more.info);
    }

    /**
     * 预报显示测试
     */
    public void forecastWeatherTextShowTest(ActivityTestRule rule){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(rule.getActivity());
        Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
        for (Forecast data : weather.forecastList){
            onView(withText(data.date)).check(matches(isDisplayed()));
            onView(allOf(withId(R.id.max_text),hasSibling(withText(data.date)))).check(matches(withText(data.temperature.max)));
            onView(allOf(withId(R.id.min_text),hasSibling(withText(data.date)))).check(matches(withText(data.temperature.min)));
            onView(allOf(withId(R.id.info_text),hasSibling(withText(data.date)))).check(matches(withText(data.more.info)));

            BaseTestLogUtility.judgeTem("预测温度最大温度", Integer.parseInt(data.temperature.max));
            BaseTestLogUtility.judgeTem("预测温度最小温度", Integer.parseInt(data.temperature.min));
            BaseTestLogUtility.judgeInfo("预测天气",weather.now.more.info);
        }
    }

    /**
     * 空气质量显示测试
     */
    public void AQITextShowTest(ActivityTestRule rule){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(rule.getActivity());
        Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
        onView(withId(R.id.aqi_text)).check(matches(withText(weather.aqi.city.aqi)));
        onView(withId(R.id.pm25_text)).check(matches(withText(weather.aqi.city.pm25)));
    }

    /**
     * 建议显示测试
     */
    public void suggestionTextTest(ActivityTestRule rule){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(rule.getActivity());
        Weather weather = Utility.handleWeatherResponse(sp.getString("weather",null));
        onView(withId(R.id.comfort_text)).check(matches(withText("舒适度：" + weather.suggestion.comfort.info)));
        onView(withId(R.id.car_wash_text)).check(matches(withText("洗车指数：" + weather.suggestion.carWash.info)));
        onView(withId(R.id.sport_text)).check(matches(withText("运行建议：" + weather.suggestion.sport.info)));
    }

    /**
     * 图片加载测试
     * @throws Exception
     */
    public void loadImage(ActivityTestRule rule) throws Exception {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rule.getActivity());
//        String url = "http://cn.bing.com/th?id=OHR.OmijimaIsland_ROW2080465862_1920x1080.jpg&rf=LaDigue_1920x1081920x1080.jpg";
        String url = prefs.getString("bing_pic",null);
        onView(withId(R.id.bing_pic_img)).check(matches(withContentDescription(url)));
        Thread.sleep(1000);
        onView(withId(R.id.nav_button)).perform(click());
    }

    /**
     * 注销
     * @param SIR
     */
    public void tearDown(SimpleIdlingResource SIR) {
        IdlingRegistry.getInstance().unregister(SIR);
    }

    /**
     * 打开设置页面
     */
    public void openSet(){
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setClassName("com.android.phone","com.android.phone.MobileNetworkSettings");
        activityTestRule.getActivity().startActivity(intent);
    }

    public void failGetInfo(){
        onView(withText("获取天气信息失败"))
                .inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        BaseTestLogUtility.showWeatherActivityTestSituation("获取天气信息失败");
        BaseTestLogUtility.judgeResponse("");
    }

}