package com.platformer.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.platformer.game.utils.Assets;
import com.platformer.game.utils.Enums.Direction;
import static com.platformer.game.utils.Constants.MEGAMAN_BULLET_SPEED;

/**
 * Created by hátén on 2016. 05. 28..
 */
public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private Circle collisionCircle;
    private Direction direction;
    private TextureRegion textureRegion = Assets.instance.megaManAssets.bullet;
    private boolean active;
    private final float RADIUS = textureRegion.getRegionWidth() / 2;

    public Bullet(){
        position = new Vector2();
        velocity = new Vector2();
        collisionCircle = new Circle();
    }

    public Bullet(float x, float y, Direction direction){
        this();
        active = true;
        position.set(x, y);
        if (direction == Direction.RIGHT){
            velocity.x = MEGAMAN_BULLET_SPEED;
        }else{
            velocity.x = -MEGAMAN_BULLET_SPEED;
        }
        this.direction = direction;
        collisionCircle.set(x, y, RADIUS);
    }

    public void update(float delta){
        position.mulAdd(velocity, delta);
        collisionCircle.setPosition(position.x + RADIUS, position.y + RADIUS);
    }

    public void render(SpriteBatch batch){
        batch.draw(textureRegion, position.x, position.y);
    }

    public void debugRender(ShapeRenderer renderer){
        renderer.circle(collisionCircle.x, collisionCircle.y, collisionCircle.radius);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getCollisionCircle() {
        return collisionCircle;
    }

    public Direction getDirection() {
        return direction;
    }
}
