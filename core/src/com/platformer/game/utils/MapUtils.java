package com.platformer.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by hátén on 2016. 05. 28..
 */
public class MapUtils {

    public static int getMapWidth(TiledMap map){
        int width = (Integer) map.getProperties().get("width");
        int tileWidth = (Integer) map.getProperties().get("tilewidth");
        return width * tileWidth;
    }

    public static int getMapHeight(TiledMap map){
        int height = (Integer) map.getProperties().get("height");
        int tileHeight = (Integer) map.getProperties().get("tileheight");
        return height * tileHeight;
    }

    public static Vector2 getStartPosition(TiledMap map){
        MapObjects mapObjects = map.getLayers().get("StartPosition").getObjects();
        MapObject object = mapObjects.get(0);
        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
        float x = rectangle.getX();
        float y = rectangle.getY();
        Vector2 position = new Vector2(x, y);
        return position;
    }
}
