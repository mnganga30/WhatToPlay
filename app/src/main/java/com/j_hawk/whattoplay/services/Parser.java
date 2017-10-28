package com.j_hawk.whattoplay.services;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.name;

/**
 * Created by martin on 10/27/2017.
 */

public class Parser {


    OnlineGame entry;

    public class StackOverflowXmlParser {
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

            parser.require(XmlPullParser.START_TAG, ns, "feed");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals("entry")) {
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
            parser.require(XmlPullParser.START_TAG, ns, "items");
            String gameName = null;
            int id = 0;
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("item")) {
                    gameName = readGameName(parser);
                    id = readId(parser);
                } else {
                    skip(parser);
                }
            }
            return new OnlineGame(id, gameName);
        }

        // Processes title tags in the feed.


        // Processes link tags in the feed.
        private int readId(XmlPullParser parser) throws IOException, XmlPullParserException {
            int id = 0;
            parser.require(XmlPullParser.START_TAG, ns, "item");
            String tag = parser.getName();
            String gameType = parser.getAttributeValue(null, "type");
            if (tag.equals("Item")) {
                if (gameType.equals("boardgame")) {
                    id = Integer.parseInt(parser.getAttributeValue(null, "id"));
                    parser.nextTag();
                }

            }
            return id;
        }

        // Processes link tags in the feed.
        private String readGameName(XmlPullParser parser) throws IOException, XmlPullParserException {
            String game = "";
            parser.require(XmlPullParser.START_TAG, ns, "name");
            String tag = parser.getName();
            String gameType = parser.getAttributeValue(null, "type");
            if (tag.equals("name")) {
                if (gameType.equals("boardgame")) {
                    game = parser.getAttributeValue(null, "value");
                    parser.nextTag();
                }

            }
            return game;
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

}
