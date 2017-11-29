package com.j_hawk.whattoplay.services;


import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.j_hawk.whattoplay.AddToCollection;
import com.j_hawk.whattoplay.HomePageActivity;
import com.j_hawk.whattoplay.data.OnlineGame;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestFindHotItems {

    @Rule
    public ActivityTestRule<HomePageActivity> findGameByQuerydRule = new ActivityTestRule<>(HomePageActivity.class, false, false);

    private ArrayList<OnlineGame> testGames;
    private FindHotItems testTask;

    @Before
    public void setup() {

        findGameByQuerydRule.launchActivity(new Intent());

        testTask = new FindHotItems();
    }

    @Test
    public void testFiftyGamesReturned() throws Throwable{
        testGames = testTask.execute().get();

        assertEquals("50 games were not returned", 50, testGames.size());
    }
}
