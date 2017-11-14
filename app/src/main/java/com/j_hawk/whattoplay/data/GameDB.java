package com.j_hawk.whattoplay.data;
import android.provider.BaseColumns;
/**
 * Created by kevin on 10/24/2017.
 */

/**
 * GAMEDB.java
 * @author Kevin
 * @version 1.0
 * This class sets up the tables and columns for Game Database
 */
public final class GameDB {

    // Version should be changed IF any schemas are MODIFIED
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "database.db";

    /**
     * Constructor is private so the contract can never be somehow initialized
     */
    private GameDB() {
    }

    /**
     * {Class} GameCollection
     * This defines the columns for our GameCollection table
     *
     * @since 1.0
     */
    public static class GameCollection implements BaseColumns {
        public static final String TABLE_NAME = "gameCollection";
        public static final String COLUMN_NAME_GAME_NAME = "gameName";
        public static final String COLUMN_NAME_MIN_PLAYERS = "minPlayers";
        public static final String COLUMN_NAME_MAX_PLAYERS = "maxPlayers";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_PLAY_TIME = "playTime";
        public static final String COLUMN_NAME_THUMBNAIL = "thumbnail";
        public static final String COLUMN_NAME_MIN_PLAYER_AGE = "minPlayerAge";
        public static final String COLUMN_NAME_SUGGESTED_AGE = "suggestedMinPlayerAge";
        public static final String COLUMN_NAME_RECOMMENDED_PLAYERS = "recommendedPlayers";
        public static final String COLUMN_NAME_DESCRIPTION = "description";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_GAME_NAME + " TEXT," +
                COLUMN_NAME_MIN_PLAYERS + " INTEGER," +
                COLUMN_NAME_MAX_PLAYERS + " INTEGER," +
                COLUMN_NAME_YEAR + " INTEGER," +
                COLUMN_NAME_PLAY_TIME + " INTEGER," +
                COLUMN_NAME_THUMBNAIL + " TEXT," +
                COLUMN_NAME_MIN_PLAYER_AGE + " INTEGER," +
                COLUMN_NAME_SUGGESTED_AGE + " INTEGER," +
                COLUMN_NAME_RECOMMENDED_PLAYERS + " INTEGER," +
                COLUMN_NAME_DESCRIPTION + " TEXT);";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /**
     * {Class} GameMechanics
     * This defines the columns for our GameMechanics table
     *
     * @since 2.0
     */
    public static class GameMechanics implements BaseColumns {
        public static final String TABLE_NAME = "gameMechanics";
        public static final String COLUMN_NAME_GAME_ID = "gameID";
        public static final String COLUMN_NAME_MECHANIC = "mechanic";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_GAME_ID + " INTEGER," +
                COLUMN_NAME_MECHANIC + " TEXT);";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /**
     * {Class} GameCategories
     * This defines the columns for our GameCategories table
     *
     * @since 2.0
     */
    public static class GameCategories implements BaseColumns {
        public static final String TABLE_NAME = "gameCategories";
        public static final String COLUMN_NAME_GAME_ID = "gameID";
        public static final String COLUMN_NAME_CATEGORY = "category";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_GAME_ID + " INTEGER," +
                COLUMN_NAME_CATEGORY + " TEXT);";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
