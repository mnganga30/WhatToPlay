package com.j_hawk.whattoplay.services;

import android.util.Log;
import android.util.Xml;

import com.j_hawk.whattoplay.data.Game;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 10/29/2017.
 */

public class ParserGame {
    // We don't use namespaces
    private  final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }


    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "items");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }


    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private Game readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String gameName = "";
        int minPlayers = 0;
        int maxPlayers = 0;
        int playTime = 0;
        int id = Integer.parseInt(parser.getAttributeValue(null, "id"));;
        int yearPublished = 0000;
        String thumbnail = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name") && parser.getAttributeValue(null, "type").equals("primary")) {
                Log.i("name", name);
                gameName = readGameName(parser);
            } else if (name.equals("yearpublished")) {
                yearPublished = readPublicationYear(parser);
            } else if (name.equals("minplayers")) {
                minPlayers = Integer.parseInt(parser.getAttributeValue(null, "value"));
                parser.nextTag();
            } else if (name.equals("maxplayers")) {
                maxPlayers = Integer.parseInt(parser.getAttributeValue(null, "value"));
                parser.nextTag();
            } else if (name.equals("playingtime")) {
                playTime = Integer.parseInt(parser.getAttributeValue(null, "value"));
                parser.nextTag();
            } else if (name.equals("thumbnail")) {
                parser.next();
                thumbnail = parser.getText();
                parser.nextTag();
            } else {
                skip(parser);
            }
        }
        return new Game(id, gameName, minPlayers, maxPlayers, yearPublished, playTime, thumbnail);
    }


    // Processes link tags in the Game Name.
    private String readGameName(XmlPullParser parser) throws IOException, XmlPullParserException {
        String game = "";
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String tag = parser.getName();
        if (tag.equals("name")) {
            Log.i("test", parser.getAttributeValue(null, "value"));
            game = parser.getAttributeValue(null, "value");
            parser.nextTag();
        }
        return game;
    }

    // Processes link tags in the Game Name.
    private int readPublicationYear(XmlPullParser parser) throws IOException, XmlPullParserException {
        int year = 0000;
        parser.require(XmlPullParser.START_TAG, ns, "yearpublished");
        String tag = parser.getName();
        if (tag.equals("yearpublished")) {
            year = Integer.parseInt(parser.getAttributeValue(null, "value"));;
            parser.nextTag();
        }
        return year;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
