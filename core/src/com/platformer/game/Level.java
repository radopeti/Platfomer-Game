package com.platformer.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.platformer.game.characters.MegaMan;
import com.platformer.game.mapcomponents.Platform;

/**
 * Created by radopeti on 2016. 05. 17..
 * This class renders the map, characters
 */
public class Level {
    private MegaMan megaMan;
    private boolean debugOn;
    private Array<Platform> platforms;

    public Level() {
        debugOn = false;
        platforms = new Array<Platform>();
        megaMan = new MegaMan(20, 20);
    }

    public void update(float delta) {
        megaMan.update(delta, platforms);
    }

    public void render(SpriteBatch batch) {
        megaMan.render(batch);
    }

    public void debugRender(ShapeRenderer renderer) {
        if (debugOn) {
            renderer.end();
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(Color.BLACK);
            for (Platform platform : platforms) {
                platform.render(renderer);
            }
            renderer.end();
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(Color.WHITE);
            megaMan.debugRenderer(renderer);
        }
    }

    public void setDebugOn(boolean debugOn) {
        this.debugOn = debugOn;
    }

    public void initPlatforms() {
        platforms.add(new Platform(60, 10, 30, 10));
        platforms.add(new Platform(100, 20, 30, 10));
        platforms.add(new Platform(140, 30, 50, 10));
    }

    public void initPlatforms(Array<Platform> platforms) {

    }
}
