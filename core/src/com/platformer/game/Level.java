package com.platformer.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.platformer.game.characters.MegaMan;
import com.platformer.game.mapcomponents.Ladder;
import com.platformer.game.mapcomponents.Platform;
import com.platformer.game.utils.CustomCam;
import com.platformer.game.utils.MapObjectLoader;
import com.platformer.game.utils.MapUtils;

import static com.platformer.game.utils.Constants.TESTMAP_NAME;

/**
 * Created by radopeti on 2016. 05. 17..
 * This class renders the map, characters
 */
public class Level implements Disposable {
    private MegaMan megaMan;
    private boolean debugOn;
    private Array<Platform> platforms;
    private Array<Ladder> ladders;
    private Viewport viewport;
    private OrthographicCamera camera;

    //tiled map
    private TiledMap tiledMap;
    private MapRenderer mapRenderer;
    private int mapWidth;
    private int mapHeight;

    public Level(OrthographicCamera camera, Viewport viewport, SpriteBatch batch) {
        debugOn = false;
        platforms = new Array<Platform>();
        ladders = new Array<Ladder>();
        megaMan = new MegaMan(20, 20);
        this.viewport = viewport;
        this.camera = camera;

        tiledMap = new TmxMapLoader().load(TESTMAP_NAME);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        mapRenderer.setView(this.camera);

        createLadders(MapObjectLoader.getLadderColliders(tiledMap));
        createPlatforms(MapObjectLoader.getPlatformColliders(tiledMap));
        mapWidth = MapUtils.getMapWidth(tiledMap);
        mapHeight = MapUtils.getMapHeight(tiledMap);
    }

    public void update(float delta) {
        megaMan.update(delta, platforms, ladders);
        updateCamera();
    }

    public void renderMap(){
        mapRenderer.render();
    }

    public void render(SpriteBatch batch) {
        megaMan.render(batch);

    }

    public void debugRender(ShapeRenderer renderer) {
        if (debugOn) {
            for (Platform platform : platforms) {
                platform.render(renderer);
            }
            for (Ladder ladder : ladders){
                ladder.render(renderer);
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

    public void createLadders(Array<Rectangle> rectangles) {
        for (Rectangle rectangle : rectangles){
            Ladder ladder = new Ladder(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            ladders.add(ladder);
        }
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
    }

    private void updateCamera(){
        if (megaMan.getPosition().x - viewport.getWorldWidth() / 2 > 0 && megaMan.getPosition().x + viewport.getWorldWidth() / 2 < mapWidth){
            camera.position.x = megaMan.getPosition().x;
        }
        if (megaMan.getPosition().y - viewport.getWorldHeight() / 2 > 0 && megaMan.getPosition().y + viewport.getWorldHeight() / 2 < mapHeight){
            camera.position.y = megaMan.getPosition().y;
        }
    }
}
