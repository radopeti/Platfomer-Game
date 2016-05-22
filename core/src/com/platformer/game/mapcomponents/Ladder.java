package com.platformer.game.mapcomponents;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by radopeti on 2016. 05. 17..
 * This is a rectangle object on the map which help climb on ladders on the map
 */
public class Ladder {

    private float x;
    private float y;
    private float width;
    private float height;
    private Rectangle rectangle;


    public Ladder(){
        this.rectangle = new Rectangle();
    }

    public Ladder(float x, float y, float width, float height){
        this();
        rectangle.set(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(ShapeRenderer renderer){
        renderer.rect(x, y, width, height);
    }

    public float getHeight() {
        return height;
    }


    public float getWidth() {
        return width;
    }


    public float getY() {
        return y;
    }


    public float getX() {
        return x;
    }

    public Rectangle getRectangle(){
        return rectangle;
    }
}
