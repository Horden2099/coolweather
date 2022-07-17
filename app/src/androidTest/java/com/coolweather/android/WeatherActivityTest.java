package com.coolweather.android;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
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
        Thread.sleep(1000);
        onView(withId(R.id.nav_button)).perform(click());
//        onData(withItemContent("")).perform(click());
//        Thread.sleep(1000);
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
