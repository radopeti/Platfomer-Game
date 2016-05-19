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


enum WalkingState{
    STANDING,
    RUNNING
}

enum Direction{
    LEFT,
    RIGHT
}

enum JumpState{
    JUMPING,
    FALLING,
    GROUNDED
}

public class MegaMan {

    private static final String TAG = MegaMan.class.getName();

    private TextureRegion currentRegion;
    private float stateTime;
    private boolean flipX;
    private Vector2 position;
    private Vector2 lastPosition;
    private Vector2 velocity;

    private WalkingState walkingState;
    private Direction direction;
    private JumpState jumpState;

    /**
     * Default constructor
     */
    public MegaMan(){
        this.position = new Vector2();
        this.lastPosition = new Vector2();
        this.velocity = new Vector2();
        this.velocity = Vector2.Zero;
        walkingState = WalkingState.STANDING;
        direction = Direction.RIGHT;
        jumpState = JumpState.FALLING;
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

        velocity.y -= GRAVITY;

        if (position.y <= 0){
            position.y = 0;
            velocity.y = 0;
            jumpState = JumpState.GROUNDED;
        }

        //move controls
        if (Gdx.input.isKeyPressed(Keys.RIGHT)){
            moveRight(delta);
        }else if (Gdx.input.isKeyPressed(Keys.LEFT)){
            moveLeft(delta);
        }else{
            velocity.x = 0;
            walkingState = WalkingState.STANDING;
        }

        if (Gdx.input.isKeyPressed(Keys.X)){
            if (isJumpState(JumpState.GROUNDED)){
                Gdx.app.log(TAG, "jump state 1: " + jumpState);
                jumpState = JumpState.JUMPING;
            }else if (isJumpState(JumpState.JUMPING)){
                Gdx.app.log(TAG, "jump state 2: " + position.y);
                if (position.y < MEGAMAN_JUMP_HEIGHT){
                    Gdx.app.log(TAG, "still jumping " + jumpState);
                }else{
                    Gdx.app.log(TAG, "falling cause hit the max height " + jumpState);
                    velocity.y = 0;
                    jumpState = JumpState.FALLING;
                }
            }
        }else{
            if (lastPosition.y > position.y && !isJumpState(JumpState.GROUNDED)){
                Gdx.app.log(TAG, "falling by released jump button: " + jumpState);
                jumpState = JumpState.FALLING;
            }
        }


        lastPosition.set(position);
        position.mulAdd(velocity, delta);
    }


    /**
     * Draw the current TextureRegion depend on the current state of MegaMan
     * @param batch SpriteBatch
     */
    public void render(SpriteBatch batch){


        if (isWalkingState(WalkingState.STANDING)){
            stateTime += Gdx.graphics.getDeltaTime();
            currentRegion = Assets.instance.megaManAssets.standingAnimation.getKeyFrame(stateTime);
        }else if (isWalkingState(WalkingState.RUNNING)){
            currentRegion = Assets.instance.megaManAssets.runAnimation.getKeyFrame(stateTime);
        }

        if (isJumpState(JumpState.JUMPING)){
            currentRegion = Assets.instance.megaManAssets.jumpingRegion;
        }else if (isJumpState(JumpState.FALLING)){
            currentRegion = Assets.instance.megaManAssets.fallingRegion;
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

    /**
     * Helper method instead of using walkingState == WalkingState.STATE
     * @param walkingState enum WalkingState
     * @return true if current walkingState equals the param walkingState
     */
    public boolean isWalkingState(WalkingState walkingState){
        if (this.walkingState.equals(walkingState)) return true;
        return false;
    }

    /**
     * Helper method instead of using direction == Direction.DIRECTION
     * @param direction enum Direction
     * @return true if current direction equals the param direction
     */
    public boolean isDirection(Direction direction){
        if (this.direction.equals(direction)) return true;
        return false;
    }

    /**
     * Helper method instead of using jumpState == JumpState.JUMPSTATE
     * @param jumpState enum JumpState
     * @return true if current jumpState equals the param jumpState
     */
    public boolean isJumpState(JumpState jumpState){
        if (this.jumpState.equals(jumpState)) return true;
        return false;
    }

    /**
     * @param delta delta time
     * Move MegaMan to the left direction
     * stateTime is for the animation
     * velocity is the speed of the movement
     * walkState is changing to RUNNING while MegaMan is moving
     * flipX is flipping the animation on the x axis, it depends on the direction
     */
    private void moveLeft(float delta){
        stateTime += delta;
        velocity.x = -MEGAMAN_MOVEMENT_SPEED;
        walkingState = WalkingState.RUNNING;
        direction = Direction.LEFT;
        flipX = false;
    }

    /**
     * @param delta delta time
     * The logic is same as moveLeft(float delta) above
     */
    private void moveRight(float delta){
        stateTime += delta;
        velocity.x = MEGAMAN_MOVEMENT_SPEED;
        walkingState = WalkingState.RUNNING;
        direction = Direction.RIGHT;
        flipX = true;
    }

}
