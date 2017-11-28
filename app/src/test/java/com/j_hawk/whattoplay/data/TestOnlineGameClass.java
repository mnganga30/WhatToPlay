package com.j_hawk.whattoplay.data;


import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class TestOnlineGameClass {

    @Test
    public void testEqualsNoThumbnail() {

        OnlineGame game = new OnlineGame(123, "Test", 5);

        assertTrue(game.equals(game));
    }

    @Test
    public void testEqualsWithThumbnail() {
        OnlineGame game = new OnlineGame(123, "Test", 5, "test");

        assertTrue(game.equals(game));
    }
}
