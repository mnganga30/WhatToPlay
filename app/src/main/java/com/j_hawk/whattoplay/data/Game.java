package com.j_hawk.whattoplay.data;

/**
 * Created by kevin on 10/24/2017.
 */

import java.util.ArrayList;

/**
 * Game.java
 * @author Kevin
 * @version 2.0
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
    private int minPlayerAge;
    private int suggestedMinPlayerAge;
    private ArrayList<String> categories;
    private ArrayList<String> mechanics;
    private int recommendedPlayers;
    private String description;

    public Game(int id, String name, int minPlayers, int maxPlayers, int year, int playTime, String thumbnail,
                int minPlayerAge, int suggestedMinPlayerAge, ArrayList<String> categories, ArrayList<String> mechanics,
                int recommendedPlayers, String description) {
        this.id = id;
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.year= year;
        this.playTime = playTime;
        this.thumbnail = thumbnail;
        this.minPlayerAge = minPlayerAge;
        this.suggestedMinPlayerAge = suggestedMinPlayerAge;
        this.categories = categories;
        this.mechanics = mechanics;
        this.recommendedPlayers = recommendedPlayers;
        this.description = description;
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

    public int getMinPlayerAge() {
        return minPlayerAge;
    }

    public int getSuggestedMinPlayerAge() {
        return suggestedMinPlayerAge;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public ArrayList<String> getMechanics() {
        return mechanics;
    }

    public int getRecommendedPlayers() {
        return recommendedPlayers;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minPlayers=" + minPlayers +
                ", maxPlayers=" + maxPlayers +
                ", year=" + year +
                ", playTime=" + playTime +
                ", thumbnail='" + thumbnail + '\'' +
                ", minPlayerAge=" + minPlayerAge +
                ", suggestedMinPlayerAge=" + suggestedMinPlayerAge +
                ", categories=" + categories +
                ", mechanics=" + mechanics +
                ", recommendedPlayers=" + recommendedPlayers +
                ", description='" + description + '\'' +
                '}';
    }
}
