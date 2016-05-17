package com.platformer.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.platformer.game.utils.Assets;

/**
 * Created by radopeti on 2016. 05. 17..
 * The main character.
 * He can walk, jump, climb on the map and eliminate
 * enemies by shooting on them.
 */


enum MotionState{
    STANDING,
    RUNNING
}

public class MegaMan {

    private TextureRegion currentRegion;
    private float stateTime;
    private Vector2 position;
    private Vector2 velocity;

    private MotionState motionState;

    public MegaMan(){
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.velocity = Vector2.Zero;
        motionState = MotionState.STANDING;
    }

    public MegaMan(float x, float y){
        this();
        this.position.set(x, y);
    }

    public MegaMan(Vector2 position){
        this();
        this.position.set(position);
    }

    public void update(){

    }

    public void render(SpriteBatch batch){

        stateTime += Gdx.graphics.getDeltaTime();

        if (isMotionState(MotionState.STANDING)){
            currentRegion = Assets.instance.megaManAssets.standingAnimation.getKeyFrame(stateTime);
        }

        batch.draw(currentRegion.getTexture(),
                position.x,
                position.y,
                0,
                0,
                currentRegion.getRegionWidth(),
                currentRegion.getRegionHeight(),
                1,
                1,
                0,
                currentRegion.getRegionX(),
                currentRegion.getRegionY(),
                currentRegion.getRegionWidth(),
                currentRegion.getRegionHeight(),
                false,
                false);
    }

    public void debugRenderer(ShapeRenderer renderer){

    }

    public boolean isMotionState(MotionState motionState){
        if (this.motionState.equals(motionState)) return true;
        return false;
    }

    private void init(){
        this.velocity = Vector2.Zero;
        motionState = MotionState.STANDING;
    }

}
