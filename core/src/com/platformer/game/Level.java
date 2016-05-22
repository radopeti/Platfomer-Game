package com.platformer.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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
            for (Platform platform : platforms) {
                platform.render(renderer);
            }
            renderer.setColor(Color.WHITE);
            megaMan.debugRenderer(renderer);
        }
    }

    public void setDebugOn(boolean debugOn) {
        this.debugOn = debugOn;
    }

    public void initTestPlatforms() {
        platforms.add(new Platform(10, 60, 20, 150));
        platforms.add(new Platform(40, 10, 150, 15));
        platforms.add(new Platform(60, 80, 80, 15));
        platforms.add(new Platform(100, 120, 50, 15));
        platforms.add(new Platform(160, 40, 30, 15));
        platforms.add(new Platform(190, 25, 20, 60));
        platforms.add(new Platform(220, 25, 30, 10));
        platforms.add(new Platform(240, 40, 20, 150));

    }

    public void createPlatforms(Array<Rectangle> rectangles) {
        for (Rectangle rectangle : rectangles){
            Platform platform = new Platform(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            platforms.add(platform);
        }
    }

}
