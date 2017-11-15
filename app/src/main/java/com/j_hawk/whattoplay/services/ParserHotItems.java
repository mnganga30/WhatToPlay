package com.j_hawk.whattoplay.services;

import android.util.Xml;

import com.j_hawk.whattoplay.data.OnlineGame;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jian on 11/14/17.
 */

/**
 * ParserHotItems.java
 * @author Kevin, Simon, Jian, Martin
 * @version 1.0
 * This class is used to hold the XmlPullParser used to parse the XML returned from FindHotItems.
 * @return List<OnlineGame> returns the List of OnlineGames parsed from the InputStream
 */
public class ParserHotItems {

    // We don't use namespaces
    private  final String ns = null;
    private String gameName;
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
    /**
     * Method called to parse the InputStream for OnlineGames
     * @throws XmlPullParserException
     * @throws IOException
     * @param in InputStream XML in input stream that contains a OnlineGames from BoardGameGeek.com
     * @return List returns the List of OnlineGames parsed from the input stream.
     */
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
    private OnlineGame readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        //String gameName = null;
         id = Integer.parseInt(parser.getAttributeValue(null, "id"));
    //    int rank = Integer.parseInt(parser.getAttributeValue(null, "rank"));;


        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                parser.next();
                gameName = parser.getAttributeValue(null,"value");
                parser.nextTag();
            }
            else if (name.equals("yearpublished")) {
                parser.next();
                yearPublished = Integer.parseInt(parser.getAttributeValue(null,"value"));
                parser.nextTag();
            }
//            else if(name.equals("thumbnail"))
//            {
//                parser.next();
//                thumbnail = parser.getText();
//                parser.nextTag();
//            }
            else {
                skip(parser);
            }
        }
        return new OnlineGame(id, gameName, yearPublished);
    }


//    // Processes link tags in the Game Name.
//    private String readGameName(XmlPullParser parser) throws IOException, XmlPullParserException {
//        String game = "";
//        parser.require(XmlPullParser.START_TAG, ns, "name");
//        String tag = parser.getName();
//        if (tag.equals("name")) {
//            game = parser.getAttributeValue(null, "value");
//            parser.nextTag();
//        }
//        return game;
////    }
//
//    // Processes link tags in the Game Name.
//    private int readPublicationYear(XmlPullParser parser) throws IOException, XmlPullParserException {
//        //int year = 0000;
//        parser.require(XmlPullParser.START_TAG, ns, "yearpublished");
//        String tag = parser.getName();
//        if (tag.equals("yearpublished")) {
//            yearPublished = Integer.parseInt(parser.getAttributeValue(null, "value"));;
//            parser.nextTag();
//        }
//        return yearPublished;
//    }



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
