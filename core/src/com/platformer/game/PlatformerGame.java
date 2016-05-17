package com.platformer.game;

import com.badlogic.gdx.Game;

public class PlatformerGame extends Game {

    @Override
    public void create() {
		setScreen(new GameScreen());
    }
}
