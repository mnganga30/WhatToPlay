package com.j_hawk.whattoplay.data;


import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;

public class TestGameClass {

    @Test
    public void testEquals() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("abc");
        categories.add("def");
        ArrayList<String> mechanic = new ArrayList<>();
        mechanic.add("12345");
        mechanic.add("67890");
        Game game = new Game(123, "Test", 1, 2, 3, 4, "Test", 5, 6, categories, mechanic, 7, "Test");

        assertTrue(game.equals(game));
    }

}
