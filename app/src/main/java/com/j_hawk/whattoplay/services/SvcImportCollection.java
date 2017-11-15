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
 * Created by kevin on 10/30/2017.
 */

/**
 * SvcImportCollection.java
 * @author Kevin, Simon, Jian, Martin
 * @version 1.0
 * This class is used to call BoardGameGeek.com api. Takes String that represents user name in execute call
 * @return ArrayList<OnlineGame>> returns the OnlineGames found through api call or null if no games were found
 */
public class SvcImportCollection extends AsyncTask<String, Void, ArrayList<OnlineGame>> {

    /**
     * AsyncTask doInBackground method
     * @param params List of Strings. Only first is used to hold user name String passed in exexute method.
     * @return ArrayList<OnlineGame> returns the OnlineGames found through api call or null if no games were found
     */
    @Override
    protected ArrayList<OnlineGame> doInBackground(String... params) {
        String urlString= "https://www.boardgamegeek.com/xmlapi2/collection?own=1&excludesubtype=boardgameexpansion&subtype=boardgame&username=" + params[0];
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            while (urlConnection.getResponseCode() == 202) {
                urlConnection.disconnect();
                Thread.sleep(1000);
                urlConnection = (HttpURLConnection) url.openConnection();
            }

            if (urlConnection.getResponseCode() != 200) {
                throw new Exception("Could not connect to webservice");
            }
            try {
                InputStream in = urlConnection.getInputStream();
                ParserCollectionList parser = new ParserCollectionList();
                List<OnlineGame> onlineGameList = parser.parse(in);
                ArrayList<OnlineGame> games = new ArrayList<>(onlineGameList);
                return  games;

            } finally {
                urlConnection.disconnect();
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
}
