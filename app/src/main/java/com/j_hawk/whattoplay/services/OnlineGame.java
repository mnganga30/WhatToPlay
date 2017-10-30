package com.j_hawk.whattoplay.services;

/**
 * Created by martin on 10/27/2017.
 */

public class OnlineGame {

    private String name;
    private int id;
    private int year;

    OnlineGame(int id, String name, int year)
    {
        this.id=id;
        this.name =name;
        this.year = year;
    }


    public int getId()
    {

        return id;
    }

    public String getName()
    {
        return name;

    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return name + "\n" + year;
    }
}
