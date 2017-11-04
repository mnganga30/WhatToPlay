package com.j_hawk.whattoplay.data;

/**
 * Created by kevin on 10/24/2017.
 */

/**
 * Game.java
 * @author Kevin, Simon, Jian, Martin
 * @version 1.0
 * This class holds the shape for all games that the GameDB holds.
 */
public class Game {
    private int id;
    private String name;
    private int minPlayers;
    private int maxPlayers;
    private int year;
    private int playTime;
    private String thumbnail;

    /**
     * Constructor for an Game. Games will always be constructed this way.
     * @param id Integer ID of the game
     * @param name String name of the game
     * @param minPlayers Integer minimum number of players
     * @param maxPlayers Integer maximum number of players
     * @param year Integer year the game was published
     * @param playTime Integer amount of time game takes to play in minutes
     * @param thumbnail String url provided by BoardGameGeek.com to the thimbnail of the image
     */
    public Game(int id, String name, int minPlayers, int maxPlayers, int year, int playTime, String thumbnail) {
        this.id = id;
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.year= year;
        this.playTime = playTime;
        this.thumbnail = thumbnail;
    }

    /**
     * Getter for the Game id
     * @return Integer id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the Game name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the Game minPlayers
     * @return Integer minPlayers
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Getter for the Game maxPlayers
     * @return Integer maxPlayers
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Getter for the Game year
     * @return Integer year
     */
    public int getYear() {
        return year;
    }

    /**
     * Getter for the Game playTime
     * @return Integer playTime
     */
    public int getPlayTime() {
        return playTime;
    }

    /**
     * Getter for the Game thumbnail
     * @return String thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }
}
