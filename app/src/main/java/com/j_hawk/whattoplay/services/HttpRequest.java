package com.j_hawk.whattoplay.services;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/**
 * Created by martin on 10/27/2017.
 */

public class HttpRequest {
    public ArrayList<OnlineGame> searchForGames(String query)
    {
        String urlString= "http://www.boardgamegeek.com/xmlapi2/search?query=" + query;
        try {
            URL url = new URL ( urlString);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            Parser searchParser = null;
            
            searchParser.parse(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return null;
    }
}
