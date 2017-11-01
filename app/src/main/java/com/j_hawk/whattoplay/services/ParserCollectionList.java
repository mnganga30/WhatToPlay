package com.j_hawk.whattoplay.services;

import android.util.Log;
import android.util.Xml;

import com.j_hawk.whattoplay.data.OnlineGame;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 10/27/2017.
 */

public class ParserCollectionList {

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
        private OnlineGame readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "item");
            String gameName = "";
            int id = Integer.parseInt(parser.getAttributeValue(null, "objectid"));;
            int yearPublished = 0;

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("name")) {
                    gameName = readGameName(parser);
                } else if (name.equals("yearpublished")) {
                    yearPublished = readPublicationYear(parser);
                } else {
                    skip(parser);
                }
            }
            Log.i("test", gameName + " : " + id + " : " + yearPublished);
            return new OnlineGame(id, gameName, yearPublished);
        }


        // Processes link tags in the Game Name.
    private String readGameName(XmlPullParser parser) throws IOException, XmlPullParserException {
        String game = "";
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String tag = parser.getName();
        if (tag.equals("name")) {
            parser.next();
            game = parser.getText();
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
            parser.next();
            year = Integer.parseInt(parser.getText());;
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


