package com.j_hawk.whattoplay;

import com.j_hawk.whattoplay.data.DBHelper;
import com.j_hawk.whattoplay.data.Game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;



@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TestDatabase {

    DBHelper dbHelper;
    ArrayList<String> mechanics1 = new ArrayList<>();
    ArrayList<String> categories1 = new ArrayList<>();
    ArrayList<String> mechanics2 = new ArrayList<>();
    ArrayList<String> categories2 = new ArrayList<>();
    Game game1;
    Game game2;

    @Before
    public void setup() {
        dbHelper = new DBHelper(RuntimeEnvironment.application);
        dbHelper.rebuildDatabase();
        categories1.add("Card Game");
        categories1.add("Science Fiction");
        mechanics1.add("Card Drafting");
        mechanics1.add("Variable Player Powers");
        categories2.add("City Building");
        categories2.add("Civilization");
        mechanics2.add("Action Point Allowance System");
        mechanics2.add("Hand Management");
        game1 = new Game(111, "Test Game1", 2, 4, 2000, 75, "thumbnail1", 7, 8, categories1, mechanics1, 3, "Still a test Game1");
        game2 = new Game(112, "Test Game2", 3, 7, 2001, 190, "thumbnail2", 15, 17, categories2, mechanics2, 5, "Still a test Game2");
    }

    @After
    public void teardown() {
        dbHelper.close();
    }

    @Test
    public void test_add_games() {
        dbHelper.rebuildDatabase();
        dbHelper.addGame(game1);
        ArrayList<Game> games = dbHelper.getAllGames();
        assertEquals("Number of games failed", 1, games.size());
        assertEquals("First game failed", games.get(0), game1);
        dbHelper.addGame(game2);
        games = dbHelper.getAllGames();
        assertEquals("Number of games returned failed", 2, games.size());
        assertEquals("Second game failed", game2, games.get(1));
    }

    @Test
    public void test_remove_game() {
        dbHelper.rebuildDatabase();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        dbHelper.removeGame(111);
        ArrayList<Game> games = dbHelper.getAllGames();
        assertEquals(games.size(), 1);
    }

    @Test
    public void add_same_game_twice() {
        dbHelper.rebuildDatabase();
        dbHelper.addGame(game1);
        long result = dbHelper.addGame(game1);
        ArrayList<Game> games = dbHelper.getAllGames();
        assertEquals(-1, result);
        assertEquals(games.size(), 1);
    }

    @Test
    public void test_get_all_mechanics() {
        dbHelper.rebuildDatabase();
        dbHelper.addGame(game1);
        ArrayList<String> testMech = dbHelper.getAllMechanics();
        assertEquals(mechanics1, testMech);

        dbHelper.addGame(game2);
        testMech = dbHelper.getAllMechanics();
        ArrayList<String> mechanics1and2 = new ArrayList<>();
        mechanics1and2.addAll(mechanics1);
        mechanics1and2.addAll(mechanics2);
        Collections.sort(mechanics1and2);
        assertEquals(mechanics1and2, testMech);
    }

    @Test
    public void test_get_all_categories() {
        dbHelper.rebuildDatabase();
        dbHelper.addGame(game1);
        ArrayList<String> testCats = dbHelper.getAllCategories();
        assertEquals("Testing with one game failed", categories1, testCats);

        dbHelper.addGame(game2);
        testCats = dbHelper.getAllCategories();
        ArrayList<String> categories1and2 = new ArrayList<>();
        categories1and2.addAll(categories1);
        categories1and2.addAll(categories2);
        Collections.sort(categories1and2);
        assertEquals("Testing with two games failed", categories1and2, testCats);
    }

    @Test
    public void test_get_mechanics() {
        dbHelper.rebuildDatabase();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        ArrayList<String> resultMechs = dbHelper.getMechanics(111);
        assertEquals(mechanics1, resultMechs);
        resultMechs = dbHelper.getMechanics(112);
        assertEquals(mechanics2, resultMechs);
    }

    @Test
    public void test_get_categories() {
        dbHelper.rebuildDatabase();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        ArrayList<String> resultCats = dbHelper.getCategories(111);
        assertEquals(categories1, resultCats);
        resultCats = dbHelper.getCategories(112);
        assertEquals(categories2, resultCats);
    }

    @Test
    public void test_get_games_by_players() {
        dbHelper.rebuildDatabase();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
    }
}
