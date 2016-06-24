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
import com.platformer.game.listeners.BulletListener;
import com.platformer.game.listeners.MobileControlListener;
import com.platformer.game.mapcomponents.Ladder;
import com.platformer.game.mapcomponents.Platform;
import com.platformer.game.utils.Assets;
import com.platformer.game.utils.Enums.*;


import static com.platformer.game.utils.Constants.*;

/**
 * Created by radopeti on 2016. 05. 17..
 * The main character.
 * He can walk, jump, climb on the map and eliminate
 * enemies by shooting on them.
 */

public class MegaMan{

    private static final String TAG = MegaMan.class.getName();

    private TextureRegion currentRegion;
    private Rectangle hitBox;
    private float stateTime;
    private float climbTime;
    private boolean flipX;
    private boolean climbOnTop;
    private boolean ableToMove;
    private Vector2 position;
    private Vector2 lastPosition;
    private Vector2 velocity;

    private WalkingState walkingState;
    private Direction direction;
    private JumpState jumpState;
    private ShootState shootState;

    private float jumpStartTime;
    private float jumpTime;

    private float shootStartTime;
    private float shootDelay;
    private boolean canShoot;
    private float shootingHeight;

    private BulletListener bulletListener;
    private MobileControlListener mobileControlListener;
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
        shootState = ShootState.NOT_SHOOTING;
        flipX = true;
        climbOnTop = false;
        ableToMove = true;
        canShoot = true;
        shootingHeight = MEGAMAN_DEF_SHOOTING_HEIGHT;
    }

    public MegaMan(float x, float y) {
        this();
        this.position.set(x, y);
        this.hitBox.set(position.x, position.y, MEGAMAN_WIDTH, MEGAMAN_HEIGHT);
    }

    public MegaMan(Vector2 position) {
        this();
        this.position.set(position);
        this.hitBox.set(position.x, position.y, MEGAMAN_WIDTH, MEGAMAN_HEIGHT);
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
        if (Gdx.input.isKeyPressed(Keys.RIGHT) || mobileControlListener.isRightButtonPressed()) {
            moveRight(delta);
        } else if (Gdx.input.isKeyPressed(Keys.LEFT)  || mobileControlListener.isLeftButtonPressed()) {
            moveLeft(delta);
        } else {
            velocity.x = 0;
            walkingState = WalkingState.STANDING;
        }


        //jump key control
        if (Gdx.input.isKeyPressed(Keys.X) || mobileControlListener.isJumpButtonPressed()) {
            jump();
        } else if (lastPosition.y > position.y) {
            if (isJumpState(JumpState.GROUNDED) || isJumpState(JumpState.JUMPING)) {
                jumpState = JumpState.FALLING;
            }
        }

        //climb key control
        if (Gdx.input.isKeyPressed(Keys.UP) || mobileControlListener.isUpButtonPressed()){
            Ladder ladder = getCurrentLadder(ladders);
            if (ladder != null){
                if (position.y > ladder.getTop() - 10 && position.y < ladder.getTop() - 1) {
                    jumpState = JumpState.GROUNDED;
                    position.y = ladder.getTop();
                    hitBox.setSize(MEGAMAN_WIDTH, MEGAMAN_HEIGHT);
                    ableToMove = true;
                }else if (position.y < ladder.getTop() - 10){
                    climbOnLadder(ladder, MEGAMAN_CLIMBING_SPEED);
                }
            }
        }else if(Gdx.input.isKeyPressed(Keys.DOWN) || mobileControlListener.isDownButtonPressed()){
            Ladder ladder = getCurrentLadder(ladders);
            if (ladder != null) {
                if (position.y > ladder.getY()){
                    climbOnLadder(ladder, -MEGAMAN_CLIMBING_SPEED);
                }
                if (position.y < ladder.getY() + 5 && position.y > ladder.getY()) {
                    jumpState = JumpState.GROUNDED;
                    position.y = ladder.getY();
                    hitBox.setSize(MEGAMAN_WIDTH, MEGAMAN_HEIGHT);
                    ableToMove = true;
                }else if (position.y <= ladder.getTop() && position.y > ladder.getTop() - 5){
                    position.set(ladder.getX(), ladder.getTop() - 5);
                }
            }
        }

        //shoot key
        if (Gdx.input.isKeyPressed(Keys.C) || mobileControlListener.isFireButtonPressed()){
            shootState = ShootState.SHOOTING;
            if (canShoot){
                shoot();
                canShoot = false;
                shootStartTime = TimeUtils.nanoTime();
            }else{
                shootDelay = (TimeUtils.nanoTime() - shootStartTime) * MathUtils.nanoToSec;
                if (shootDelay > MEGAMAN_SHOOTING_DELAY) canShoot = true;
            }
        }else{
            canShoot = true;
            shootState = ShootState.NOT_SHOOTING;
        }

        //platform collision detection
        checkPlatformCollision(platforms);

        lastPosition.set(position);
        position.mulAdd(velocity, delta);
        updateHitBox();
        setShootingHeight(jumpState);
    }


    /**
     * Draw the current TextureRegion depend on the current state of MegaMan
     * @param batch SpriteBatch
     */
    public void render(SpriteBatch batch) {
        float correction = 0;

        if (isWalkingState(WalkingState.STANDING)) {
            if (isShootState(ShootState.SHOOTING)){
                currentRegion = Assets.instance.megaManAssets.standAndShoot;
                if (isDirection(Direction.LEFT)) correction = MEGAMAN_STANDING_WIDTH_CORRECTION;
            }else {
                stateTime += Gdx.graphics.getDeltaTime();
                currentRegion = Assets.instance.megaManAssets.standingAnimation.getKeyFrame(stateTime);
            }
        } else if (isWalkingState(WalkingState.RUNNING)) {
            if (isShootState(ShootState.SHOOTING)){
                currentRegion = Assets.instance.megaManAssets.shootAndRunAnimation.getKeyFrame(stateTime);
            }else{
                currentRegion = Assets.instance.megaManAssets.runAnimation.getKeyFrame(stateTime);
            }
        }

        if (isJumpState(JumpState.JUMPING)) {
            if (isShootState(ShootState.SHOOTING)){
                currentRegion = Assets.instance.megaManAssets.jumpOrFallShoot;
                if (isDirection(Direction.LEFT)) correction = MEGAMAN_JUMP_WIDTH_CORRECTION;
            }else{
                currentRegion = Assets.instance.megaManAssets.jumpingRegion;
            }
        } else if (isJumpState(JumpState.FALLING)) {
            if (isShootState(ShootState.SHOOTING)){
                currentRegion = Assets.instance.megaManAssets.jumpOrFallShoot;
                if (isDirection(Direction.LEFT)) correction = MEGAMAN_FALL_WIDTH_CORRECTION;
            }else{
                currentRegion = Assets.instance.megaManAssets.fallingRegion;
            }
        } else if (isJumpState(JumpState.CLIMBING) && !climbOnTop){
            if (isShootState(ShootState.SHOOTING)){
                currentRegion = Assets.instance.megaManAssets.climbAndShootAnimation.getKeyFrame(climbTime);
                if (isDirection(Direction.LEFT)) correction = MEGAMAN_CLIMBING_WIDTH_CORRECTION;
            }else{
                currentRegion = Assets.instance.megaManAssets.climbingAnimation.getKeyFrame(climbTime);
            }
        } else if (isJumpState(JumpState.CLIMBING) && climbOnTop){
            currentRegion = Assets.instance.megaManAssets.climbOnTop;
        }

        batch.draw(currentRegion.getTexture(),
                position.x - correction,
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


    /**
     * Renders MM's hitbox for debugging puposes
     * @param renderer
     */
    public void debugRenderer(ShapeRenderer renderer) {
        renderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

    /**
     * Helper method instead of using shootState == ShootState.STATE
     * @param shootState enum
     * @return true if current state equals the param shootState
     */
    public boolean isShootState(ShootState shootState){
        if (this.shootState.equals(shootState)) return true;
        return false;
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

    /**
     * Get the current shooting height, to know where to draw the bullet
     * @return the current shooting height, to know where to draw the bullet
     */
    public float getShootingHeight() {
        return shootingHeight;
    }

    /**
     * Set the actual shooting height, depend on the current state
     * because different textures modify the height of the bullet
     * @param jumpState
     */
    public void setShootingHeight(JumpState jumpState) {
        if (isJumpState(JumpState.CLIMBING) || isJumpState(JumpState.FALLING) || isJumpState(JumpState.JUMPING)){
            shootingHeight = MEGAMAN_OTHER_SHOOTING_HEIGHT;
        }else{
            shootingHeight = MEGAMAN_DEF_SHOOTING_HEIGHT;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    /**
     * Set up the bulletlistener interface for this object
     * @param bulletListener
     */
    public void setBulletListener(BulletListener bulletListener) {
        this.bulletListener = bulletListener;
    }

    /**
     * Call the implemented bulletlistener's createBullet() method
     */
    private void shoot(){
        if (bulletListener != null){
            bulletListener.createBullet();
        }
    }

    /**
     * Update the hitbox's position
     */
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
        Gdx.app.log("mm", "able to move: " + ableToMove);
    }

    /**
     * @param delta delta time
     * The logic is same as moveLeft(float delta) above
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
        //start jump
        if (isJumpState(JumpState.GROUNDED)) {
            jumpState = JumpState.JUMPING;
            velocity.y += MEGAMAN_JUMP_SPEED;
            jumpStartTime = TimeUtils.nanoTime();
        }
        //enable to jump down from the ladder
        else if (isJumpState(JumpState.CLIMBING)){
            velocity.y = -GRAVITY;
            hitBox.setSize(MEGAMAN_WIDTH, MEGAMAN_HEIGHT);
            jumpState = JumpState.FALLING;
            ableToMove = true;
        }
        //continue jump
        else if (isJumpState(JumpState.JUMPING)) {
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

    /**
     * Return a Ladder object if MM's hitbox intersects it and the intersection's width is
     * bigger then LADDER_INTERSECTION_MIN_WIDTH
     * @param ladders Array if Ladder objects
     * @return Ladder object
     */
    private Ladder getCurrentLadder(Array<Ladder> ladders){
        for (Ladder ladder : ladders){
            Rectangle intersector = new Rectangle();
            if (Intersector.intersectRectangles(hitBox, ladder.getRectangle(), intersector)){
                if (intersector.getWidth() > LADDER_INTERSECTION_MIN_WIDTH){
                    return ladder;
                }
            }
        }
        return null;
    }

    /**
     * Sets MM's position to the current ladder, make him able to climb on it with the
     * given given speed and decrease the hitbox size equals to ladder's
     * @param ladder Ladder object
     * @param climbingSpeed MM's vertical speed
     */
    private void climbOnLadder(Ladder ladder, float climbingSpeed){
        if (!isJumpState(JumpState.CLIMBING)){
            hitBox.setSize(MEGAMAN_CLIMBING_WIDTH, hitBox.height);
            position.x = ladder.getX();
            velocity.x = 0;
            velocity.y = climbingSpeed;
            ableToMove = false;
            jumpState = JumpState.CLIMBING;
        }else if (isJumpState(JumpState.CLIMBING)){
            climbTime += Gdx.graphics.getDeltaTime();
            velocity.y = climbingSpeed;
            climbOnTop = position.y > ladder.getTop() - hitBox.getHeight() / 2 && position.y < ladder.getTop() - 1;
        }
    }

    public void setMobileControlListener(MobileControlListener listener){
        mobileControlListener = listener;
    }
}
