package com.j_hawk.whattoplay.services;


import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.j_hawk.whattoplay.AddToCollection;
import com.j_hawk.whattoplay.data.Game;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestFindGameByID  {

    @Rule
    public ActivityTestRule<AddToCollection> findGameByIdRule = new ActivityTestRule<>(AddToCollection.class, false, false);

    private Game expectedGame;
    private Game testGame;
    private FindGameByID testTask;

    @Before
    public void setup() {
        findGameByIdRule.launchActivity(new Intent());
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Ancient");
        categories.add("Card Game");
        categories.add("City Building");
        categories.add("Civilization");
        ArrayList<String> mechanics = new ArrayList<>();
        mechanics.add("Card Drafting");
        mechanics.add("Hand Management");
        mechanics.add("Set Collection");
        mechanics.add("Simultaneous Action Selection");
        mechanics.add("Variable Player Powers");
        String desricption = "You are the leader of one of the 7 great cities of the Ancient World. Gather resources, develop commercial routes, " +
                "and affirm your military supremacy. Build your city and erect an architectural wonder which will transcend future "+
                "times.&amp;#10;&amp;#10;7 Wonders lasts three ages. In each age, players receive seven cards from a particular deck,"+
                " choose one of those cards, then pass the remainder to an adjacent player. Players reveal their cards simultaneously,"+
                " paying resources if needed or collecting resources or interacting with other players in various ways. (Players have "+
                "individual boards with special powers on which to organize their cards, and the boards are double-sided). Each player "+
                "then chooses another card from the deck they were passed, and the process repeats until players have six cards in play "+
                "from that age. After three ages, the game ends.&amp;#10;&amp;#10;In essence, 7 Wonders is a card development game. Some "+
                "cards have immediate effects, while others provide bonuses or upgrades later in the game. Some cards provide discounts"+
                " on future purchases. Some provide military strength to overpower your neighbors and others give nothing but victory "+
                "points. Each card is played immediately after being drafted, so you'll know which cards your neighbor is receiving and"+
                " how his choices might affect what you've already built up. Cards are passed left-right-left over the three ages, "+
                "so you need to keep an eye on the neighbors in both directions.&amp;#10;&amp;#10;Though the box of earlier editions "+
                "is listed as being for 3&amp;ndash;7 players, there is an official 2-player variant included in the instructions."+
                "&amp;#10;&amp;#10;";
        desricption = desricption.replaceAll("&amp;#10;", "\n");
        expectedGame = new Game(68448, "7 Wonders", 2, 7, 2010, 30, "https://cf.geekdo-images.com/images/pic860217_t.jpg",
                10, 10, categories, mechanics, 4, desricption);
    }

    @Test
    public void testFoundValidGame() throws Throwable{

        testTask = new FindGameByID();
        testGame = testTask.execute(68448).get();

        assertEquals("Game from API did not mtach expected game", expectedGame, testGame);

    }

    @Test
    public void testNoValidGame() throws Throwable{

        testTask = new FindGameByID();
        testGame = testTask.execute(-1).get();

        assertEquals("Game from API did not mtach expected game", null, testGame);

    }

}
