package com.coolweather.android;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.coolweather.android.test.LoadImageActivity;
import com.coolweather.android.test.SimpleIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoadImageActivityTest {

    @Rule
    public ActivityTestRule<LoadImageActivity> mActivityRule = new ActivityTestRule<>(LoadImageActivity.class);

    SimpleIdlingResource mIdlingResource;

    @Before
    public void setUp() throws Exception {
        LoadImageActivity activity = mActivityRule.getActivity();
        mIdlingResource = new SimpleIdlingResource(activity);//如果要测试需要去SimpleIdlingResource更改对应参数类
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void loadImage() throws Exception {
        String url = "http://pic21.photophoto.cn/20111019/0034034837110352_b.jpg";
        onView(withId(R.id.iv_test)).check(matches(withContentDescription(url)));
    }
}
