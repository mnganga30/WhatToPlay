package com.j_hawk.whattoplay.data;

/**
 * Created by kevin on 10/24/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * DBHelper.java
 * @author Kevin
 * @version 1.0
 * This class contains helper methods that interact with the Database.
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * Default constructor for the DBHelper.
     * @param context Always the entire application context because we want the database to be for the whole application.
     */
    public DBHelper(Context context) {
        super(context, GameDB.DATABASE_NAME, null, GameDB.DATABASE_VERSION);
    }

    /**
     * Called when the DBHelper class is created. Will create database tables if they do not exist.
     * @param sqLiteDatabase Current writable database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create table
        sqLiteDatabase.execSQL(GameDB.GameCollection.CREATE_TABLE);
        sqLiteDatabase.execSQL(GameDB.GameMechanics.CREATE_TABLE);
        sqLiteDatabase.execSQL(GameDB.GameCategories.CREATE_TABLE);
    }

    /**
     * Called when the database version is changed in GameDB
     * @param sqLiteDatabase Current writable database
     * @param i Old version of DB. Set in GameDB
     * @param i1 New version of DB. Set in GameDB
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Delete all tables
        Log.i("sql", "Updating DB");
        sqLiteDatabase.execSQL(GameDB.GameCollection.DROP_TABLE);
        sqLiteDatabase.execSQL((GameDB.GameMechanics.DROP_TABLE));
        sqLiteDatabase.execSQL(GameDB.GameCategories.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    //region GameCollection Table Methods

    /**
     * This function will add a game to the GameCollection table
     * @param game the game object that is to be added
     * @return -1 for failure, otherwise will return the row inserted at.
     */
    public long addGame(Game game) {
        long dbRow;
        SQLiteDatabase db = this.getWritableDatabase(); // is this okay?

        ContentValues values = new ContentValues();
        values.put(GameDB.GameCollection._ID, game.getId());
        values.put(GameDB.GameCollection.COLUMN_NAME_GAME_NAME, game.getName());
        values.put(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS, game.getMinPlayers());
        values.put(GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS, game.getMaxPlayers());
        values.put(GameDB.GameCollection.COLUMN_NAME_YEAR, game.getYear());
        values.put(GameDB.GameCollection.COLUMN_NAME_PLAY_TIME, game.getPlayTime());
        values.put(GameDB.GameCollection.COLUMN_NAME_THUMBNAIL, game.getThumbnail());
        values.put(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE, game.getMinPlayerAge());
        values.put(GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE, game.getSuggestedMinPlayerAge());
        values.put(GameDB.GameCollection.COLUMN_NAME_RECOMMENDED_PLAYERS, game.getRecommendedPlayers());
        values.put(GameDB.GameCollection.COLUMN_NAME_DESCRIPTION, game.getDescription());
        dbRow = db.insertWithOnConflict(GameDB.GameCollection.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        if (dbRow != -1) {
            for (String mechanic : game.getMechanics()) {
                values.clear();
                values.put(GameDB.GameMechanics.COLUMN_NAME_GAME_ID, game.getId());
                values.put(GameDB.GameMechanics.COLUMN_NAME_MECHANIC, mechanic);
                db.insert(GameDB.GameMechanics.TABLE_NAME, null, values);
            }

            for (String category : game.getCategories()) {
                values.clear();
                values.put(GameDB.GameCategories.COLUMN_NAME_GAME_ID, game.getId());
                values.put(GameDB.GameCategories.COLUMN_NAME_CATEGORY, category);
                db.insert(GameDB.GameCategories.TABLE_NAME, null, values);
            }
        }
        return dbRow;
}

    /**
     * This function will get all games from the GameCollection table
     * @return A sorted ArrayList of Games from the Database
     * @since 1.0
     */
    public ArrayList<Game> getAllGames() {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Game> sortedListOfGames = new ArrayList<>();

        String[] columnsGame = {
                GameDB.GameCollection._ID,
                GameDB.GameCollection.COLUMN_NAME_GAME_NAME,
                GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS,
                GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS,
                GameDB.GameCollection.COLUMN_NAME_YEAR,
                GameDB.GameCollection.COLUMN_NAME_PLAY_TIME,
                GameDB.GameCollection.COLUMN_NAME_THUMBNAIL,
                GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE,
                GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE,
                GameDB.GameCollection.COLUMN_NAME_RECOMMENDED_PLAYERS,
                GameDB.GameCollection.COLUMN_NAME_DESCRIPTION
        };

        //Sort by name
        String sortOrderGame = GameDB.GameCollection.COLUMN_NAME_GAME_NAME + " COLLATE NOCASE ASC";

        Cursor queryGame = db.query(
                GameDB.GameCollection.TABLE_NAME,
                columnsGame,
                null, null, null, null,
                sortOrderGame
        );

        while (queryGame.moveToNext()) {

            int id, minPlayers, maxPlayers, year, playTime, minPlayerAge, recommendedPlayers;
            String name, thumbnail, description, suggestedMinPlayerAge;

            id = Integer.parseInt(queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection._ID)));
            name = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_GAME_NAME));
            minPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS));
            maxPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS));
            year = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_YEAR));
            playTime = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_PLAY_TIME));
            thumbnail = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_THUMBNAIL));
            minPlayerAge = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE));
            suggestedMinPlayerAge = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE));
            recommendedPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_RECOMMENDED_PLAYERS));
            description = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_DESCRIPTION));

            ArrayList<String> mechanics = getMechanics(id);
            ArrayList<String> categories = getCategories(id);

            //Create Game object from row and add to Vector
            Game game = new Game(id, name, minPlayers, maxPlayers, year, playTime, thumbnail,
                                minPlayerAge, suggestedMinPlayerAge, categories, mechanics,
                                recommendedPlayers, description);
            sortedListOfGames.add(game);
        }

        queryGame.close();

        return sortedListOfGames;
    }

    /**
     * This function will get all mechanics for pass id
     * @return ArrayList of the mechanics
     * @since 2.0
     */
    private ArrayList<String> getMechanics (int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> mechanics = new ArrayList<>();

        String[] args = new String[] {String.valueOf(id)};

        String[] columnsMechanics = {
                GameDB.GameMechanics.COLUMN_NAME_MECHANIC
        };

        String sortOrderMechanics = GameDB.GameMechanics.COLUMN_NAME_MECHANIC + " COLLATE NOCASE ASC";

        Cursor queryMechanics = db.query(
                GameDB.GameMechanics.TABLE_NAME,
                columnsMechanics,
                null, null, null, null,
                sortOrderMechanics
        );

        while (queryMechanics.moveToNext()) {
            String mechanic = queryMechanics.getString(queryMechanics.getColumnIndexOrThrow(GameDB.GameMechanics.COLUMN_NAME_MECHANIC));
            mechanics.add(mechanic);
        }

        queryMechanics.close();

        return mechanics;
    }

    /**
     * This function will get all categories for pass id
     * @return ArrayList of the mechanics
     * @since 2.0
     */
    private ArrayList<String> getCategories (int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> categories = new ArrayList<>();

        String[] args = new String[] {String.valueOf(id)};

        String[] columnsMechanics = {
                GameDB.GameCategories.COLUMN_NAME_CATEGORY
        };

        String sortOrderCategories = GameDB.GameCategories.COLUMN_NAME_CATEGORY + " COLLATE NOCASE ASC";

        Cursor queryCategories = db.query(
                GameDB.GameCategories.TABLE_NAME,
                columnsMechanics,
                null, null, null, null,
                sortOrderCategories
        );

        while (queryCategories.moveToNext()) {
            String category = queryCategories.getString(queryCategories.getColumnIndexOrThrow(GameDB.GameCategories.COLUMN_NAME_CATEGORY));
            categories.add(category);
        }

        queryCategories.close();

        return categories;
    }

    /**
     * This function will delete the queried game from the database
     * @return -1 for failure, otherwise will return the row inserted at.
     * @since 1.0
     */
    public int removeGame(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] args = new String[] {String.valueOf(id)};

        return db.delete(GameDB.GameCollection.TABLE_NAME, "_ID=?", args);
    }

    /**
     * This function will get all games from the GameCollection table that meet the player requirement
     * @param numOfPlayers The number players participating.
     * @return A sorted ArrayList of Games from the Database
     * @since 1.0
     */
    public ArrayList<Game> getGamesByPlayers(int numOfPlayers) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnsGame = {
                GameDB.GameCollection._ID,
                GameDB.GameCollection.COLUMN_NAME_GAME_NAME,
                GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS,
                GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS,
                GameDB.GameCollection.COLUMN_NAME_YEAR,
                GameDB.GameCollection.COLUMN_NAME_PLAY_TIME,
                GameDB.GameCollection.COLUMN_NAME_THUMBNAIL,
                GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE,
                GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE,
                GameDB.GameCollection.COLUMN_NAME_RECOMMENDED_PLAYERS,
                GameDB.GameCollection.COLUMN_NAME_DESCRIPTION
        };

        String[] where = {Integer.toString(numOfPlayers), Integer.toString(numOfPlayers)};

        String sortOrderGame = GameDB.GameCollection.COLUMN_NAME_GAME_NAME + " COLLATE NOCASE ASC";

        Cursor queryGame = db.query(
                GameDB.GameCollection.TABLE_NAME,
                columnsGame,
                "minPlayers <= ? AND maxPlayers >= ?", where, null, null,
                sortOrderGame
        );

        ArrayList<Game> gameList = new ArrayList<>();

        while(queryGame.moveToNext()) {
            int id, minPlayers, maxPlayers, year, playTime, minPlayerAge, recommendedPlayers;
            String name, thumbnail, description, suggestedMinPlayerAge;

            id = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection._ID));
            name = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_GAME_NAME));
            minPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS));
            maxPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS));
            year = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_YEAR));
            playTime = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_PLAY_TIME));
            thumbnail = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_THUMBNAIL));
            minPlayerAge = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE));
            suggestedMinPlayerAge = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE));
            recommendedPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_RECOMMENDED_PLAYERS));
            description = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_DESCRIPTION));

            ArrayList<String> mechanics = getMechanics(id);
            ArrayList<String> categories = getCategories(id);
            Game game = new Game(id, name, minPlayers, maxPlayers, year, playTime, thumbnail,
                    minPlayerAge, suggestedMinPlayerAge, categories, mechanics,
                    recommendedPlayers, description);
            gameList.add(game);
        }
        queryGame.close();

        return  gameList;
    }

    /**
     * This function will get a game from the GameCollection table that has the passed id
     * @param passed_id the id of the desired game
     * @return A Game object of the requested game
     * @since 1.0
     */
    public Game getGameByID(int passed_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnsGame = {
                GameDB.GameCollection._ID,
                GameDB.GameCollection.COLUMN_NAME_GAME_NAME,
                GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS,
                GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS,
                GameDB.GameCollection.COLUMN_NAME_YEAR,
                GameDB.GameCollection.COLUMN_NAME_PLAY_TIME,
                GameDB.GameCollection.COLUMN_NAME_THUMBNAIL,
                GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE,
                GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE,
                GameDB.GameCollection.COLUMN_NAME_RECOMMENDED_PLAYERS,
                GameDB.GameCollection.COLUMN_NAME_DESCRIPTION
        };

        String[] where = {Integer.toString(passed_id)};

        String sortOrderGame = GameDB.GameCollection.COLUMN_NAME_GAME_NAME + " COLLATE NOCASE ASC";

        Cursor queryGame = db.query(
                GameDB.GameCollection.TABLE_NAME,
                columnsGame,
                "_ID = ?", where, null, null,
                sortOrderGame
        );

        if (queryGame.moveToNext()) {
            int id, minPlayers, maxPlayers, year, playTime, minPlayerAge, recommendedPlayers;
            String name, thumbnail, description, suggestedMinPlayerAge;

            id = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection._ID));
            name = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_GAME_NAME));
            minPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS));
            maxPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS));
            year = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_YEAR));
            playTime = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_PLAY_TIME));
            thumbnail = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_THUMBNAIL));
            minPlayerAge = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE));
            suggestedMinPlayerAge = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE));
            recommendedPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_RECOMMENDED_PLAYERS));
            description = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_DESCRIPTION));

            ArrayList<String> mechanics = getMechanics(id);
            ArrayList<String> categories = getCategories(id);
            Game game = new Game(id, name, minPlayers, maxPlayers, year, playTime, thumbnail,
                    minPlayerAge, suggestedMinPlayerAge, categories, mechanics,
                    recommendedPlayers, description);

            queryGame.close();
            return  game;
        } else {
            return null;
        }

    }
}
