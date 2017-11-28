package com.j_hawk.whattoplay.services;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.j_hawk.whattoplay.ImportCollection;
import com.j_hawk.whattoplay.R;
import com.j_hawk.whattoplay.data.DBHelper;
import com.j_hawk.whattoplay.data.Game;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestImportCollection {

    @Rule
    public ActivityTestRule<ImportCollection> findGameByQuerydRule = new ActivityTestRule<>(ImportCollection.class, false, false);

    private ArrayList<Game> expectedGames;
    private ArrayList<Game> testGames;
    private static final String USER_ID = "Team JHawk";
    private static final int NUM_OF_GAMES = 5;
    DBHelper dbHelper;

    @Before
    public void setup() {
        findGameByQuerydRule.launchActivity(new Intent());
        dbHelper = new DBHelper(findGameByQuerydRule.getActivity());
        dbHelper.rebuildDatabase();
        setupExpectedGames();

        onView(withId(R.id.bggUserNameEdTxt)).perform(typeText(USER_ID));
        onView(withId(R.id.importButton)).perform(click());

        testGames = dbHelper.getAllGames();
    }

    @After
    public void teardown() {
        dbHelper.close();
    }

    @Test
    public void testReturneGamesSize() {
        assertEquals(NUM_OF_GAMES, testGames.size());
    }

    @Test
    public void testGamesImportedCorrectly() {
        assertEquals(testGames.size(), expectedGames.size());
        boolean allEqual = true;
        int numberOfBadGames = 0;
        for (int i = 0; i < testGames.size(); i++) {
            if (!testGames.get(i).equals(expectedGames.get(i))) {
                allEqual = false;
                numberOfBadGames++;
                Log.d("TestImportCollection", "Test Game:\n" + testGames.get(i).toString() + "\n");
                Log.d("TestImportCollection",  "did not match:\n" + expectedGames.get(i).toString());
            }
        }
        assertTrue(numberOfBadGames + " games were not the same", allEqual);
    }

    private void setupExpectedGames() {
        expectedGames = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> mechanics = new ArrayList<>();
        categories.add("Ancient");
        categories.add("Card Game");
        categories.add("City Building");
        categories.add("Civilization");
        mechanics.add("Card Drafting");
        mechanics.add("Hand Management");
        mechanics.add("Set Collection");
        mechanics.add("Simultaneous Action Selection");
        mechanics.add("Variable Player Powers");
        String description = "You are the leader of one of the 7 great cities of the Ancient World. Gather resources, develop commercial routes, and affirm your military supremacy. Build your city and erect an architectural wonder which will transcend future times.\n" +
                "\n"+
                "7 Wonders lasts three ages. In each age, players receive seven cards from a particular deck, choose one of those cards, then pass the remainder to an adjacent player. Players reveal their cards simultaneously, paying resources if needed or collecting resources or interacting with other players in various ways. (Players have individual boards with special powers on which to organize their cards, and the boards are double-sided). Each player then chooses another card from the deck they were passed, and the process repeats until players have six cards in play from that age. After three ages, the game ends.\n" +
                "\n" +
                "In essence, 7 Wonders is a card development game. Some cards have immediate effects, while others provide bonuses or upgrades later in the game. Some cards provide discounts on future purchases. Some provide military strength to overpower your neighbors and others give nothing but victory points. Each card is played immediately after being drafted, so you'll know which cards your neighbor is receiving and how his choices might affect what you've already built up. Cards are passed left-right-left over the three ages, so you need to keep an eye on the neighbors in both directions.\n" +
                " \n" +
                "Though the box of earlier editions is listed as being for 3&ndash;7 players, there is an official 2-player variant included in the instructions.\n\n";
        String thumbnail = "https://cf.geekdo-images.com/images/pic860217_t.jpg";
        expectedGames.add(new Game(68448, "7 Wonders", 2, 7, 2010, 30, thumbnail, 10, 10, categories, mechanics, 4, description));

        categories = new ArrayList<>();
        mechanics = new ArrayList<>();

        thumbnail = "https://cf.geekdo-images.com/images/pic400196_t.jpg";
        description = "From the publisher: Android is a board game of murder and conspiracy set in a dystopian future. Detectives travel between the city of New Angeles and moon colony Heinlein chasing down leads, calling in favors, and uncovering the sinister conspiracy beneath it all. The detectives must balance their pursuit of the murderer against their personal lives and their inner demons. Android's innovative mechanics ensure that no two detectives play alike. Will you play as Louis Blaine, the crooked cop tormented by guilt and loss? Or will you take the role of Caprice Nisei, the psychic clone who struggles to retain her sanity while proving that she's as human as anyone else? Whoever you choose to play, you've got just two weeks to solve the murder, uncover the conspiracy, and face your personal demons.\n" +
                "\n" +
                "During a round, the players get action points to spend on various actions, like moving their detectives, following leads that appear on locations, solving their personal demons, or doing a location-specific action. In this way, the detectives try to advance on three different sectors: solving the murder, uncovering the conspiracy behind the murder, and finding their inner peace. To solve the murder, the detectives follow leads that appear on New Angeles and Heinlein, and find evidence that they plant on the suspect of their choice. The conspiracy puzzle is also solved by following leads, and may alter the way victory points are distributed at the end of the game. Finally, each detective has his/her own personal plots that are resolved gradually throughout the game. If the detective invests enough time in the plot, he/she will be awarded victory points; otherwise, he will suffer penalties.\n" +
                "\n" +
                "The game ends when two in-game weeks (12 rounds) have passed. At that point, victory points are awarded based on the aforementioned sectors. The player with the most victory points is the winner.\n" +
                "\n";
        categories.add("Murder/Mystery");
        categories.add("Science Fiction");
        mechanics.add("Action Point Allowance System");
        mechanics.add("Hand Management");
        mechanics.add("Variable Player Powers");
        expectedGames.add(new Game(39339, "Android", 3, 5, 2008, 180, thumbnail, 13,16, categories, mechanics, 3, description));

        categories = new ArrayList<>();
        mechanics = new ArrayList<>();
        thumbnail = "https://cf.geekdo-images.com/images/pic354500_t.jpg";
        description = "Battlestar Galactica: The Board Game is an exciting game of mistrust, intrigue, and the struggle for survival. Based on the epic and widely-acclaimed Sci Fi Channel series, Battlestar Galactica: The Board Game puts players in the role of one of ten of their favorite characters from the show. Each playable character has their own abilities and weaknesses, and must all work together in order for humanity to have any hope of survival. However, one or more players in every game secretly side with the Cylons. Players must attempt to expose the traitor while fuel shortages, food contaminations, and political unrest threatens to tear the fleet apart.\n" +
                "\n" +
                "After the Cylon attack on the Colonies, the battered remnants of the human race are on the run, constantly searching for the next signpost on the road to Earth. They face the threat of Cylon attack from without, and treachery and crisis from within. Humanity must work together if they are to have any hope of survival&hellip;but how can they, when any of them may, in fact, be a Cylon agent?\n" +
                "\n" +
                "Battlestar Galactica: The Board Game is a semi-cooperative game for 3-6 players ages 10 and up that can be played in 2-3 hours. Players choose from pilots, political leaders, military leaders, or engineers to crew Galactica. They are also dealt a loyalty card at the start of the game to determine if they are a human or Cylon along with an assortment of skill cards based on their characters abilities. Players then can move and take actions either on Galactica, on Colonial 1, or in a Viper. They need to collect skill cards, fend off Cylon ships, and keep Galactica and the fleet jumping. Each turn also brings a Crisis Card, various tasks that players must overcome. Players need to play matching skill cards to fend off the problems; skill cards that don't match hinder the players success. Fate could be working against the crew, or there could be a traitorous Cylon! As players get closer and closer towards reaching their Earth, another round of loyalty cards are passed out and more Cylons may turn up. If players can keep their up their food stores, fuel levels, ship morale, and population, and they can keep Galactica in one piece long enough to make it to Earth, the Humans win the game. But if the Cylon players reveal themselves at the right moment and bring down Galactica, the Humans have lost.\n" +
                "\n" +
                "Official Site, Rules &amp; FAQ: http://www.fantasyflightgames.com/edge_minisite_sec.asp?eidm=18&amp;esem=4\n" +
                "Unofficial FAQ for really tricky questions: http://boardgamegeek.com/wiki/page/Battlestar_Galactica_FAQ\n\n";
        categories.add("Bluffing");
        categories.add("Deduction");
        categories.add("Movies / TV / Radio theme");
        categories.add("Political");
        categories.add("Science Fiction");
        categories.add("Space Exploration");
        categories.add("Spies/Secret Agents");
        mechanics.add("Area Movement");
        mechanics.add("Co-operative Play");
        mechanics.add("Dice Rolling");
        mechanics.add("Hand Management");
        mechanics.add("Partnerships");
        mechanics.add("Role Playing");
        mechanics.add("Variable Player Powers");
        expectedGames.add(new Game(37111, "Battlestar Galactica: The Board Game", 3, 6, 2008, 300, thumbnail, 10, 14, categories, mechanics, 5, description));

        categories = new ArrayList<>();
        mechanics = new ArrayList<>();

        thumbnail = "https://cf.geekdo-images.com/images/pic2247647_t.jpg";
        description = "Star Wars: Imperial Assault is a strategy board game of tactical combat and missions for two to five players, offering two distinct games of battle and adventure in the Star Wars universe!\n" +
                "\n" +
                "Imperial Assault puts you in the midst of the Galactic Civil War between the Rebel Alliance and the Galactic Empire after the destruction of the Death Star over Yavin 4. In this game, you and your friends can participate in two separate games. The campaign game pits the limitless troops and resources of the Galactic Empire against a crack team of elite Rebel operatives as they strive to break the Empire&rsquo;s hold on the galaxy, while the skirmish game invites you and a friend to muster strike teams and battle head-to-head over conflicting objectives.\n" +
                "\n" +
                "In the campaign game, Imperial Assault invites you to play through a cinematic tale set in the Star Wars universe. One player commands the seemingly limitless armies of the Galactic Empire, threatening to extinguish the flame of the Rebellion forever. Up to four other players become heroes of the Rebel Alliance, engaging in covert operations to undermine the Empire&rsquo;s schemes. Over the course of the campaign, both the Imperial player and the Rebel heroes gain new experience and skills, allowing characters to evolve as the story unfolds.\n" +
                "\n" +
                "Imperial Assault offers a different game experience in the skirmish game. In skirmish missions, you and a friend compete in head-to-head, tactical combat. You&rsquo;ll gather your own strike force of Imperials, Rebels, and Mercenaries and build a deck of command cards to gain an unexpected advantage in the heat of battle. Whether you recover lost holocrons or battle to defeat a raiding party, you&rsquo;ll find danger and tactical choices in every skirmish.\n" +
                "\n" +
                "As an additional benefit, the Luke Skywalker Ally Pack and the Darth Vader Villain Pack are included within the Imperial Assault Core Set. These figure packs offer sculpted plastic figures alongside additional campaign and skirmish missions that highlight both Luke Skywalker and Darth Vader within Imperial Assault. With these Imperial Assault Figure Packs, you'll find even more missions that allow your heroes to fight alongside these iconic characters from the Star Wars saga.\n" +
                "\n";
        categories.add("Adventure");
        categories.add("Fighting");
        categories.add("Miniatures");
        categories.add("Movies / TV / Radio theme");
        categories.add("Science Fiction");
        categories.add("Wargame");
        mechanics.add("Dice Rolling");
        mechanics.add("Grid Movement");
        mechanics.add("Modular Board");
        mechanics.add("Partnerships");
        mechanics.add("Role Playing");
        mechanics.add("Variable Player Powers");
        expectedGames.add(new Game(164153, "Star Wars: Imperial Assault", 2, 5, 2014, 120, thumbnail, 14, 12, categories, mechanics, 5, description));

        categories = new ArrayList<>();
        mechanics = new ArrayList<>();
        thumbnail = "https://cf.geekdo-images.com/images/pic2737530_t.png";
        description = "From the publisher:\n" +
                "\n" +
                "Star Wars: Rebellion is a board game of epic conflict between the Galactic Empire and Rebel Alliance for two to four players!\n" +
                "\n" +
                "Experience the Galactic Civil War like never before. In Rebellion, you control the entire Galactic Empire or the fledgling Rebel Alliance. You must command starships, account for troop movements, and rally systems to your cause. Given the differences between the Empire and Rebel Alliance, each side has different win conditions, and you'll need to adjust your play style depending on who you represent:\n" +
                "\n" +
                "     As the Imperial player, you can command legions of Stormtroopers, swarms of TIEs, Star Destroyers, and even the Death Star. You rule the galaxy by fear, relying on the power of your massive military to enforce your will. To win the game, you need to snuff out the budding Rebel Alliance by finding its base and obliterating it. Along the way, you can subjugate worlds or even destroy them.\n" +
                "     As the Rebel player, you can command dozens of troopers, T-47 airspeeders, Corellian corvettes, and fighter squadrons. However, these forces are no match for the Imperial military. In terms of raw strength, you'll find yourself clearly overmatched from the very outset, so you'll need to rally the planets to join your cause and execute targeted military strikes to sabotage Imperial build yards and steal valuable intelligence. To win the Galactic Civil War, you'll need to sway the galaxy's citizens to your cause. If you survive long enough and strengthen your reputation, you inspire the galaxy to a full-scale revolt, and you win.\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Featuring more than 150 plastic miniatures and two game boards that account for thirty-two of the Star Wars galaxy's most notable systems, Rebellion features a scope that is as large and sweeping as any Star Wars game before it.\n" +
                "\n" +
                "Yet for all its grandiosity, Rebellion remains intensely personal, cinematic, and heroic. As much as your success depends upon the strength of your starships, vehicles, and troops, it depends upon the individual efforts of such notable characters as Leia Organa, Mon Mothma, Grand Moff Tarkin, and Emperor Palpatine. As civil war spreads throughout the galaxy, these leaders are invaluable to your efforts, and the secret missions they attempt will evoke many of the most inspiring moments from the classic trilogy. You might send Luke Skywalker to receive Jedi training on Dagobah or have Darth Vader spring a trap that freezes Han Solo in carbonite!\n" +
                "\n";
        categories.add("Dice");
        categories.add("Fighting");
        categories.add("Miniatures");
        categories.add("Movies / TV / Radio theme");
        categories.add("Science Fiction");
        categories.add("Wargame");
        mechanics.add("Area Control / Area Influence");
        mechanics.add("Area Movement");
        mechanics.add("Dice Rolling");
        mechanics.add("Hand Management");
        mechanics.add("Variable Player Powers");
        expectedGames.add(new Game(187645, "Star Wars: Rebellion", 2, 4, 2016, 240, thumbnail, 14, 14, categories, mechanics, 2, description));

    }

}
