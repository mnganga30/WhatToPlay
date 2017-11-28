package com.j_hawk.whattoplay.services;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.j_hawk.whattoplay.AddToCollection;
import com.j_hawk.whattoplay.ImportCollection;
import com.j_hawk.whattoplay.data.OnlineGame;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestSvcImportCollection {

    @Rule
    public ActivityTestRule<ImportCollection> findGameByQuerydRule = new ActivityTestRule<>(ImportCollection.class, false, false);

    private ArrayList<OnlineGame> expectedGames;
    private ArrayList<OnlineGame> testGames;
    private SvcImportCollection testTask;
    private static final String USER_NAME = "Team JHawk";

    @Before
    public void setup() {

        findGameByQuerydRule.launchActivity(new Intent());

        expectedGames = new ArrayList<>();
        expectedGames.add(new OnlineGame(68448, "7 Wonders", 2010));
        expectedGames.add(new OnlineGame(39339, "Android", 2008));
        expectedGames.add(new OnlineGame(37111, "Battlestar Galactica: The Board Game", 2008));
        expectedGames.add(new OnlineGame(164153, "Star Wars: Imperial Assault", 2014));
        expectedGames.add(new OnlineGame(187645, "Star Wars: Rebellion", 2016));
        testTask = new SvcImportCollection();
    }

    @Test
    public void testSvcImportCollection() throws Throwable{
        testGames = testTask.execute(USER_NAME).get();
        assertEquals(testGames.size(), expectedGames.size());
        boolean allEqual = true;
        int numberOfBadGames = 0;
        for (int i = 0; i < testGames.size(); i++) {
            if (!testGames.get(i).equals(expectedGames.get(i))) {
                allEqual = false;
                numberOfBadGames++;
                Log.d("TestImportCollection", "Test Game:\n" + testGames.get(i).toString() + "\n");
                Log.d("TestImportCollection",  "did not match:\n" + expectedGames.get(i).toString());
            }
        }
        assertTrue(numberOfBadGames + " games were not the same", allEqual);
    }
}
