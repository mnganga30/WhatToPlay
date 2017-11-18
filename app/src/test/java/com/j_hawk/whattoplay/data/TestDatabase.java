package com.j_hawk.whattoplay.data;

import com.j_hawk.whattoplay.BuildConfig;
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
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;



@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TestDatabase {

    private DBHelper dbHelper;
    private ArrayList<String> mechanics1 = new ArrayList<>();
    private ArrayList<String> categories1 = new ArrayList<>();
    private ArrayList<String> mechanics2 = new ArrayList<>();
    private ArrayList<String> categories2 = new ArrayList<>();
    private Game game1;
    private Game game2;

    @Before
    public void setup() {
        dbHelper = new DBHelper(RuntimeEnvironment.application);
        dbHelper.rebuildDatabase();
        categories1.add("Card Game");
        categories1.add("Science Fiction");
        mechanics1.add("Card Drafting");
        mechanics1.add("Hand Management");
        mechanics1.add("Variable Player Powers");
        categories2.add("Card Game");
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
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        ArrayList<Game> games = dbHelper.getAllGames();
        assertEquals("Added one game. Number of games failed", 1, games.size());
        assertEquals("First game was not found", game1, games.get(0));
        dbHelper.addGame(game2);
        games = dbHelper.getAllGames();
        assertEquals("Added two games. Number of games returned failed", 2, games.size());
        assertEquals("Second game was not found", game2, games.get(1));
    }

    @Test
    public void test_remove_game() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        dbHelper.removeGame(111);
        ArrayList<Game> games = dbHelper.getAllGames();
        assertEquals("Added two games and removed one game failed", 1, games.size());
        assertEquals("Remaining game was not game2", game2, games.get(0));
    }

    @Test
    public void add_same_game_twice() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        long result = dbHelper.addGame(game1);
        ArrayList<Game> games = dbHelper.getAllGames();
        assertEquals("Added same game twice. Insert did not return -1", -1, result);
        assertEquals("Added same game twice. Size failed", 1, games.size());
    }

    @Test
    public void test_get_all_mechanics() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        ArrayList<String> testMech = dbHelper.getAllMechanics();
        assertEquals(mechanics1, testMech);

        dbHelper.addGame(game2);
        testMech = dbHelper.getAllMechanics();
        Set<String> mechanics1and2 = new TreeSet<>();
        mechanics1and2.addAll(mechanics1);
        mechanics1and2.addAll(mechanics2);
        ArrayList<String> combinedMechs = new ArrayList<>(mechanics1and2);
        assertEquals(combinedMechs, testMech);
    }

    @Test
    public void test_get_all_categories() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        ArrayList<String> testCats = dbHelper.getAllCategories();
        assertEquals("Testing with one game failed", categories1, testCats);

        dbHelper.addGame(game2);
        testCats = dbHelper.getAllCategories();
        Set<String> categories1and2 = new TreeSet<>();
        categories1and2.addAll(categories1);
        categories1and2.addAll(categories2);
        ArrayList<String> combinedCats = new ArrayList<>(categories1and2);
        assertEquals("Testing with two games failed", combinedCats, testCats);
    }

    @Test
    public void test_get_mechanics() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        ArrayList<String> resultMechs = dbHelper.getMechanics(111);
        assertEquals("Getting mechanics for game1 failed", mechanics1, resultMechs);
        resultMechs = dbHelper.getMechanics(112);
        assertEquals("Getting mechanics for game2 failed", mechanics2, resultMechs);
    }

    @Test
    public void test_get_categories() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        ArrayList<String> resultCats = dbHelper.getCategories(111);
        assertEquals("Getting categories for game1 failed", categories1, resultCats);
        resultCats = dbHelper.getCategories(112);
        assertEquals("Getting categories for game2 failed", categories2, resultCats);
    }

    @Test
    public void test_get_games_by_players() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        ArrayList<Game> games = dbHelper.getGamesByPlayers(2);
        assertEquals("getGamesByPlayers did not return 1 game", 1, games.size());
        assertEquals("Test for 2 player game failed", game1, games.get(0));

        games = dbHelper.getGamesByPlayers(4);
        assertEquals("getGamesByPlayers did not return 2 games", 2, games.size());
        assertEquals("Test for 4 player game1 failed", game1, games.get(0));
        assertEquals("Test for 4 player game2 failed", game2, games.get(1));

        games = dbHelper.getGamesByPlayers(6);
        assertEquals("getGamesByPlayers did not return any games", 1, games.size());
        assertEquals("Test for 6 player game failed", game2, games.get(0));
    }

    @Test
    public void test_get_games_by_filter_numOfPlayers_bggFalse() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        ArrayList<Game> games = dbHelper.getGamesByFilter(2, false, null, null, null, null, null);
        assertEquals("getGamesByPlayers did not return 1 game", 1, games.size());
        assertEquals("Test for 2 player game failed", game1, games.get(0));

        games = dbHelper.getGamesByFilter(4, false, null, null, null, null, null);
        assertEquals("getGamesByPlayers did not return 2 games", 2, games.size());
        assertEquals("Test for 4 player game1 failed", game1, games.get(0));
        assertEquals("Test for 4 player game2 failed", game2, games.get(1));

        games = dbHelper.getGamesByFilter(6, false, null, null, null, null, null);
        assertEquals("getGamesByPlayers did not return any games", 1, games.size());
        assertEquals("Test for 6 player game failed", game2, games.get(0));
    }

    @Test
    public void test_get_games_by_filter_numOfPlayers_bggTrue() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        ArrayList<Game> games = dbHelper.getGamesByFilter(2, true, null, null, null, null, null);
        assertEquals("getGamesByFilter did not return 0 games", 0, games.size());

        games = dbHelper.getGamesByFilter(6, true, null, null, null, null, null);
        assertEquals("getGamesByFilter did not return 0 games", 0, games.size());

        games = dbHelper.getGamesByFilter(3, true, null, null, null, null, null);
        assertEquals("getGamesByFilter did not return 1 game1", 1, games.size());
        assertEquals("Test for 3 player game1 failed", game1, games.get(0));

        games = dbHelper.getGamesByFilter(5, true, null, null, null, null, null);
        assertEquals("getGamesByPlayers did not return any games", 1, games.size());
        assertEquals("Test for 5 player game failed", game2, games.get(0));
    }

    @Test
    public void test_get_games_by_filter_playingTime() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        ArrayList<Game> games = dbHelper.getGamesByFilter(null, null, "0-60", null, null, null, null);
        assertEquals("getGamesByFilter did not return 0 games", 0, games.size());

        games = dbHelper.getGamesByFilter(null, null, "60-120", null, null, null, null);
        assertEquals("getGamesByFilter did not return 1 game1", 1, games.size());
        assertEquals("Test for 60-120 minutes playingtime failed", game1, games.get(0));

        games = dbHelper.getGamesByFilter(null, null, "120+", null, null, null, null);
        assertEquals("getGamesByFilter did not return 1 game2", 1, games.size());
        assertEquals("Test for 120+ minutes playingtime failed", game2, games.get(0));
    }

    @Test
    public void test_get_games_by_filter_minPlayerAge_bggFalse() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        ArrayList<Game> games = dbHelper.getGamesByFilter(null, null, null, 5, false, null, null);
        assertEquals("getGamesByFilter did not return 0 games", 0, games.size());

        games = dbHelper.getGamesByFilter(null, null, null, 7, false, null, null);
        assertEquals("getGamesByFilter did not return 1 game1", 1, games.size());
        assertEquals("Test for minPlayerAge <= 7 failed", game1, games.get(0));

        games = dbHelper.getGamesByFilter(null, null, null, 8, false, null, null);
        assertEquals("getGamesByFilter did not return 1 game1", 1, games.size());
        assertEquals("Test for minPlayerAge <= 8 failed", game1, games.get(0));

        games = dbHelper.getGamesByFilter(null, null, null, 16, false, null, null);
        assertEquals("getGamesByFilter did not return 1 game1", 2, games.size());
        assertEquals("Test for minPlayerAge <= 16 failed on game1", game1, games.get(0));
        assertEquals("Test for minPlayerAge <= 16 failed on game2", game2, games.get(1));
    }

    @Test
    public void test_get_games_by_filter_minPlayerAge_bggTrue() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);
        ArrayList<Game> games = dbHelper.getGamesByFilter(null, null, null, 5, true, null, null);
        assertEquals("getGamesByFilter did not return 0 games", 0, games.size());

        games = dbHelper.getGamesByFilter(null, null, null, 8, true, null, null);
        assertEquals("getGamesByFilter did not return 1 game1", 1, games.size());
        assertEquals("Test for minPlayerAge <= 7 failed", game1, games.get(0));

        games = dbHelper.getGamesByFilter(null, null, null, 12, true, null, null);
        assertEquals("getGamesByFilter did not return 1 game1", 1, games.size());
        assertEquals("Test for minPlayerAge <= 8 failed", game1, games.get(0));

        games = dbHelper.getGamesByFilter(null, null, null, 18, true, null, null);
        assertEquals("getGamesByFilter did not return 1 game1", 2, games.size());
        assertEquals("Test for minPlayerAge <= 16 failed on game1", game1, games.get(0));
        assertEquals("Test for minPlayerAge <= 16 failed on game2", game2, games.get(1));
    }

    @Test
    public void test_get_games_by_filter_mechanics() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);

        ArrayList<String> testMech = new ArrayList<>();
        testMech.add("Card Drafting");
        ArrayList<Game> games = dbHelper.getGamesByFilter(null, null, null, null, null, testMech, null);
        assertEquals("getGamesByFilter did not return 1 game", 1, games.size());
        assertEquals("Test for Card Drafting mechanic failed", game1, games.get(0));

        testMech.clear();
        testMech.add("Hand Management");
        games = dbHelper.getGamesByFilter(null, null, null, null, null, testMech, null);
        assertEquals("getGamesByFilter did not return 2 games", 2, games.size());
        assertEquals("Test for Hand Management mechanic failed for game1", game1, games.get(0));
        assertEquals("Test for Hand Management mechanic failed for game2", game2, games.get(1));

        testMech.clear();
        testMech.add("Not A Mechanic");
        games = dbHelper.getGamesByFilter(null, null, null, null, null, testMech, null);
        assertEquals("getGamesByFilter did not return 0 games", 0, games.size());
    }

    @Test
    public void test_get_games_by_filter_categories() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);

        ArrayList<String> testCats = new ArrayList<>();
        testCats.add("City Building");
        ArrayList<Game> games = dbHelper.getGamesByFilter(null, null, null, null, null, null, testCats);
        assertEquals("getGamesByFilter did not return 1 game", 1, games.size());
        assertEquals("Test for City Building category failed", game2, games.get(0));

        testCats.clear();
        testCats.add("Card Game");
        games = dbHelper.getGamesByFilter(null, null, null, null, null, null, testCats);
        assertEquals("getGamesByFilter did not return 2 games", 2, games.size());
        assertEquals("Test for Card Game category failed for game1", game1, games.get(0));
        assertEquals("Test for Card Game category failed for game2", game2, games.get(1));

        testCats.clear();
        testCats.add("Not A Category");
        games = dbHelper.getGamesByFilter(null, null, null, null, null, null, testCats);
        assertEquals("getGamesByFilter did not return 0 games", 0, games.size());
    }

    @Test
    public void test_get_games_by_filter_allParameters() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);

        ArrayList<String> testMech = new ArrayList<>();
        testMech.add("Card Drafting");
        ArrayList<String> testCats = new ArrayList<>();
        testCats.add("Card Game");

        ArrayList<Game> games = dbHelper.getGamesByFilter(4, false, "60-120", 14, true, testMech, testCats);
        assertEquals("getGamesByFilter did not return 1 game1", 1, games.size());
        assertEquals("Test with all parameters failed for game1", game1, games.get(0));
    }

    @Test
    public void test_get_games_by_filter_allParamNull() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);

        ArrayList<Game> games = dbHelper.getGamesByFilter(null, null, null, null, null, null, null);
        assertEquals("getGamesByFilter did not return null", null, games);
    }

    @Test
    public void test_get_games_by_filter_bggNull() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);

        ArrayList<Game> games = dbHelper.getGamesByFilter(5, null, null, null, null, null, null);
        assertEquals("getGamesByFilter failed for numplayers not null but bggNumPlayers null", null, games);

        games = dbHelper.getGamesByFilter(null, null, null, 5, null, null, null);
        assertEquals("getGamesByFilter failed for minPlayerAge not null but bggPlayerAge null", null, games);
    }

    @Test
    public void test_get_games_by_filter_arrayListEmpty() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);

        ArrayList<String> testMech = new ArrayList<>();
        ArrayList<String> testCats = new ArrayList<>();

        ArrayList<Game> games = dbHelper.getGamesByFilter(null, null, null, null, null, testMech, null);
        assertEquals("getGamesByFilter failed for mechanics empty and no other parameters", null, games);

        games = dbHelper.getGamesByFilter(null, null, null, null, null, null, testCats);
        assertEquals("getGamesByFilter failed for empty categories and no other parameters", null, games);
    }

    @Test
    public void test_get_game_by_id() {
        dbHelper.deleteAllEntries();
        dbHelper.addGame(game1);
        dbHelper.addGame(game2);

        Game game = dbHelper.getGameByID(111);
        assertEquals("Test failed for getting game by id", game1, game);
    }
}
