package com.platformer.game.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;

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
}
