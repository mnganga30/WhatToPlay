package com.j_hawk.whattoplay.services;

import android.os.AsyncTask;
import android.util.Log;

import com.j_hawk.whattoplay.data.Game;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by kevin on 10/30/2017.
 */

/**
 * FindGameByID.java
 * @author Kevin, Simon, Jian, Martin
 * @version 1.0
 * This class is used to call BoardGameGeek.com api. Takes Integer in execute call and returns a Game
 * @return Game returns the game found through api call or null if no game was found
 */
public class FindGameByID extends AsyncTask<Integer, Void, Game> {

    /**
     * AsyncTask doInBackground method
     * @param params List of Integers. Only first is used to hold id passed in exexute method.
     * @return Game returns the bame found through api call or null if no game was found
     */
    @Override
    protected Game doInBackground(Integer... params) {
        String urlString= "https://www.boardgamegeek.com/xmlapi2/thing?id=" + params[0];
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() != 200) {
                throw new Exception("Could not connect to webservice");
            }
            try {
                InputStream in = urlConnection.getInputStream();
                ParserGame parser = new ParserGame();
                List<Game> game = parser.parse(in);
                return game.get(0);

            } finally {
                urlConnection.disconnect();
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
}
