package com.j_hawk.whattoplay.services;

import android.os.AsyncTask;
import android.util.Log;

import com.j_hawk.whattoplay.data.OnlineGame;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * FindGamesByQuery.java
 * @author Kevin, Simon, Jian, Martin
 * @version 1.0
 * This class is used to call BoardGameGeek.com api. Takes query String in execute call
 * @return ArrayList<OnlineGame>> returns the OnlineGames found through api call or null if no games were found
 */
public class FindGamesByQuery extends AsyncTask<String, Void, ArrayList<OnlineGame>> {

    /**
     * AsyncTask doInBackground method
     * @param params List of Strings. Only first is used to hold query String passed in exexute method.
     * @return ArrayList<OnlineGame> returns the OnlineGames found through api call or null if no games were found
     */
    @Override
    protected ArrayList<OnlineGame> doInBackground(String... params) {
        ArrayList<OnlineGame> gameResults;
        String urlString= "https://www.boardgamegeek.com/xmlapi2/search?type=boardgame&query=" + params[0];
        Log.i("test", urlString);
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("test", "" + urlConnection.getResponseCode());
            try {
                InputStream in = urlConnection.getInputStream();
                ParserGameList parser = new ParserGameList();
                List<OnlineGame> gameList = parser.parse(in);
                gameResults = new ArrayList<>(gameList);
                return gameResults;
            } finally {
                urlConnection.disconnect();
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
}
