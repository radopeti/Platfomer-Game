package com.platformer.game.utils.mobilecontrols;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.platformer.game.listeners.MobileControlListener;
import com.platformer.game.utils.Assets;

import static com.platformer.game.utils.Constants.FB_HORIZONTAL_OFFSET;
import static com.platformer.game.utils.Constants.FB_VERTICAL_OFFSET;
import static com.platformer.game.utils.Constants.JB_HORIZONTAL_OFFSET;
import static com.platformer.game.utils.Constants.JB_VERTICAL_OFFSET;
import static com.platformer.game.utils.Constants.WORLD_SIZE;

/**
 * Created by Peter Rado on 2016. 06. 21..
 * This class represents the mobile controls. It renders the buttons and also polling the input
 * from the screen. It implements the MobileControlListener class to call appropriate functions
 * if a button is pressed.
 * It has a separate viewport and camera, to fix the position of the buttons.
 */
public class MobileControls implements MobileControlListener{

    private Viewport viewport;
    private Array<Button> buttons;
    private MovementButton movement;
    private Button jump;
    private Button fire;
    private float currentViewportWidth;

    public MobileControls(Camera camera){
        viewport = new ExtendViewport(WORLD_SIZE, WORLD_SIZE, camera);
        buttons = new Array<Button>();
        movement = new MovementButton(0, 0, Assets.instance.mobileControlButtons.movement);
        jump = new Button(0, 0, Assets.instance.mobileControlButtons.circle);
        fire = new Button(0, 0, Assets.instance.mobileControlButtons.square);
        buttons.add(movement);
        buttons.add(jump);
        buttons.add(fire);
        initCenterPositions();
    }

    private void initCenterPositions(){
        for (Button button : buttons){
            button.setCenter();
        }
    }

    public void debugRender(ShapeRenderer renderer){
        for (Button button : buttons){
            button.debugRender(renderer);
        }
    }

    public void render(SpriteBatch batch){
        currentViewportWidth = viewport.getWorldWidth();
        jump.setPosition(currentViewportWidth - JB_HORIZONTAL_OFFSET, 0 + JB_VERTICAL_OFFSET);
        jump.setCenter();
        fire.setPosition(currentViewportWidth - FB_HORIZONTAL_OFFSET, 0 + FB_VERTICAL_OFFSET);
        fire.setCenter();
        for (Button button : buttons){
            button.render(batch);
        }
    }

    public Viewport getViewport(){
        return this.viewport;
    }


    private Vector2 getCenter(Rectangle rect){
        return new Vector2(rect.x + rect.getWidth() / 2, rect.y + rect.getHeight() / 2);
    }


    @Override
    public boolean isLeftButtonPressed() {
        if (isFireOrJumpPressed()) return isButtonPressed(movement.left, 1);
        return isButtonPressed(movement.left, 0);
    }

    @Override
    public boolean isRightButtonPressed() {
        if (isFireOrJumpPressed()) return isButtonPressed(movement.right, 1);
        return isButtonPressed(movement.right, 0);
    }

    @Override
    public boolean isDownButtonPressed() {
        if (isFireOrJumpPressed()) return isButtonPressed(movement.down, 1);
        return isButtonPressed(movement.down, 0);
    }

    @Override
    public boolean isUpButtonPressed() {
        if (isFireOrJumpPressed()) return isButtonPressed(movement.up, 1);
        return isButtonPressed(movement.up, 0);
    }

    @Override
    public boolean isJumpButtonPressed() {
        if (isButtonPressed(movement.left, 0) || isButtonPressed(movement.right, 0))
            return isButtonPressed(jump, 1);
        else if (isButtonPressed(movement.up, 0) || isButtonPressed(movement.down, 0))
            return isButtonPressed(jump, 1);
        return isButtonPressed(jump, 0);
    }

    @Override
    public boolean isFireButtonPressed() {
        if (isButtonPressed(movement.left, 0) || isButtonPressed(movement.right, 0))
            return isButtonPressed(fire, 1);
        else if (isButtonPressed(movement.up, 0) || isButtonPressed(movement.down, 0))
            return isButtonPressed(fire, 1);
        return isButtonPressed(fire, 0);
    }

    private boolean isButtonPressed(Button button, int pointer){
        float x = Gdx.input.getX(pointer);
        float y = Gdx.input.getY(pointer);
        Vector2 coords = viewport.unproject(new Vector2(x, y));

        if (Gdx.input.isTouched(pointer))
            if (button.isPressed(coords))
                return true;
        return false;
    }

    //helper method to avoid code duplication
    private boolean isFireOrJumpPressed(){
        return isButtonPressed(jump, 0) || isButtonPressed(fire, 0);
    }
}
