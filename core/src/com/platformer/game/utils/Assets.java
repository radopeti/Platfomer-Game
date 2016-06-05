package com.platformer.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import static com.platformer.game.utils.Constants.*;

/**
 * Created by radopeti on 2016. 05. 17..
 * Singleton class for assets
 */
public class Assets implements Disposable, AssetErrorListener{

    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    public MegaManAssets megaManAssets;

    private AssetManager assetManager;
    
    private Assets(){
    }

    public void init(AssetManager assetManager){
        this.assetManager = assetManager;
        assetManager.load(MEGAMAN_ATLAS, TextureAtlas.class);
        assetManager.finishLoading();
        TextureAtlas megaManAtlas = assetManager.get(MEGAMAN_ATLAS);
        megaManAssets = new MegaManAssets(megaManAtlas);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.log(TAG, "Can't load " + asset + " because " + throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public class MegaManAssets{
        public final TextureRegion jumpingRegion;
        public final TextureRegion fallingRegion;
        public final TextureRegion climbOnTop;
        public final TextureRegion bullet;
        public final TextureRegion standAndShoot;
        public final TextureRegion jumpOrFallShoot;

        public final Array<TextureRegion> runningFrames;
        public final Animation runAnimation;
        public final Array<TextureRegion> staindingFrames;
        public final Animation standingAnimation;
        public final Array<TextureRegion> climbingFrames;
        public final Animation climbingAnimation;
        public final Array<TextureRegion> shootAndRunFrames;
        public final Animation shootAndRunAnimation;
        public final Array<TextureRegion> climbAndShootFrames;
        public final Animation climbAndShootAnimation;
        public final float climbingWidthCorrection;
        public final float standShootWidthCorrection;
        public final float fallShootWidthCorrection;
        public final float jumpShootWidthCorrection;

        public MegaManAssets(TextureAtlas atlas){
            jumpingRegion = atlas.findRegion("jumping");
            fallingRegion = atlas.findRegion("falling");
            climbOnTop = atlas.findRegion("climbing-top");
            bullet = atlas.findRegion("bullet");
            standAndShoot = atlas.findRegion("stand-shooting");
            jumpOrFallShoot = atlas.findRegion("jump-fall-shoot");

            climbingWidthCorrection = atlas.findRegion("climb-shoot-0").originalWidth -
                    atlas.findRegion("climbing-0").originalWidth;
            standShootWidthCorrection = atlas.findRegion("stand-shooting").originalWidth -
                    atlas.findRegion("standing-0").originalWidth;
            fallShootWidthCorrection = atlas.findRegion("jump-fall-shoot").originalWidth -
                    atlas.findRegion("falling").originalWidth;
            jumpShootWidthCorrection = atlas.findRegion("jump-fall-shoot").originalWidth -
                    atlas.findRegion("jumping").originalWidth;


            runningFrames = new Array<TextureRegion>();
            runningFrames.add(atlas.findRegion("running-0"));
            runningFrames.add(atlas.findRegion("running-1"));
            runningFrames.add(atlas.findRegion("running-2"));
            runningFrames.add(atlas.findRegion("running-3"));
            runningFrames.add(atlas.findRegion("running-4"));
            runningFrames.add(atlas.findRegion("running-5"));
            runAnimation = new Animation(MEGAMAN_WALK_ANIMATION_FRAME_TIME, runningFrames, Animation.PlayMode.LOOP);

            staindingFrames = new Array<TextureRegion>();
            staindingFrames.add(atlas.findRegion("standing-0"));
            staindingFrames.add(atlas.findRegion("standing-1"));
            staindingFrames.add(atlas.findRegion("standing-2"));
            standingAnimation = new Animation(MEGAMAN_STANDING_ANIMATION_FRAME_TIME, staindingFrames, Animation.PlayMode.LOOP);

            climbingFrames = new Array<TextureRegion>();
            climbingFrames.add(atlas.findRegion("climbing-0"));
            climbingFrames.add(atlas.findRegion("climbing-1"));
            climbingFrames.add(atlas.findRegion("climbing-2"));
            climbingFrames.add(atlas.findRegion("climbing-3"));
            climbingAnimation = new Animation(MEGAMAN_CLIMBING_ANIMATION_FRAME_TIME, climbingFrames, Animation.PlayMode.LOOP);

            shootAndRunFrames = new Array<TextureRegion>();
            shootAndRunFrames.add(atlas.findRegion("run-shoot-0"));
            shootAndRunFrames.add(atlas.findRegion("run-shoot-1"));
            shootAndRunFrames.add(atlas.findRegion("run-shoot-2"));
            shootAndRunFrames.add(atlas.findRegion("run-shoot-3"));
            shootAndRunFrames.add(atlas.findRegion("run-shoot-4"));
            shootAndRunFrames.add(atlas.findRegion("run-shoot-5"));
            shootAndRunAnimation = new Animation(MEGAMAN_CLIMBING_ANIMATION_FRAME_TIME, shootAndRunFrames, Animation.PlayMode.LOOP);

            climbAndShootFrames = new Array<TextureRegion>();
            climbAndShootFrames.add(atlas.findRegion("climb-shoot-0"));
            climbAndShootFrames.add(atlas.findRegion("climb-shoot-1"));
            climbAndShootAnimation = new Animation(MEGAMAN_CLIMBING_ANIMATION_FRAME_TIME, climbAndShootFrames, Animation.PlayMode.LOOP);
        }
    }
}
