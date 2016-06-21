package com.platformer.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.platformer.game.listeners.MobileControlListener;

import static com.platformer.game.utils.Constants.WORLD_SIZE;

/**
 * Created by hátén on 2016. 06. 21..
 */
public class MobileControls implements MobileControlListener{

    private Rectangle left;
    private Rectangle right;
    private Vector2 leftCenter;
    private Vector2 rightCenter;
    private Rectangle jump;
    private Viewport viewport;
    private Camera camera;
    private Array<Rectangle> testButtons;

    public MobileControls(Camera camera){
        viewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE, camera);
        left = new Rectangle(10, 10, 20, 20);
        right = new Rectangle(50, 10, 20, 20);
        leftCenter = getCenter(left);
        rightCenter = getCenter(right);
        testButtons = new Array<Rectangle>();

        testButtons.add(left);
        testButtons.add(right);
    }

    public void debugRender(ShapeRenderer renderer){
        for (Rectangle rect : testButtons){
            renderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
    }


    public Viewport getViewport(){
        return this.viewport;
    }

    public Camera getCamera(){
        return this.camera;
    }


    private Vector2 getCenter(Rectangle rect){
        return new Vector2(rect.x + rect.getWidth() / 2, rect.y + rect.getHeight() / 2);
    }


    @Override
    public boolean isLeftButtonPressed() {
        return isButtonPressed(leftCenter);
    }

    @Override
    public boolean isRightButtonPressed() {
        return isButtonPressed(rightCenter);
    }

    @Override
    public boolean isJumpButtonPressed() {
        return false;
    }

    private boolean isButtonPressed(Vector2 center){
        float x = Gdx.input.getX(0);
        float y = Gdx.input.getY(0);
        Vector2 coords = viewport.unproject(new Vector2(x, y));

        Gdx.app.log("mc", "coords: " + coords);
        if (Gdx.input.isTouched(0))
            if (coords.dst(center) < 20)
                return true;
        return false;
    }
}
