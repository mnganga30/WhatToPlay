package com.j_hawk.whattoplay.service;

import com.j_hawk.whattoplay.BuildConfig;
import com.j_hawk.whattoplay.data.OnlineGame;
import com.j_hawk.whattoplay.services.ParserCollectionList;
import com.j_hawk.whattoplay.services.ParserGame;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TestParserCollectionList {

    private ParserCollectionList parser = new ParserCollectionList();
    private List<OnlineGame> results;
    private InputStream inputStream;
    private ArrayList<OnlineGame> expectedGames;

    @Test
    public void test_parse_does_not_throw_exception() {
        try {
            results = parser.parse(inputStream);
        } catch (XmlPullParserException e) {
            assertTrue("Parser threw XmlPullParserException", false);
        } catch (IOException e) {
            assertTrue("Parser threw IOException", false);
        }
    }

    @Test
    public void test_expected_games_returned() {
        try {
            results = parser.parse(inputStream);
            assertEquals(results.size(), expectedGames.size());
            boolean allEqual = true;
            int numberOfBadGames = 0;
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).equals(expectedGames.get(i))) {
                    allEqual = false;
                    numberOfBadGames++;
                }
            }
            assertTrue(numberOfBadGames + " games were not the same", allEqual);
        } catch (XmlPullParserException e) {
            assertTrue("Parser threw XmlPullParserException", false);
        } catch (IOException e) {
            assertTrue("Parser threw IOException", false);
        }
    }

    @Before
    public void setup() {

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
                "<items totalitems=\"5\" termsofuse=\"http://boardgamegeek.com/xmlapi/termsofuse\" pubdate=\"Fri, 24 Nov 2017 18:18:16 +0000\">\n" +
                "    <item objecttype=\"thing\" objectid=\"68448\" subtype=\"boardgame\" collid=\"27151095\">\n" +
                "        <name sortindex=\"1\">7 Wonders</name>\n" +
                "        <yearpublished>2010</yearpublished>\n" +
                "        <image>https://cf.geekdo-images.com/images/pic860217.jpg</image>\n" +
                "        <thumbnail>https://cf.geekdo-images.com/images/pic860217_t.jpg</thumbnail>\n" +
                "        <status own=\"1\" prevowned=\"0\" fortrade=\"0\" want=\"0\" wanttoplay=\"0\" wanttobuy=\"0\" wishlist=\"0\"  preordered=\"0\" lastmodified=\"2015-06-14 07:24:27\" />\n" +
                "        <numplays>0</numplays>\n" +
                "    </item>\n" +
                "    <item objecttype=\"thing\" objectid=\"39339\" subtype=\"boardgame\" collid=\"41585648\">\n" +
                "        <name sortindex=\"1\">Android</name>\n" +
                "        <yearpublished>2008</yearpublished>\n" +
                "        <image>https://cf.geekdo-images.com/images/pic400196.jpg</image>\n" +
                "        <thumbnail>https://cf.geekdo-images.com/images/pic400196_t.jpg</thumbnail>\n" +
                "        <status own=\"1\" prevowned=\"0\" fortrade=\"0\" want=\"0\" wanttoplay=\"0\" wanttobuy=\"0\" wishlist=\"0\"  preordered=\"0\" lastmodified=\"2017-04-01 12:53:36\" />\n" +
                "        <numplays>0</numplays>\n" +
                "    </item>\n" +
                "    <item objecttype=\"thing\" objectid=\"124742\" subtype=\"boardgame\" collid=\"32364424\">\n" +
                "        <name sortindex=\"1\">Android: Netrunner</name>\n" +
                "        <yearpublished>2012</yearpublished>\n" +
                "        <image>https://cf.geekdo-images.com/images/pic1324609.jpg</image>\n" +
                "        <thumbnail>https://cf.geekdo-images.com/images/pic1324609_t.jpg</thumbnail>\n" +
                "        <status own=\"1\" prevowned=\"0\" fortrade=\"0\" want=\"0\" wanttoplay=\"0\" wanttobuy=\"0\" wishlist=\"0\"  preordered=\"0\" lastmodified=\"2016-01-08 07:11:18\" />\n" +
                "        <numplays>0</numplays>\n" +
                "    </item>\n" +
                "    <item objecttype=\"thing\" objectid=\"178210\" subtype=\"boardgame\" collid=\"31048422\">\n" +
                "        <name sortindex=\"1\">Batman Fluxx</name>\n" +
                "        <yearpublished>2015</yearpublished>\n" +
                "        <image>https://cf.geekdo-images.com/images/pic2529971.png</image>\n" +
                "        <thumbnail>https://cf.geekdo-images.com/images/pic2529971_t.png</thumbnail>\n" +
                "        <status own=\"1\" prevowned=\"0\" fortrade=\"0\" want=\"0\" wanttoplay=\"0\" wanttobuy=\"0\" wishlist=\"0\"  preordered=\"0\" lastmodified=\"2015-10-31 23:12:36\" />\n" +
                "        <numplays>0</numplays>\n" +
                "    </item>\n" +
                "    <item objecttype=\"thing\" objectid=\"171131\" subtype=\"boardgame\" collid=\"39388677\">\n" +
                "        <name sortindex=\"1\">Captain Sonar</name>\n" +
                "        <yearpublished>2016</yearpublished>\n" +
                "        <image>https://cf.geekdo-images.com/images/pic3013621.png</image>\n" +
                "        <thumbnail>https://cf.geekdo-images.com/images/pic3013621_t.png</thumbnail>\n" +
                "        <status own=\"1\" prevowned=\"0\" fortrade=\"0\" want=\"0\" wanttoplay=\"0\" wanttobuy=\"0\" wishlist=\"0\"  preordered=\"0\" lastmodified=\"2017-01-01 13:24:31\" />\n" +
                "        <numplays>0</numplays>\n" +
                "    </item>\n" +
                "</items>";
        inputStream = new ByteArrayInputStream(xml.getBytes());
        expectedGames = new ArrayList<>();
        expectedGames.add(new OnlineGame(68448, "7 Wonders", 2010));
        expectedGames.add(new OnlineGame(39339, "Android", 2008));
        expectedGames.add(new OnlineGame(124742, "Android: Netrunner", 2012));
        expectedGames.add(new OnlineGame(178210, "Batman Fluxx", 2015));
        expectedGames.add(new OnlineGame(171131, "Captain Sonar", 2016));
    }
}
