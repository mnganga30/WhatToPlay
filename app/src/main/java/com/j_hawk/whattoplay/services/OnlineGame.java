package com.j_hawk.whattoplay.services;

/**
 * Created by martin on 10/27/2017.
 */

public class OnlineGame {

    private String name;
   private int id;

    OnlineGame(int id, String name)
    {
        this.id=id;
        this.name =name;
    }


    int getId()
    {
        return id;
    }

    String getName()
    {
        return name;

    }


}
