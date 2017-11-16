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
 * Created by Jian on 11/14/17.
 */

/**
 * FindGamesByQuery.java
 * @author Kevin, Simon, Jian, Martin
 * @version 1.0
 * This class is used to call BoardGameGeek.com api. Takes query String in execute call
 * @return ArrayList<OnlineGame>> returns the hot items lits found through api call or null if no games were found
 */
    public class FindHotItems extends AsyncTask<String, Void, ArrayList<OnlineGame>>
{   /**
         * AsyncTask doInBackground method
         * @param params List of Strings. Only first is used to hold query String passed in execute method.
         * @return ArrayList<OnlineGame> returns the OnlineGames found through api call or null if no games were found
         */
        @Override
        protected ArrayList<OnlineGame> doInBackground(String... params) {
            ArrayList<OnlineGame> hotItemsResults;
            String urlString= "https://www.boardgamegeek.com/xmlapi2/hot?boardgame";
          //  Log.i("test", urlString);
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() != 200) {
                    throw new Exception("Could not connect to webservice");
                }
                try {
                    InputStream in = urlConnection.getInputStream();
                    ParserHotItems parser = new ParserHotItems();
                    List<OnlineGame> gameList = parser.parse(in);
                    hotItemsResults = new ArrayList<>(gameList);
                    return hotItemsResults;
                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }


}
