package com.platformer.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.platformer.game.characters.MegaMan;
import com.platformer.game.entities.Bullet;
import com.platformer.game.listeners.BulletListener;
import com.platformer.game.listeners.MobileControlListener;
import com.platformer.game.mapcomponents.Ladder;
import com.platformer.game.mapcomponents.Platform;
import com.platformer.game.utils.Enums;
import com.platformer.game.utils.MapObjectLoader;
import com.platformer.game.utils.MapUtils;
import com.platformer.game.utils.mobilecontrols.MobileControls;

import java.util.Iterator;

import static com.platformer.game.utils.Constants.TESTMAP_NAME;

/**
 * Created by radopeti on 2016. 05. 17..
 * This class renders the map, characters
 */
public class Level implements Disposable, BulletListener {
    private MegaMan megaMan;
    private boolean debugOn;
    private Array<Platform> platforms;
    private Array<Ladder> ladders;
    private Array<Bullet> megaManBullets;
    private Viewport viewport;
    private OrthographicCamera camera;
    private float currentViewLeft;
    private float currentViewRight;

    //tiled map
    private TiledMap tiledMap;
    private MapRenderer mapRenderer;
    private int mapWidth;
    private int mapHeight;

    public Level(OrthographicCamera camera, Viewport viewport, SpriteBatch batch) {
        debugOn = false;
        platforms = new Array<Platform>();
        ladders = new Array<Ladder>();
        megaManBullets = new Array<Bullet>();
        this.viewport = viewport;
        this.camera = camera;
        updateCurrentView();
        tiledMap = new TmxMapLoader().load(TESTMAP_NAME);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        mapRenderer.setView(this.camera);

        createLadders(MapObjectLoader.getLadderColliders(tiledMap));
        createPlatforms(MapObjectLoader.getPlatformColliders(tiledMap));
        mapWidth = MapUtils.getMapWidth(tiledMap);
        mapHeight = MapUtils.getMapHeight(tiledMap);
        Vector2 startPosition = MapUtils.getStartPosition(tiledMap);

        megaMan = new MegaMan(startPosition);
        megaMan.setBulletListener(this);
    }

    public void update(float delta) {
        megaMan.update(delta, platforms, ladders);
        updateCurrentView();
        for (Bullet bullet : megaManBullets){
            bullet.update(delta);
            if (bullet.getPosition().x < currentViewLeft || bullet.getPosition().x > currentViewRight){
                bullet.setActive(false);
            }
        }
        removeInactiveBullets();
        updateCamera();
    }

    public void renderMap(){
        mapRenderer.render();
    }

    public void render(SpriteBatch batch) {
        megaMan.render(batch);
        for (Bullet bullet : megaManBullets){
            bullet.render(batch);
        }
    }

    public void debugRender(ShapeRenderer renderer) {
        if (debugOn) {
            for (Platform platform : platforms) {
                platform.render(renderer);
            }
            for (Ladder ladder : ladders){
                ladder.render(renderer);
            }
            for (Bullet bullet : megaManBullets){
                bullet.debugRender(renderer);
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

    private void removeInactiveBullets(){
        Iterator<Bullet> it = megaManBullets.iterator();
        while (it.hasNext()){
            Bullet bullet = it.next();
            if (!bullet.isActive()) it.remove();
        }
    }

    private void updateCurrentView(){
        currentViewLeft = camera.position.x - viewport.getWorldWidth() / 2;
        currentViewRight = camera.position.x + viewport.getWorldWidth() / 2;
    }

    @Override
    public void createBullet() {
        float x = 0;
        float y = 0;
        Enums.Direction direction = megaMan.getDirection();
        float height = megaMan.getShootingHeight();
        if (direction.equals(Enums.Direction.RIGHT)){
            x = megaMan.getPosition().x + megaMan.getHitBox().getWidth();
            y = megaMan.getPosition().y + height;
        }else if (direction.equals(Enums.Direction.LEFT)){
            x = megaMan.getPosition().x;
            y = megaMan.getPosition().y + height;
        }
        Bullet bullet = new Bullet(x, y, direction);
        megaManBullets.add(bullet);
    }

    public void setMobileListener(MobileControlListener mc){
        megaMan.setMobileControlListener(mc);
    }
}
