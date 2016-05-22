package com.platformer.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.platformer.game.mapcomponents.Ladder;
import com.platformer.game.mapcomponents.Platform;
import com.platformer.game.utils.Assets;
import com.platformer.game.utils.Enums.Direction;
import com.platformer.game.utils.Enums.JumpState;
import com.platformer.game.utils.Enums.WalkingState;

import static com.platformer.game.utils.Constants.GRAVITY;
import static com.platformer.game.utils.Constants.MEGAMAN_CLIMBING_SPEED;
import static com.platformer.game.utils.Constants.MEGAMAN_CLIMBING_WIDTH;
import static com.platformer.game.utils.Constants.MEGAMAN_HEIGHT;
import static com.platformer.game.utils.Constants.MEGAMAN_JUMP_SPEED;
import static com.platformer.game.utils.Constants.MEGAMAN_JUMP_TIME;
import static com.platformer.game.utils.Constants.MEGAMAN_MOVEMENT_SPEED;
import static com.platformer.game.utils.Constants.MEGAMAN_WIDTH;

/**
 * Created by radopeti on 2016. 05. 17..
 * The main character.
 * He can walk, jump, climb on the map and eliminate
 * enemies by shooting on them.
 */

public class MegaMan {

    private static final String TAG = MegaMan.class.getName();

    private TextureRegion currentRegion;
    private Rectangle hitBox;
    private float stateTime;
    private boolean flipX;
    private boolean ableToClimb;
    private boolean ableToMove;
    private Vector2 position;
    private Vector2 lastPosition;
    private Vector2 velocity;

    private WalkingState walkingState;
    private Direction direction;
    private JumpState jumpState;

    private float jumpStartTime;
    private float jumpTime;

    /**
     * Default constructor
     */
    public MegaMan() {
        this.position = new Vector2();
        this.lastPosition = new Vector2();
        this.velocity = new Vector2();
        this.velocity = Vector2.Zero;
        this.hitBox = new Rectangle();
        walkingState = WalkingState.STANDING;
        direction = Direction.RIGHT;
        jumpState = JumpState.FALLING;
        flipX = true;
        ableToClimb = false;
        ableToMove = true;
    }

    public MegaMan(float x, float y) {
        this();
        this.position.set(x, y);
        this.hitBox.set(position.x, position.y, MEGAMAN_WIDTH, MEGAMAN_HEIGHT);
    }

    public MegaMan(Vector2 position) {
        this();
        this.position.set(position);
    }

