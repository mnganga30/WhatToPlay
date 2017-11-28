package com.j_hawk.whattoplay.service;

import com.j_hawk.whattoplay.BuildConfig;
import com.j_hawk.whattoplay.data.Game;
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

import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TestParserGame {

    private ParserGame parser = new ParserGame();
    private List<Game> results;
    private InputStream inputStream;
    private Game expectedGame;

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
    public void test_game_is_read_correctly() {
        try {
            results = parser.parse(inputStream);
            assertEquals("Exactly 1 game was not returned from parser", 1, results.size());
            Game resultGame = results.get(0);
            assertEquals("Returned game did not match expected game", expectedGame, resultGame);
        } catch (XmlPullParserException e) {
            assertTrue("Parser threw XmlPullParserException", false);
        } catch (IOException e) {
            assertTrue("Parser threw IOException", false);
        }
    }

    @Before
    public void setup() {
        String input = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<items termsofuse=\"http://boardgamegeek.com/xmlapi/termsofuse\">\n" +
                "    <item type=\"boardgame\" id=\"68448\">\n" +
                "        <thumbnail>https://cf.geekdo-images.com/images/pic860217_t.jpg</thumbnail>\n" +
                "        <image>https://cf.geekdo-images.com/images/pic860217.jpg</image>\n" +
                "        <name type=\"primary\" sortindex=\"1\" value=\"7 Wonders\" />\n" +
                "        <name type=\"alternate\" sortindex=\"1\" value=\"7 csoda\" />\n" +
                "        <name type=\"alternate\" sortindex=\"1\" value=\"7 Cudów Świata\" />\n" +
                "        <name type=\"alternate\" sortindex=\"1\" value=\"7 divů světa\" />\n" +
                "        <name type=\"alternate\" sortindex=\"1\" value=\"7 чудес\" />\n" +
                "        <name type=\"alternate\" sortindex=\"1\" value=\"Τα 7 θαύματα του κόσμου\" />\n" +
                "        <name type=\"alternate\" sortindex=\"1\" value=\"七大奇蹟\" />\n" +
                "        <name type=\"alternate\" sortindex=\"1\" value=\"世界の七不思議\" />\n" +
                "        <description>You are the leader of one of the 7 great cities of the Ancient World. Gather resources, develop commercial routes, " +
                                        "and affirm your military supremacy. Build your city and erect an architectural wonder which will transcend future "+
                                        "times.&amp;#10;&amp;#10;7 Wonders lasts three ages. In each age, players receive seven cards from a particular deck,"+
                                        " choose one of those cards, then pass the remainder to an adjacent player. Players reveal their cards simultaneously,"+
                                        " paying resources if needed or collecting resources or interacting with other players in various ways. (Players have "+
                                        "individual boards with special powers on which to organize their cards, and the boards are double-sided). Each player "+
                                        "then chooses another card from the deck they were passed, and the process repeats until players have six cards in play "+
                                        "from that age. After three ages, the game ends.&amp;#10;&amp;#10;In essence, 7 Wonders is a card development game. Some "+
                                        "cards have immediate effects, while others provide bonuses or upgrades later in the game. Some cards provide discounts"+
                                        " on future purchases. Some provide military strength to overpower your neighbors and others give nothing but victory "+
                                        "points. Each card is played immediately after being drafted, so you'll know which cards your neighbor is receiving and"+
                                        " how his choices might affect what you've already built up. Cards are passed left-right-left over the three ages, "+
                                        "so you need to keep an eye on the neighbors in both directions.&amp;#10;&amp;#10;Though the box of earlier editions "+
                                        "is listed as being for 3&amp;ndash;7 players, there is an official 2-player variant included in the instructions."+
                                        "&amp;#10;&amp;#10;</description>\n" +
                "        <yearpublished value=\"2010\" />\n" +
                "        <minplayers value=\"2\" />\n" +
                "        <maxplayers value=\"7\" />\n" +
                "        <poll name=\"suggested_numplayers\" title=\"User Suggested Number of Players\" totalvotes=\"1502\">\n" +
                "            <results numplayers=\"1\">\n" +
                "                <result value=\"Best\" numvotes=\"3\" />\n" +
                "                <result value=\"Recommended\" numvotes=\"10\" />\n" +
                "                <result value=\"Not Recommended\" numvotes=\"825\" />\n" +
                "            </results>\n" +
                "            <results numplayers=\"2\">\n" +
                "                <result value=\"Best\" numvotes=\"107\" />\n" +
                "                <result value=\"Recommended\" numvotes=\"302\" />\n" +
                "                <result value=\"Not Recommended\" numvotes=\"655\" />\n" +
                "            </results>\n" +
                "            <results numplayers=\"3\">\n" +
                "                <result value=\"Best\" numvotes=\"329\" />\n" +
                "                <result value=\"Recommended\" numvotes=\"771\" />\n" +
                "                <result value=\"Not Recommended\" numvotes=\"122\" />\n" +
                "            </results>\n" +
                "            <results numplayers=\"4\">\n" +
                "                <result value=\"Best\" numvotes=\"777\" />\n" +
                "                <result value=\"Recommended\" numvotes=\"504\" />\n" +
                "                <result value=\"Not Recommended\" numvotes=\"28\" />\n" +
                "            </results>\n" +
                "            <results numplayers=\"5\">\n" +
                "                <result value=\"Best\" numvotes=\"622\" />\n" +
                "                <result value=\"Recommended\" numvotes=\"610\" />\n" +
                "                <result value=\"Not Recommended\" numvotes=\"41\" />\n" +
                "            </results>\n" +
                "            <results numplayers=\"6\">\n" +
                "                <result value=\"Best\" numvotes=\"283\" />\n" +
                "                <result value=\"Recommended\" numvotes=\"781\" />\n" +
                "                <result value=\"Not Recommended\" numvotes=\"117\" />\n" +
                "            </results>\n" +
                "            <results numplayers=\"7\">\n" +
                "                <result value=\"Best\" numvotes=\"259\" />\n" +
                "                <result value=\"Recommended\" numvotes=\"706\" />\n" +
                "                <result value=\"Not Recommended\" numvotes=\"201\" />\n" +
                "            </results>\n" +
                "            <results numplayers=\"7+\">\n" +
                "                <result value=\"Best\" numvotes=\"17\" />\n" +
                "                <result value=\"Recommended\" numvotes=\"61\" />\n" +
                "                <result value=\"Not Recommended\" numvotes=\"563\" />\n" +
                "            </results>\n" +
                "        </poll>\n" +
                "        <playingtime value=\"30\" />\n" +
                "        <minplaytime value=\"30\" />\n" +
                "        <maxplaytime value=\"30\" />\n" +
                "        <minage value=\"10\" />\n" +
                "        <poll name=\"suggested_playerage\" title=\"User Suggested Player Age\" totalvotes=\"335\">\n" +
                "            <results>\n" +
                "                <result value=\"2\" numvotes=\"0\" />\n" +
                "                <result value=\"3\" numvotes=\"0\" />\n" +
                "                <result value=\"4\" numvotes=\"1\" />\n" +
                "                <result value=\"5\" numvotes=\"0\" />\n" +
                "                <result value=\"6\" numvotes=\"12\" />\n" +
                "                <result value=\"8\" numvotes=\"86\" />\n" +
                "                <result value=\"10\" numvotes=\"128\" />\n" +
                "                <result value=\"12\" numvotes=\"86\" />\n" +
                "                <result value=\"14\" numvotes=\"18\" />\n" +
                "                <result value=\"16\" numvotes=\"3\" />\n" +
                "                <result value=\"18\" numvotes=\"1\" />\n" +
                "                <result value=\"21 and up\" numvotes=\"0\" />\n" +
                "            </results>\n" +
                "        </poll>\n" +
                "        <poll name=\"language_dependence\" title=\"Language Dependence\" totalvotes=\"372\">\n" +
                "            <results>\n" +
                "                <result level=\"1\" value=\"No necessary in-game text\" numvotes=\"285\" />\n" +
                "                <result level=\"2\" value=\"Some necessary text - easily memorized or small crib sheet\" numvotes=\"82\" />\n" +
                "                <result level=\"3\" value=\"Moderate in-game text - needs crib sheet or paste ups\" numvotes=\"4\" />\n" +
                "                <result level=\"4\" value=\"Extensive use of text - massive conversion needed to be playable\" numvotes=\"1\" />\n" +
                "                <result level=\"5\" value=\"Unplayable in another language\" numvotes=\"0\" />\n" +
                "            </results>\n" +
                "        </poll>\n" +
                "        <link type=\"boardgamecategory\" id=\"1050\" value=\"Ancient\" />\n" +
                "        <link type=\"boardgamecategory\" id=\"1002\" value=\"Card Game\" />\n" +
                "        <link type=\"boardgamecategory\" id=\"1029\" value=\"City Building\" />\n" +
                "        <link type=\"boardgamecategory\" id=\"1015\" value=\"Civilization\" />\n" +
                "        <link type=\"boardgamemechanic\" id=\"2041\" value=\"Card Drafting\" />\n" +
                "        <link type=\"boardgamemechanic\" id=\"2040\" value=\"Hand Management\" />\n" +
                "        <link type=\"boardgamemechanic\" id=\"2004\" value=\"Set Collection\" />\n" +
                "        <link type=\"boardgamemechanic\" id=\"2020\" value=\"Simultaneous Action Selection\" />\n" +
                "        <link type=\"boardgamemechanic\" id=\"2015\" value=\"Variable Player Powers\" />\n" +
                "        <link type=\"boardgamefamily\" id=\"17552\" value=\"7 Wonders\" />\n" +
                "        <link type=\"boardgamefamily\" id=\"27646\" value=\"Tableau Building\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"154638\" value=\"7 Wonders: Babel\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"110308\" value=\"7 Wonders: Catan\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"111661\" value=\"7 Wonders: Cities\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"92539\" value=\"7 Wonders: Leaders\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"83445\" value=\"7 Wonders: Manneken Pis\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"133993\" value=\"7 Wonders: Wonder Pack\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"164649\" value=\"Collection (fan expansion for 7 Wonders)\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"140098\" value=\"Empires (fan expansion for 7 Wonders)\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"138187\" value=\"Game Wonders (fan expansion for 7 Wonders)\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"134849\" value=\"Lost Wonders (fan expansion for 7 Wonders)\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"131947\" value=\"More Wonders... (fan expansion for 7 Wonders)\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"132146\" value=\"Myths (fan expansion for 7 Wonders)\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"164648\" value=\"Ruins (fan expansion for 7 Wonders)\" />\n" +
                "        <link type=\"boardgameexpansion\" id=\"164647\" value=\"Sailors (fan expansion for 7 Wonders)\" />\n" +
                "        <link type=\"boardgamedesigner\" id=\"9714\" value=\"Antoine Bauza\" />\n" +
                "        <link type=\"boardgameartist\" id=\"12016\" value=\"Miguel Coimbra\" />\n" +
                "        <link type=\"boardgamepublisher\" id=\"4384\" value=\"Repos Production\" />\n" +
                "        <link type=\"boardgamepublisher\" id=\"23043\" value=\"ADC Blackfire Entertainment\" />\n" +
                "        <link type=\"boardgamepublisher\" id=\"157\" value=\"Asmodee\" />\n" +
                "        <link type=\"boardgamepublisher\" id=\"15889\" value=\"Asterion Press\" />\n" +
                "        <link type=\"boardgamepublisher\" id=\"15605\" value=\"Galápagos Jogos\" />\n" +
                "        <link type=\"boardgamepublisher\" id=\"8820\" value=\"Gém Klub Kft.\" />\n" +
                "        <link type=\"boardgamepublisher\" id=\"1391\" value=\"Hobby Japan\" />\n" +
                "        <link type=\"boardgamepublisher\" id=\"6214\" value=\"Kaissa Chess &amp; Games\" />\n" +
                "        <link type=\"boardgamepublisher\" id=\"3218\" value=\"Lautapelit.fi\" />\n" +
                "        <link type=\"boardgamepublisher\" id=\"9325\" value=\"Lifestyle Boardgames Ltd\" />\n" +
                "        <link type=\"boardgamepublisher\" id=\"7466\" value=\"Rebel\" />\n" +
                "    </item>\n" +
                "</items>";
        inputStream = new ByteArrayInputStream(input.getBytes());
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Ancient");
        categories.add("Card Game");
        categories.add("City Building");
        categories.add("Civilization");
        ArrayList<String> mechanics = new ArrayList<>();
        mechanics.add("Card Drafting");
        mechanics.add("Hand Management");
        mechanics.add("Set Collection");
        mechanics.add("Simultaneous Action Selection");
        mechanics.add("Variable Player Powers");
        String desricption = "You are the leader of one of the 7 great cities of the Ancient World. Gather resources, develop commercial routes, " +
                "and affirm your military supremacy. Build your city and erect an architectural wonder which will transcend future "+
                "times.&amp;#10;&amp;#10;7 Wonders lasts three ages. In each age, players receive seven cards from a particular deck,"+
                " choose one of those cards, then pass the remainder to an adjacent player. Players reveal their cards simultaneously,"+
                " paying resources if needed or collecting resources or interacting with other players in various ways. (Players have "+
                "individual boards with special powers on which to organize their cards, and the boards are double-sided). Each player "+
                "then chooses another card from the deck they were passed, and the process repeats until players have six cards in play "+
                "from that age. After three ages, the game ends.&amp;#10;&amp;#10;In essence, 7 Wonders is a card development game. Some "+
                "cards have immediate effects, while others provide bonuses or upgrades later in the game. Some cards provide discounts"+
                " on future purchases. Some provide military strength to overpower your neighbors and others give nothing but victory "+
                "points. Each card is played immediately after being drafted, so you'll know which cards your neighbor is receiving and"+
                " how his choices might affect what you've already built up. Cards are passed left-right-left over the three ages, "+
                "so you need to keep an eye on the neighbors in both directions.&amp;#10;&amp;#10;Though the box of earlier editions "+
                "is listed as being for 3&amp;ndash;7 players, there is an official 2-player variant included in the instructions."+
                "&amp;#10;&amp;#10;";
        desricption = desricption.replaceAll("&amp;#10;", "\n");
        expectedGame = new Game(68448, "7 Wonders", 2, 7, 2010, 30, "https://cf.geekdo-images.com/images/pic860217_t.jpg",
                10, 10, categories, mechanics, 4, desricption);

    }
}
