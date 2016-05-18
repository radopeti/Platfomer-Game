package com.platformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.platformer.game.characters.MegaMan;
import com.platformer.game.utils.Assets;

import static com.platformer.game.utils.Constants.*;
/**
 * Created by radopeti on 2016. 05. 17..
 * The class renders everything you see on the screen
 */

public class GameScreen extends ScreenAdapter{

    //render the textures
    private SpriteBatch batch;
    //for debug rendering
    private ShapeRenderer renderer;
    //Camera object
    private OrthographicCamera camera;
    //Viewport
    private Viewport viewport;

    private Level level;

    private MegaMan megaMan;

    @Override
    public void show() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE, camera);

        AssetManager assetManager = new AssetManager();
        Assets.instance.init(assetManager);

        megaMan = new MegaMan(20, 20);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        megaMan.update(delta);

        batch.begin();

        megaMan.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
    }
}
