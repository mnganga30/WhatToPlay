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

    private String gameName;
    private int minPlayers;
    private int maxPlayers;
    private int playTime;
    private int id;
    private int yearPublished;
    private String thumbnail;
    private int minPlayerAge;
    private String suggestedMinPlayerAge;
    private ArrayList<String> categories;
    private ArrayList<String> mechanics;
    private int recommendedPlayers;
    private String description;
    private int numVotes;

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
        id = Integer.parseInt(parser.getAttributeValue(null, "id"));;
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> mechanics = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name") && parser.getAttributeValue(null, "type").equals("primary")) {
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
            } else if (name.equals("minage")) {
                minPlayerAge = Integer.parseInt(parser.getAttributeValue(null, "value"));
                parser.nextTag();
            } else if (name.equals("poll") && parser.getAttributeValue(null, "name").equals("suggested_numplayers")) {
                numVotes = 0;
                readPollNumPlayers(parser);
                parser.next();
            } else if (name.equals("poll") && parser.getAttributeValue(null, "name").equals("suggested_playerage")) {
                numVotes = 0;
                readPollPlayerAge(parser);
            } else if (name.equals("description")) {
                parser.next();
                description = parser.getText();
                description = description.replaceAll("&#10;", "");
                parser.nextTag();
            } else if (name.equals("link")) {
                if (parser.getAttributeValue(null, "type").equals("boardgamecategory")) {
                    categories.add(parser.getAttributeValue(null, "value"));
                } else if (parser.getAttributeValue(null, "type").equals("boardgamemechanic")) {
                    mechanics.add(parser.getAttributeValue(null, "value"));
                }
                parser.nextTag();
            } else {
                skip(parser);
            }
        }
        return new Game(id, gameName, minPlayers, maxPlayers, yearPublished, playTime, thumbnail,
                        minPlayerAge, suggestedMinPlayerAge, categories, mechanics,
                        recommendedPlayers, description);
    }


    // Processes link tags in the Game Name.
    private String readGameName(XmlPullParser parser) throws IOException, XmlPullParserException {
        String game = "";
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String tag = parser.getName();
        if (tag.equals("name")) {
            game = parser.getAttributeValue(null, "value");
            parser.nextTag();
        }
        return game;
    }

    // Processes link tags in the Game Name.
    private int readPublicationYear(XmlPullParser parser) throws IOException, XmlPullParserException {
        int year = 0;
        parser.require(XmlPullParser.START_TAG, ns, "yearpublished");
        String tag = parser.getName();
        if (tag.equals("yearpublished")) {
            year = Integer.parseInt(parser.getAttributeValue(null, "value"));;
            parser.nextTag();
        }
        return year;
    }

    // Processes link tags in the Game Name.
    private void readPollNumPlayers(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "poll");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("results")) {
                readResultsNumPlayers(parser);
            } else {
                return;
            }
        }
    }

    // Processes link tags in the Game Name.
    private void readResultsNumPlayers(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "results");
        boolean skip = false;
        int suggestedPlayers = 0;
        try {
            suggestedPlayers = Integer.parseInt(parser.getAttributeValue(null, "numplayers"));
        } catch (Exception e) {
            skip = true;
        }
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("result")) {
                if (parser.getAttributeValue(null, "value").equals("Best") && !skip) {
                    int votes = Integer.parseInt(parser.getAttributeValue(null, "numvotes"));
                    if (votes > numVotes) {
                        numVotes = votes;
                        recommendedPlayers = suggestedPlayers;
                    }
                }
                parser.nextTag();
            }
        }
    }

    // Processes link tags in the Game Name.
    private void readPollPlayerAge(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "poll");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("results")) {
                readResultsPlayerAge(parser);
            }
        }
    }

    // Processes link tags in the Game Name.
    private void readResultsPlayerAge(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "results");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("result")) {
                String tempPlayerAge = parser.getAttributeValue(null, "value");
                int votes = Integer.parseInt(parser.getAttributeValue(null, "numvotes"));
                if (votes > numVotes) {
                    numVotes = votes;
                    suggestedMinPlayerAge = tempPlayerAge;
                }
                parser.nextTag();
            }
        }
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
