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
public class ParserHotItemsTest {

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
                "<items termsofuse=\"http://boardgamegeek.com/xmlapi/termsofuse\">\n" +
                "    <item id=\"174430\" rank=\"1\">\n" +
                "        <thumbnail value=\"https://cf.geekdo-images.com/images/pic2437871_t.jpg\"/>\n" +
                "        <name value=\"Gloomhaven\"/>\n" +
                "        <yearpublished value=\"2017\" />\n" +
                "    </item>\n" +
                "    <item id=\"233247\" rank=\"2\">\n" +
                "        <thumbnail value=\"https://cf.geekdo-images.com/images/pic3764168_t.jpg\"/>\n" +
                "        <name value=\"Sid Meier&#039;s Civilization: A New Dawn\"/>\n" +
                "        <yearpublished value=\"2017\" />\n" +
                "    </item>\n" +
                "    <item id=\"234669\" rank=\"3\">\n" +
                "        <thumbnail value=\"https://cf.geekdo-images.com/images/pic3754388_t.jpg\"/>\n" +
                "        <name value=\"Legacy of Dragonholt\"/>\n" +
                "        <yearpublished value=\"2017\" />\n" +
                "    </item>\n" +
                "    <item id=\"230802\" rank=\"4\">\n" +
                "        <thumbnail value=\"https://cf.geekdo-images.com/images/pic3718275_t.jpg\"/>\n" +
                "        <name value=\"Azul\"/>\n" +
                "        <yearpublished value=\"2017\" />\n" +
                "    </item>\n" +
                "    <item id=\"232918\" rank=\"5\">\n" +
                "        <thumbnail value=\"https://cf.geekdo-images.com/images/pic3728149_t.jpg\"/>\n" +
                "        <name value=\"Fallout\"/>\n" +
                "        <yearpublished value=\"2017\" />\n" +
                "    </item>";
        inputStream = new ByteArrayInputStream(xml.getBytes());
        expectedList = new ArrayList<>();
        expectedList.add(new OnlineGame(174430, "Gloomhaven", 2017));
        expectedList.add(new OnlineGame(233247, "Sid Meier&#039;s Civilization: A New Dawn", 2017));
        expectedList.add(new OnlineGame(234669, "Legacy of Dragonholt", 2017));
        expectedList.add(new OnlineGame(230802, "Azul", 2017));
        expectedList.add(new OnlineGame(232918, "Fallout", 2017));
    }
}
