package com.platformer.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.platformer.game.utils.Assets;

import static com.platformer.game.utils.Constants.*;

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

enum Direction{
    LEFT,
    RIGHT
}

public class MegaMan {

    private TextureRegion currentRegion;
    private float stateTime;
    private boolean flipX;
    private Vector2 position;
    private Vector2 velocity;

    private MotionState motionState;
    private Direction direction;

    public MegaMan(){
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.velocity = Vector2.Zero;
        motionState = MotionState.STANDING;
        direction = Direction.RIGHT;
        flipX = true;
    }

    public MegaMan(float x, float y){
        this();
        this.position.set(x, y);
    }

    public MegaMan(Vector2 position){
        this();
        this.position.set(position);
    }

    public void update(float delta){
        if (Gdx.input.isKeyPressed(Keys.RIGHT)){
            moveRight(delta);
        }else if (Gdx.input.isKeyPressed(Keys.LEFT)){
            moveLeft(delta);
        }else{
            velocity.x = 0;
            motionState = MotionState.STANDING;
        }

        position.mulAdd(velocity, delta);
    }

    public void render(SpriteBatch batch){


        if (isMotionState(MotionState.STANDING)){
            stateTime += Gdx.graphics.getDeltaTime();
            currentRegion = Assets.instance.megaManAssets.standingAnimation.getKeyFrame(stateTime);
        }else if (isMotionState(MotionState.RUNNING)){
            currentRegion = Assets.instance.megaManAssets.runAnimation.getKeyFrame(stateTime);
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
                flipX,
                false);
    }

    public void debugRenderer(ShapeRenderer renderer){

    }

    public boolean isMotionState(MotionState motionState){
        if (this.motionState.equals(motionState)) return true;
        return false;
    }

    public boolean isDirection(Direction direction){
        if (this.direction.equals(direction)) return true;
        return false;
    }

    private void init(){
        this.velocity = Vector2.Zero;
        motionState = MotionState.STANDING;
    }

    private void moveLeft(float delta){
        stateTime += delta;
        velocity.x = -MEGAMAN_MOVEMENT_SPEED;
        motionState = MotionState.RUNNING;
        direction = Direction.LEFT;
        flipX = false;
    }

    private void moveRight(float delta){
        stateTime += delta;
        velocity.x = MEGAMAN_MOVEMENT_SPEED;
        motionState = MotionState.RUNNING;
        direction = Direction.RIGHT;
        flipX = true;
    }

}