    public void update(float delta, Array<Platform> platforms, Array<Ladder> ladders) {

        if (isJumpState(JumpState.CLIMBING)){
            velocity.y = 0;
        }else{
            velocity.y -= GRAVITY;
        }


        if (position.y <= 0) {
            position.y = 0;
            velocity.y = 0;
            jumpState = JumpState.GROUNDED;
        }

        //move key controls
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            moveRight(delta);
        } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            moveLeft(delta);
        } else {
            velocity.x = 0;
            walkingState = WalkingState.STANDING;
        }

        //jump key control
        if (Gdx.input.isKeyPressed(Keys.X)) {
            jump();
        } else if (lastPosition.y > position.y) {
            if (isJumpState(JumpState.GROUNDED) || isJumpState(JumpState.JUMPING)) {
                jumpState = JumpState.FALLING;
            }
        }

        //climb key control
        if (Gdx.input.isKeyPressed(Keys.UP)){
            Ladder ladder = ableToClimb(ladders);
            climbOnLadder(ladder, MEGAMAN_CLIMBING_SPEED);
        }else if(Gdx.input.isKeyPressed(Keys.DOWN)){
            Ladder ladder = ableToClimb(ladders);
            climbOnLadder(ladder, -MEGAMAN_CLIMBING_SPEED);
        }

        //platform collision detection
        checkPlatformCollision(platforms);

        lastPosition.set(position);
        position.mulAdd(velocity, delta);
        updateHitBox();
    }


    /**
     * Draw the current TextureRegion depend on the current state of MegaMan
     *
     * @param batch SpriteBatch
     */
    public void render(SpriteBatch batch) {

        if (isWalkingState(WalkingState.STANDING)) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentRegion = Assets.instance.megaManAssets.standingAnimation.getKeyFrame(stateTime);
        } else if (isWalkingState(WalkingState.RUNNING)) {
            currentRegion = Assets.instance.megaManAssets.runAnimation.getKeyFrame(stateTime);
        }

        if (isJumpState(JumpState.JUMPING)) {
            currentRegion = Assets.instance.megaManAssets.jumpingRegion;
        } else if (isJumpState(JumpState.FALLING)) {
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

    public void debugRenderer(ShapeRenderer renderer) {
        renderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

    /**
     * Helper method instead of using walkingState == WalkingState.STATE
     *
     * @param walkingState enum WalkingState
     * @return true if current walkingState equals the param walkingState
     */
    public boolean isWalkingState(WalkingState walkingState) {
        if (this.walkingState.equals(walkingState)) return true;
        return false;
    }

    /**
     * Helper method instead of using direction == Direction.DIRECTION
     *
     * @param direction enum Direction
     * @return true if current direction equals the param direction
     */
    public boolean isDirection(Direction direction) {
        if (this.direction.equals(direction)) return true;
        return false;
    }

    /**
     * Helper method instead of using jumpState == JumpState.JUMPSTATE
     *
     * @param jumpState enum JumpState
     * @return true if current jumpState equals the param jumpState
     */
    public boolean isJumpState(JumpState jumpState) {
        if (this.jumpState.equals(jumpState)) return true;
        return false;
    }

    private void updateHitBox() {
        hitBox.setPosition(position.x, position.y);
    }

    /**
     * @param delta delta time
     * Move MegaMan to the left directio stateTime is for the animation
     * velocity is the speed of the movement
     * walkState is changing to RUNNING while MegaMan is moving
     * flipX is flipping the animation on the x axis, it depends on the direction
     */
    private void moveLeft(float delta) {
        stateTime += delta;
        walkingState = WalkingState.RUNNING;
        direction = Direction.LEFT;
        flipX = false;
        if (ableToMove) velocity.x = -MEGAMAN_MOVEMENT_SPEED;
    }

    /**
     * @param delta delta time
     *              The logic is same as moveLeft(float delta) above
     */
    private void moveRight(float delta) {
        stateTime += delta;
        walkingState = WalkingState.RUNNING;
        direction = Direction.RIGHT;
        flipX = true;
        if (ableToMove) velocity.x = MEGAMAN_MOVEMENT_SPEED;
    }

    /**
     * Increase MegaMan's vertical velocity until a defined time
     * MegaMan can start to jump only if his state is grounded.
     */
    private void jump() {
        if (isJumpState(JumpState.GROUNDED)) {
            jumpState = JumpState.JUMPING;
            velocity.y += MEGAMAN_JUMP_SPEED;
            jumpStartTime = TimeUtils.nanoTime();
        } else if (isJumpState(JumpState.CLIMBING)){
            velocity.y = -GRAVITY;
            hitBox.setSize(MEGAMAN_WIDTH, MEGAMAN_HEIGHT);
            jumpState = JumpState.FALLING;
            ableToMove = true;
        } else if (isJumpState(JumpState.JUMPING)) {
            //calculate the current jumptime
            jumpTime = (TimeUtils.nanoTime() - jumpStartTime) * MathUtils.nanoToSec;
            if (jumpTime < MEGAMAN_JUMP_TIME) {
                velocity.y += MEGAMAN_JUMP_SPEED;
            }
        }
    }

    /**
     * Platform collision detection
     */
    private void checkPlatformCollision(Array<Platform> platforms){
        for (Platform platform : platforms) {
            Rectangle intersector = new Rectangle();
            if (Intersector.intersectRectangles(hitBox, platform.getRectangle(), intersector)){
                platform.setColor(Color.BLUE);
                if (intersector.getWidth() > intersector.getHeight()){
                    if (position.y <= platform.getTop() &&
                            position.y + hitBox.getHeight() > platform.getTop() &&
                            lastPosition.y > position.y) {
                        velocity.y = 0;
                        position.y = position.y + intersector.getHeight();
                        jumpState = JumpState.GROUNDED;
                    }
                    else if (position.y + hitBox.getHeight() >= platform.getBottom() &&
                            position.y < platform.getBottom() &&
                            isJumpState(JumpState.JUMPING)){
                        velocity.y = 0;
                        position.y = position.y - intersector.getHeight();
                    }
                }
                else if(intersector.getWidth() < intersector.getHeight()){
                    if (position.x + hitBox.getWidth() > platform.getLeft() &&
                            position.x < platform.getLeft() && isDirection(Direction.RIGHT)){
                        velocity.x = 0;
                    }
                    else if (position.x + hitBox.getWidth() > platform.getRight() &&
                            position.x < platform.getRight() &&
                            isDirection(Direction.LEFT)){
                        velocity.x = 0;
                    }
                }
            }else{
                platform.setColor(Color.LIME);
            }
        }
    }

    private Ladder ableToClimb(Array<Ladder> ladders){
        for (Ladder ladder : ladders){
            Rectangle intersector = new Rectangle();
            if (Intersector.intersectRectangles(hitBox, ladder.getRectangle(), intersector)){
                if (intersector.getWidth() > 12){
                    return ladder;
                }
            }
        }
        return null;
    }

    private void climbOnLadder(Ladder ladder, float climbingSpeed){
        if (ladder != null){
            position.x = ladder.getX();
            velocity.y = climbingSpeed;
            ableToMove = false;
            jumpState = JumpState.CLIMBING;
            hitBox.setSize(MEGAMAN_CLIMBING_WIDTH, hitBox.height);
        }
    }
}
