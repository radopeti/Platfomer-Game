package com.platformer.game;

import com.badlogic.gdx.Game;

/*
This class allows the game to have multiple screens
 */

public class PlatformerGame extends Game {

    @Override
    public void create() {
		setScreen(new GameScreen());
    }
}
