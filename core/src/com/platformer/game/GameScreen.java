package com.platformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.platformer.game.characters.MegaMan;
import com.platformer.game.utils.Assets;
import com.platformer.game.utils.MapObjectLoader;
import com.platformer.game.utils.MobileControls;

import java.util.Iterator;

import javax.swing.text.html.HTMLDocument;

import javafx.beans.property.MapProperty;

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
    private ShapeRenderer mobileControlRenderer;
    //Camera object
    private OrthographicCamera camera;
    private OrthographicCamera guiCam;
    //Viewport
    private Viewport viewport;

    private Level level;

    MobileControls mc;
    @Override
    public void show() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE, camera);
        camera.setToOrtho(false);
        Assets.instance.init(new AssetManager());

        level = new Level(camera, viewport, batch);
        level.setDebugOn(true);



        mobileControlRenderer = new ShapeRenderer();
        guiCam = new OrthographicCamera(WORLD_SIZE, WORLD_SIZE);

        Gdx.app.log("tag", "vp width" + camera.viewportWidth);


        guiCam.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        guiCam.update();
        mc = new MobileControls(guiCam);
        level.setMobileListener(mc);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        mc.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();

        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        mobileControlRenderer.setProjectionMatrix(guiCam.combined);

        level.update(delta);
        level.renderMap();

        renderer.begin(ShapeRenderer.ShapeType.Line);
            level.debugRender(renderer);
        renderer.end();

        mobileControlRenderer.begin(ShapeRenderer.ShapeType.Filled);
            mc.debugRender(mobileControlRenderer);
        mobileControlRenderer.end();

        batch.begin();
            level.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        level.dispose();
        mobileControlRenderer.dispose();
    }
}
