package com.j_hawk.whattoplay;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomePageActivityTest {

    @Rule
    public ActivityTestRule<HomePageActivity> myhomepageactivity = new ActivityTestRule<>(HomePageActivity.class, false, false);
    @Before
    public void setup() {
        myhomepageactivity.launchActivity(new Intent());
        onView(withId(R.id.navigation)).perform(click());
    }

    @Test
    public void testBottomNavigationViewSwitch() throws Throwable {
        onView(withId(R.id.navigation_search)).perform(click());
        onView(withId(R.id.navigation_home)).perform(click());
        onView(withId(R.id.navigation_recommendations)).perform(click());
    }

    @Test
    public void testFragement1() throws Throwable {
        onView(withId(R.id.toolbar2)).perform(click());
    }

    @Test
    public void testFragement2() throws Throwable {
        onView(withId(R.id.navigation_recommendations)).perform(click());
    }

    @Test
    public void testFragement3() throws Throwable {
      onView(withId(R.id.pager)).perform(click());
    }

    @After
    public void teardown() {
        myhomepageactivity.getActivity().finish();
    }
}

