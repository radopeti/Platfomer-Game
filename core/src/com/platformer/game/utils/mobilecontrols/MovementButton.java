package com.platformer.game.utils.mobilecontrols;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import static com.platformer.game.utils.Constants.*;

/**
 * Created by hátén on 2016. 06. 24..
 */
public class MovementButton extends Button {

    public final Button left;
    public final Button right;
    public final Button up;
    public final Button down;
    public Array<Button> buttons;

    public MovementButton(float x, float y, TextureRegion region){
        super(x, y, region);
        buttons = new Array<Button>();
        left = new Button(x,
                y + MOVEMENT_BUTTON_HEIGHT,
                MOVEMENT_BUTTON_WIDTH,
                MOVEMENT_BUTTON_HEIGHT);
        right = new Button(x + 2 * MOVEMENT_BUTTON_WIDTH + 2 * MOVEMENT_BUTTON_OFFSET,
                y + MOVEMENT_BUTTON_HEIGHT,
                MOVEMENT_BUTTON_WIDTH,
                MOVEMENT_BUTTON_HEIGHT);
        down = new Button(x + MOVEMENT_BUTTON_WIDTH + MOVEMENT_BUTTON_OFFSET,
                y,
                MOVEMENT_BUTTON_WIDTH,
                MOVEMENT_BUTTON_HEIGHT);
        up = new Button(x + MOVEMENT_BUTTON_WIDTH + MOVEMENT_BUTTON_OFFSET,
                y + 2 * MOVEMENT_BUTTON_HEIGHT,
                MOVEMENT_BUTTON_WIDTH,
                MOVEMENT_BUTTON_HEIGHT);
        buttons.add(left);
        buttons.add(right);
        buttons.add(up);
        buttons.add(down);
    }

    public void debugRender(ShapeRenderer renderer){
        for (Button button : buttons){
            button.debugRender(renderer);
        }
    }

    @Override
    public void setCenter(){
        for (Button button : buttons){
            button.setCenter(true);
        }
    }

}
