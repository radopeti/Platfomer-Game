package com.platformer.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Map;

/**
 * Created by hátén on 2016. 05. 22..
 */
public class MapObjectLoader {

    public static Array<Rectangle> getPlatformColliders(TiledMap map) {
        MapObjects mapObjects = map.getLayers().get("PlatformColliders").getObjects();
        Array<Rectangle> rectangles = new Array<Rectangle>();
        for (MapObject object : mapObjects) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            rectangles.add(rectangle);
        }
        return rectangles;
    }

    public static Array<Rectangle> getLadderColliders(TiledMap map) {
        MapObjects mapObjects = map.getLayers().get("Ladders").getObjects();
        Array<Rectangle> rectangles = new Array<Rectangle>();
        for (MapObject object : mapObjects) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            rectangles.add(rectangle);
        }
        return rectangles;
    }

}
