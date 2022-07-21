package com.coolweather.android;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.coolweather.android.test.TestNetworkActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NetworkTest {

    @Rule
    public ActivityTestRule<TestNetworkActivity> rule = new ActivityTestRule<>(TestNetworkActivity.class,true);

    @Test
    public void aTest() throws InterruptedException {
        onView(withId(R.id.btn_network)).perform(click());
        Thread.sleep(1000);
        //验证提示弹窗是否弹出
        onView(withText(containsString("开启或关闭蓝牙")))
                .inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        //点击弹窗的确认按钮
        onView(withText("仅允许一次"))
                .inRoot(withDecorView(not(is(rule.getActivity().getWindow().getDecorView()))))
                .perform(click());
        Assert.assertTrue(rule.getActivity().isFinishing());
    }
}
