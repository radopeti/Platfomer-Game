package com.platformer.game.mapcomponents;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by radopeti on 2016. 05. 17..
 * This is a rectangle object on the map which help walk and jump on the map
 */
public class Platform {
    private Rectangle rectangle;
    private float left;
    private float right;
    private float top;
    private float bottom;
    private Color color;

    public Platform() {
        rectangle = new Rectangle();
        color = Color.BLACK;
    }

    public Platform(float x, float y, float width, float height) {
        this();
        rectangle.set(x, y, width, height);
        left = x;
        right = x + width;
        bottom = y;
        top = y + height;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void render(ShapeRenderer renderer) {
        renderer.setColor(color);
        renderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    @Override
    public String toString(){
        return "[x=" + rectangle.x + ", y=" + rectangle.y + ", width=" + rectangle.width +
                ", height=" + rectangle.height + "]";
    }
}
