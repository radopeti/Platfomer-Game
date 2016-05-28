package com.platformer.game.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by hátén on 2016. 05. 23..
 */
public abstract class CustomCam {

    private Object object;
    private Camera camera;


    public CustomCam(Camera camera){
        this.camera = camera;
    }

    public Camera getCamera(){
        return camera;
    }

    public abstract void setTarget(Object object);
    public abstract void update();
}
