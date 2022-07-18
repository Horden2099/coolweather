package com.coolweather.android;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WeatherActivityTest {

    @Rule
    public ActivityTestRule<WeatherActivity> weatherActivity = new ActivityTestRule<>(WeatherActivity.class,true);

    @Test
    public void mainUITest() throws InterruptedException {
        onView(withId(R.id.swipe_refresh)).perform(withCustomConstraints(swipeDown(),isDisplayingAtLeast(85)));
        Thread.sleep(1000);
        onView(withId(R.id.nav_button)).perform(click());
        onView(withId(R.id.list_view)).perform(swipeUp());
        Thread.sleep(1000);
        chooseAreaTest1();
        Thread.sleep(1000);
        chooseAreaTest2();
        onView(withId(R.id.swipe_refresh)).perform(withCustomConstraints(swipeDown(),isDisplayingAtLeast(85)));
        Thread.sleep(1000);
//        onData(withItemContent("")).perform(click());
//        Thread.sleep(1000);
    }

    @Test
    public void testDialog(WeatherActivity weatherActivity) throws Exception {
        //按下返回键
        pressBack();
        //验证提示弹窗是否弹出
        onView(withText(containsString("确认退出应用吗")))
                .inRoot(withDecorView(not(is(weatherActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        //点击弹窗的确认按钮
        onView(withText("确认"))
                .inRoot(withDecorView(not(is(weatherActivity.getWindow().getDecorView()))))
                .perform(click());
        Assert.assertTrue(weatherActivity.isFinishing());
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
     */
    public static Matcher<Object> withItemContent(String expectedText) {
        checkNotNull(expectedText);
        return withItemContent(equalTo(expectedText));
    }
}
