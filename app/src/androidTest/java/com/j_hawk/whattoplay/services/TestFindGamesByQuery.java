package com.j_hawk.whattoplay.services;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.j_hawk.whattoplay.AddToCollection;
import com.j_hawk.whattoplay.data.OnlineGame;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestFindGamesByQuery {

    @Rule
    public ActivityTestRule<AddToCollection> findGameByQuerydRule = new ActivityTestRule<>(AddToCollection.class, false, false);

    ArrayList<OnlineGame> expectedGames;
    ArrayList<OnlineGame> testGames;
    FindGamesByQuery testTask;

    @Before
    public void setup() {

        findGameByQuerydRule.launchActivity(new Intent());

        expectedGames = new ArrayList<>();
        expectedGames.add(new OnlineGame(173346, "7 Wonders Duel", 2015));
        expectedGames.add(new OnlineGame(202976, "7 Wonders Duel: Pantheon", 2016));
        expectedGames.add(new OnlineGame(196339, "7 Wonders Duel: Statue of Liberty", 2016));
        expectedGames.add(new OnlineGame(228690, "7 Wonders Duel: Stonehenge", 2017));
        expectedGames.add(new OnlineGame(186069, "7 Wonders Duel: The Messe Essen", 2015));
        testTask = new FindGamesByQuery();
    }

    @Test
    public void findValidGames() throws Throwable {
        testGames = testTask.execute("7-wonders-duel").get();
        assertEquals(testGames.size(), expectedGames.size());
        boolean allEqual = true;
        int numberOfBadGames = 0;
        for (int i = 0; i < testGames.size(); i++) {
            if (!testGames.get(i).equals(expectedGames.get(i))) {
                allEqual = false;
                numberOfBadGames++;
            }
        }
        assertTrue(numberOfBadGames + " games were not the same", allEqual);
    }

    @Test
    public void findNoGames() throws Throwable{
        testGames = testTask.execute("").get();
        assertEquals("Did not return 0 games", 0, testGames.size());
    }
}
