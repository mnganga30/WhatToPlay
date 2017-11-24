package com.j_hawk.whattoplay.service;

import com.j_hawk.whattoplay.BuildConfig;
import com.j_hawk.whattoplay.data.OnlineGame;
import com.j_hawk.whattoplay.services.ParserGameList;

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
public class TestParserGameList {

    private ParserGameList parser = new ParserGameList();
    private List<OnlineGame> results;
    private InputStream inputStream;
    ArrayList<OnlineGame> expectedList;

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
            assertEquals(results.size(), expectedList.size());
            boolean allEqual = true;
            int numberOfBadGames = 0;
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).equals(expectedList.get(i))) {
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
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<items total=\"9\" termsofuse=\"http://boardgamegeek.com/xmlapi/termsofuse\">\n" +
                "    <item type=\"boardgame\" id=\"173346\">\n" +
                "        <name type=\"primary\" value=\"7 Wonders Duel\"/>\n" +
                "        <yearpublished value=\"2015\" />\n" +
                "    </item>\n" +
                "    <item type=\"boardgame\" id=\"202976\">\n" +
                "        <name type=\"primary\" value=\"7 Wonders Duel: Pantheon\"/>\n" +
                "        <yearpublished value=\"2016\" />\n" +
                "    </item>\n" +
                "    <item type=\"boardgame\" id=\"196339\">\n" +
                "        <name type=\"primary\" value=\"7 Wonders Duel: Statue of Liberty\"/>\n" +
                "        <yearpublished value=\"2016\" />\n" +
                "    </item>\n" +
                "    <item type=\"boardgame\" id=\"228690\">\n" +
                "        <name type=\"primary\" value=\"7 Wonders Duel: Stonehenge\"/>\n" +
                "        <yearpublished value=\"2017\" />\n" +
                "    </item>\n" +
                "    <item type=\"boardgame\" id=\"186069\">\n" +
                "        <name type=\"primary\" value=\"7 Wonders Duel: The Messe Essen\"/>\n" +
                "        <yearpublished value=\"2015\" />\n" +
                "    </item>\n" +
                "    <item type=\"boardgameexpansion\" id=\"202976\">\n" +
                "        <name type=\"primary\" value=\"7 Wonders Duel: Pantheon\"/>\n" +
                "        <yearpublished value=\"2016\" />\n" +
                "    </item>\n" +
                "    <item type=\"boardgameexpansion\" id=\"196339\">\n" +
                "        <name type=\"primary\" value=\"7 Wonders Duel: Statue of Liberty\"/>\n" +
                "        <yearpublished value=\"2016\" />\n" +
                "    </item>\n" +
                "    <item type=\"boardgameexpansion\" id=\"228690\">\n" +
                "        <name type=\"primary\" value=\"7 Wonders Duel: Stonehenge\"/>\n" +
                "        <yearpublished value=\"2017\" />\n" +
                "    </item>\n" +
                "    <item type=\"boardgameexpansion\" id=\"186069\">\n" +
                "        <name type=\"primary\" value=\"7 Wonders Duel: The Messe Essen\"/>\n" +
                "        <yearpublished value=\"2015\" />\n" +
                "    </item>\n" +
                "</items>";
        inputStream = new ByteArrayInputStream(xml.getBytes());
        expectedList = new ArrayList<>();
        expectedList.add(new OnlineGame(173346, "7 Wonders Duel", 2015));
        expectedList.add(new OnlineGame(202976, "7 Wonders Duel: Pantheon", 2016));
        expectedList.add(new OnlineGame(196339, "7 Wonders Duel: Statue of Liberty", 2016));
        expectedList.add(new OnlineGame(228690, "7 Wonders Duel: Stonehenge", 2017));
        expectedList.add(new OnlineGame(186069, "7 Wonders Duel: The Messe Essen", 2015));
    }
}
