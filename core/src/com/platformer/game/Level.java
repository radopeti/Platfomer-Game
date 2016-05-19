package com.platformer.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.platformer.game.characters.MegaMan;

import static com.platformer.game.utils.Constants.*;

/**
 * Created by radopeti on 2016. 05. 17..
 * This class renders the map, characters
 */
public class Level {
    private MegaMan megaMan;
    private boolean debugOn;

    public Level() {
        debugOn = false;
        megaMan = new MegaMan(20, 20);
    }

    public void update(float delta) {
        megaMan.update(delta);
    }

    public void render(SpriteBatch batch) {
        megaMan.render(batch);
    }

    public void debugRender(ShapeRenderer renderer) {
        if (debugOn) {
        }
    }

    public void setDebugOn(boolean debugOn) {
        this.debugOn = debugOn;
    }
}
