package com.j_hawk.whattoplay.data;

/**
 * Created by kevin on 10/24/2017.
 */
/**
 * Game.java
 * @author Kevin
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

    public Game(int id, String name, int minPlayers, int maxPlayers, int year, int playTime, String thumbnail) {
        this.id = id;
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.year= year;
        this.playTime = playTime;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getYear() {
        return year;
    }

    public int getPlayTime() {
        return playTime;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
