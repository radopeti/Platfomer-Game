package com.platformer.game;

import static com.badlogic.gdx.Application.ApplicationType.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.platformer.game.utils.Assets;
import com.platformer.game.utils.EmptyControls;
import com.platformer.game.utils.mobilecontrols.MobileControls;

import static com.platformer.game.utils.Constants.*;
/**
 * Created by radopeti on 2016. 05. 17..
 * The class renders everything you see on the screen
 */

public class GameScreen extends ScreenAdapter{

    //render the textures
    private SpriteBatch batch;
    private SpriteBatch mobileControlBatch;
    //for debug rendering
    private ShapeRenderer renderer;
    private ShapeRenderer mobileControlRenderer;
    //Camera object
    private OrthographicCamera camera;
    private OrthographicCamera guiCam;
    //Viewport
    private Viewport viewport;

    private Level level;

    private MobileControls mc;
    private boolean isMobile = false;

    @Override
    public void show() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE, camera);
        camera.setToOrtho(false);

        //initializing assets
        AssetManager am = new AssetManager();
        Assets.instance.init(am);
        Assets.instance.initMobileControlButtons(am);
        level = new Level(camera, viewport, batch);
        level.setDebugOn(true);

        //if app runs on android, we add the mobile controls
        if (Gdx.app.getType().equals(Android)){
            isMobile = true;
            mobileControlRenderer = new ShapeRenderer();
            mobileControlBatch = new SpriteBatch();
            guiCam = new OrthographicCamera(WORLD_SIZE, WORLD_SIZE);
            guiCam.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
            guiCam.update();
            mc = new MobileControls(guiCam);
            level.setMobileListener(mc);
        }else{
            level.setMobileListener(new EmptyControls());
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (isMobile) mc.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();

        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);

        level.update(delta);
        level.renderMap();

        if (isMobile){
            mobileControlRenderer.setProjectionMatrix(guiCam.combined);
            mobileControlBatch.setProjectionMatrix(guiCam.combined);
            mobileControlBatch.begin();
                mc.render(mobileControlBatch);
            mobileControlBatch.end();

            mobileControlRenderer.begin(ShapeRenderer.ShapeType.Line);
                mc.debugRender(mobileControlRenderer);
            mobileControlRenderer.end();
        }


        renderer.begin(ShapeRenderer.ShapeType.Line);
            level.debugRender(renderer);
        renderer.end();

        batch.begin();
            level.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        level.dispose();

        if (isMobile){
            mobileControlRenderer.dispose();
            mobileControlBatch.dispose();
        }
    }
}
