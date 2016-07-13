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

    //to render the textures in the game world
    private SpriteBatch batch;
    //to render the gui
    private SpriteBatch guiBatch;
    //for debug rendering
    private ShapeRenderer renderer;
    private ShapeRenderer guiRenderer;
    //Camera objects
    //camera if for the game world
    private OrthographicCamera camera;
    //guiCam is for gui
    private OrthographicCamera guiCam;
    //Viewport
    private Viewport viewport;
    //Level clas
    private Level level;
    //mobile controls
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
        level.setDebugOn(false);

        //if app runs on android, we add the mobile controls
        if (Gdx.app.getType().equals(Android)){
            isMobile = true;
            guiRenderer = new ShapeRenderer();
            guiBatch = new SpriteBatch();
            guiCam = new OrthographicCamera(WORLD_SIZE, WORLD_SIZE);
            guiCam.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
            guiCam.update();
            mc = new MobileControls(guiCam);
            level.setMobileListener(mc);
        }else{
            //set a empty MobileControlListener on to avoid NullPointerException
            //if the game runs on Desktop or WebGL
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
            guiRenderer.setProjectionMatrix(guiCam.combined);
            guiBatch.setProjectionMatrix(guiCam.combined);
            guiBatch.begin();
                mc.render(guiBatch);
            guiBatch.end();

            /*guiRenderer.begin(ShapeRenderer.ShapeType.Line);
                mc.debugRender(guiRenderer);
            guiRenderer.end();*/
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
            guiRenderer.dispose();
            guiBatch.dispose();
        }
    }
}
