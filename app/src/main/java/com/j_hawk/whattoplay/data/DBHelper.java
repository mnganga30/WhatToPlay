package com.j_hawk.whattoplay.data;

/**
 * Created by kevin on 10/24/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

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

    /**
     * Drops and recreates tables. Used for unit testing.
     */
    public void rebuildDatabase() {
        // Delete all tables
        Log.i("sql", "Rebuilding DB");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(GameDB.GameCollection.DROP_TABLE);
        db.execSQL((GameDB.GameMechanics.DROP_TABLE));
        db.execSQL(GameDB.GameCategories.DROP_TABLE);
        onCreate(db);
    }

    /**
     * Deletes all entries in all tables. Only used for unit testing.
     */
    public void deleteAllEntries() {
        Log.i("sql", "Deleting all entries");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ GameDB.GameCollection.TABLE_NAME);
        db.execSQL("delete from "+ GameDB.GameMechanics.TABLE_NAME);
        db.execSQL("delete from "+ GameDB.GameCategories.TABLE_NAME);
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
        try {
            dbRow = db.insertWithOnConflict(GameDB.GameCollection.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_ABORT);
        } catch (SQLiteException e) {
            return -1;
        }

        Log.i("test", "" + dbRow);
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

            int id, minPlayers, maxPlayers, year, playTime, minPlayerAge, recommendedPlayers, suggestedMinPlayerAge;
            String name, thumbnail, description;

            id = Integer.parseInt(queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection._ID)));
            name = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_GAME_NAME));
            minPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS));
            maxPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS));
            year = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_YEAR));
            playTime = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_PLAY_TIME));
            thumbnail = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_THUMBNAIL));
            minPlayerAge = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE));
            suggestedMinPlayerAge = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE));
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
    public ArrayList<String> getMechanics (int id) {

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
                GameDB.GameMechanics.COLUMN_NAME_GAME_ID + " = ?", args, null, null,
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
    public ArrayList<String> getCategories (int id) {

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
                GameDB.GameCategories.COLUMN_NAME_GAME_ID + " = ?", args, null, null,
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
     * @return -number of rows deleted.
     * @since 1.0
     */
    public int removeGame(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] args = new String[] {String.valueOf(id)};

        db.delete(GameDB.GameMechanics.TABLE_NAME, GameDB.GameMechanics.COLUMN_NAME_GAME_ID + "=?", args);
        db.delete(GameDB.GameCategories.TABLE_NAME, GameDB.GameCategories.COLUMN_NAME_GAME_ID + "=?", args);

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
            int id, minPlayers, maxPlayers, year, playTime, minPlayerAge, recommendedPlayers, suggestedMinPlayerAge;
            String name, thumbnail, description;

            id = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection._ID));
            name = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_GAME_NAME));
            minPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS));
            maxPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS));
            year = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_YEAR));
            playTime = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_PLAY_TIME));
            thumbnail = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_THUMBNAIL));
            minPlayerAge = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE));
            suggestedMinPlayerAge = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE));
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
     * This function will get all games from the GameCollection table that meet the passed filters. Parameters can be passed as null if no filtering is desired
     * on that parameter.
     * @param numOfPlayers The number players participating as an Integer.
     * @param bggNumPlayers Boolean used to determine if search will use manufacturer (false) or BGG (true) player count recommendation.
     *                      Must pass true or false if numPlayers is not null.
     * @param playingTime String representing playing time. Only accepts "0-60", "60-120", or "120+". Otherwise will return null.
     * @param minPlayerAge Integer representing the minimum player age.
     * @param bggPlayerAge Boolean used to determine if search will use manufacturer (false) or BGG (true) player age recommendation.
     *                     Must pass true or false if numPlayers is not null.
     * @param mechanics ArrayList of Strings containing all mechanics to be filtered against. Game must contain all mechanics to be returned.
     * @param categories ArrayList of Strings containing all categories to be filtered against. Game must contain all categories to be returned.
     * @return A sorted ArrayList of Games from the Database or null if no or incoreect parameters passed.
     * @since 2.0
     */
    public ArrayList<Game> getGamesByFilter(Integer numOfPlayers, Boolean bggNumPlayers, String playingTime,
                                            Integer minPlayerAge, Boolean bggPlayerAge,
                                            ArrayList<String> mechanics, ArrayList<String> categories) {

        // validate parameters
        // check if Boolean is passed with corresponding Integer
        if ((numOfPlayers != null && bggNumPlayers == null) || (minPlayerAge != null && bggPlayerAge == null)) return null;
        // make sure at least one parameter was passed
        if (numOfPlayers == null && playingTime == null && minPlayerAge == null &&
                (mechanics == null || mechanics.size() == 0) && (categories == null || categories.size() == 0)) return null;


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

        //sort order
        String sortOrderGame = GameDB.GameCollection.COLUMN_NAME_GAME_NAME + " COLLATE NOCASE ASC";

        // array list to returned games
        ArrayList<String> whereClasue = new ArrayList<>();

        // string to hold arguments to be passed to sql
        String args = "";

        // track the number of arguments that were passed
        int numOfArgs = 0;

        // check numOfPlayers parameter
        if (numOfPlayers != null) {
            numOfArgs++;
            if (!bggNumPlayers) {
                whereClasue.add(String.valueOf(numOfPlayers));
                whereClasue.add(String.valueOf(numOfPlayers));
                args += GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS + " <= ? AND " + GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS + " >= ?";
            } else {
                whereClasue.add(String.valueOf(numOfPlayers));
                args += GameDB.GameCollection.COLUMN_NAME_RECOMMENDED_PLAYERS + " = ?";
            }

        }

        // check playing time
        if (playingTime != null) {
            // link with and if not first argument
            if (numOfArgs != 0) {
                args += " AND ";
            }
            if (playingTime.equals("0-60")) {
                whereClasue.add("0");
                whereClasue.add(("60"));
                numOfArgs += 2;
                args += GameDB.GameCollection.COLUMN_NAME_PLAY_TIME + " >= ? AND " + GameDB.GameCollection.COLUMN_NAME_PLAY_TIME + " <= ?";
            } else if (playingTime.equals("60-120")) {
                whereClasue.add("60");
                whereClasue.add("120");
                numOfArgs += 2;
                args += GameDB.GameCollection.COLUMN_NAME_PLAY_TIME + " >= ? AND " + GameDB.GameCollection.COLUMN_NAME_PLAY_TIME + " <= ?";
            } else if (playingTime.equals("120+")){
                whereClasue.add("120");
                numOfArgs++;
                args += GameDB.GameCollection.COLUMN_NAME_PLAY_TIME + " >= ?";
            } else {
                return null;
            }

        }

        // check player age
        if (minPlayerAge != null) {

            if (numOfArgs != 0) {
                args += " AND ";
            }
            numOfArgs++;

            if (!bggPlayerAge) {
                args += GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE + " <= ?";
            } else {
                args += GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE + " <= ?";
            }
            whereClasue.add(String.valueOf(minPlayerAge));
        }

        // build where clause array
        String[] where = new String[whereClasue.size()];
        where = whereClasue.toArray(where);

        Cursor queryGame = db.query(
                GameDB.GameCollection.TABLE_NAME,
                columnsGame,
                args, where, null, null,
                sortOrderGame
        );

        ArrayList<Game> gameList = new ArrayList<>();

        while(queryGame.moveToNext()) {
            int id, minPlayers, maxPlayers, year, playTime, playerAge, recommendedPlayers, suggestedMinPlayerAge;
            String name, thumbnail, description;

            id = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection._ID));
            name = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_GAME_NAME));
            minPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS));
            maxPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS));
            year = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_YEAR));
            playTime = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_PLAY_TIME));
            thumbnail = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_THUMBNAIL));
            playerAge = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE));
            suggestedMinPlayerAge = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE));
            recommendedPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_RECOMMENDED_PLAYERS));
            description = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_DESCRIPTION));

            ArrayList<String> mechs = getMechanics(id);
            ArrayList<String> cats = getCategories(id);
            Game game = new Game(id, name, minPlayers, maxPlayers, year, playTime, thumbnail,
                    playerAge, suggestedMinPlayerAge, cats, mechs,
                    recommendedPlayers, description);

            boolean addGame = true;

            // check if any mechanics were passes
            if (mechanics != null && !mechanics.isEmpty()) {
                for (String mechanic : mechanics) {
                    if (!mechs.contains(mechanic)) {
                        addGame = false;
                    }
                }
            }

            // check if an categories were passed
            if (categories != null && !categories.isEmpty()) {
                for (String category : categories) {
                    if (!cats.contains(category)) {
                        addGame = false;
                    }
                }
            }

            if (addGame) {
                gameList.add(game);
            }
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
            int id, minPlayers, maxPlayers, year, playTime, minPlayerAge, recommendedPlayers, suggestedMinPlayerAge;
            String name, thumbnail, description;

            id = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection._ID));
            name = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_GAME_NAME));
            minPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYERS));
            maxPlayers = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MAX_PLAYERS));
            year = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_YEAR));
            playTime = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_PLAY_TIME));
            thumbnail = queryGame.getString(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_THUMBNAIL));
            minPlayerAge = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_MIN_PLAYER_AGE));
            suggestedMinPlayerAge = queryGame.getInt(queryGame.getColumnIndexOrThrow(GameDB.GameCollection.COLUMN_NAME_SUGGESTED_AGE));
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

    /**
     * This function will return an ArrayList of all unique mechanics
     * @return ArrayList of  unique mechanics
     * @since 2.0
     */
    public ArrayList<String> getAllMechanics() {

        Set<String> mechanics = new TreeSet<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnsMechanics = {
                GameDB.GameMechanics.COLUMN_NAME_MECHANIC
        };

        Cursor queryMechanics = db.query(
                GameDB.GameMechanics.TABLE_NAME,
                columnsMechanics,
                null, null, null, null, null
        );

        while(queryMechanics.moveToNext()) {
            String mechanic = queryMechanics.getString(queryMechanics.getColumnIndexOrThrow(GameDB.GameMechanics.COLUMN_NAME_MECHANIC));
            mechanics.add(mechanic);
        }

        queryMechanics.close();

        ArrayList<String> returnList = new ArrayList<>(mechanics);

        return returnList;
    }

    /**
     * This function will return an ArrayList of all unique categories
     * @return ArrayList of  unique categories
     * @since 2.0
     */
    public ArrayList<String> getAllCategories() {

        Set<String> categories = new TreeSet<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnsCategories = {
                GameDB.GameCategories.COLUMN_NAME_CATEGORY
        };

        Cursor queryCategories = db.query(
                GameDB.GameCategories.TABLE_NAME,
                columnsCategories,
                null, null, null, null, null
        );

        while(queryCategories.moveToNext()) {
            String category = queryCategories.getString(queryCategories.getColumnIndexOrThrow(GameDB.GameCategories.COLUMN_NAME_CATEGORY));
            categories.add(category);
        }

        queryCategories.close();

        ArrayList<String> returnList = new ArrayList<>(categories);

        return returnList;
    }
}
