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

public class FindGamesByQuery extends AsyncTask<String, Void, ArrayList<OnlineGame>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

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

    @Override
    protected void onPostExecute(ArrayList<OnlineGame> onlineGames) {
        super.onPostExecute(onlineGames);
    }
}
