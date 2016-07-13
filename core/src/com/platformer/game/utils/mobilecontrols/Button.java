package com.platformer.game.utils.mobilecontrols;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import static com.platformer.game.utils.Constants.*;

/**
 * Created by Peter Rado on 2016. 06. 24..
 * Present a button object.
 */

public class Button {
    private TextureRegion textureRegion;
    private Vector2 position;
    private Vector2 center;
    private float width;
    private float height;

    public Button(float x, float y){
        this.position = new Vector2(x, y);
    }

    public Button(float x, float y, TextureRegion region){
        this(x, y);
        textureRegion = region;
    }

    public Button(float x, float y, float width, float height){
        this(x, y);
        this.center = new Vector2();
        this.width = width;
        this.height = height;
        center = new Vector2(position.x + width / 2, position.y + height / 2);
    }

    public void render(SpriteBatch batch){
        batch.draw(textureRegion, position.x, position.y, 0, 0, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), BUTTON_SCALE, BUTTON_SCALE, 0);
    }

    public void debugRender(ShapeRenderer renderer){
        renderer.rect(position.x, position.y, width, height);
    }

    /**
     * This functions sets the center position of the buttons with texture, like circle
     * and square buttons. We call it after the object is created, else we run into
     * NullPointerException, because we calculate the width and height from the textureRegion's
     * properties.
     */
    public void setCenter(){
        width = textureRegion.getRegionWidth();
        height = textureRegion.getRegionHeight();
        float centerX = position.x + width / 2;
        float centerY = position.y + height / 2;
        this.center = new Vector2(centerX, centerY);
    }

    /**
     * It the parameter is true, it means this function is for the
     * Buttons without texture and fixed width and height
     * @param notNull if true we set
     */
    public void setCenter(boolean notNull){
        if (notNull){
            float centerX = position.x + width / 2;
            float centerY = position.y + height / 2;
            this.center = new Vector2(centerX, centerY);
        }
    }

    public Vector2 getPosition() { return position; }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    /**
     * Check if a button is touched or not.
     * @param touchCoords unprojected coordinates of a screen touch
     * @return true if the touch's coordinates are inside the button
     */
    public boolean isPressed(Vector2 touchCoords){
        return touchCoords.dst(center) < width / 2;
    }
}
