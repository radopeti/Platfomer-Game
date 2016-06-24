package com.platformer.game.utils;

import com.badlogic.gdx.graphics.Color;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by radopeti on 2016. 05. 17..
 * Game constants
 */
public class Constants {
    public static final float WORLD_SIZE = 200;
    public static final float GRAVITY = 10;
    public static final Color BACKGROUND_COLOR = Color.FOREST;

    public static final String TESTMAP_NAME = "maps/testmap.tmx";

    public static final String MEGAMAN_ATLAS = "atlas/megaman.atlas";
    public static final float MEGAMAN_WALK_ANIMATION_FRAME_TIME = 0.1f;
    public static final float MEGAMAN_STANDING_ANIMATION_FRAME_TIME = 0.25f;
    public static final float MEGAMAN_CLIMBING_ANIMATION_FRAME_TIME = 0.1f;
    public static final float MEGAMAN_MOVEMENT_SPEED = 150f;
    public static final float MEGAMAN_JUMP_SPEED = 30f;
    public static final float MEGAMAN_JUMP_TIME = 0.2f;
    public static final float MEGAMAN_WIDTH = 22;
    public static final float MEGAMAN_HEIGHT = 29;
    public static final float MEGAMAN_CLIMBING_SPEED = 50;
    public static final float MEGAMAN_CLIMBING_WIDTH = 16;
    public static final float MEGAMAN_BULLET_SPEED = 250;
    public static final float MEGAMAN_DEF_SHOOTING_HEIGHT = 10;
    public static final float MEGAMAN_OTHER_SHOOTING_HEIGHT = 16;
    public static final float MEGAMAN_SHOOTING_DELAY = 0.3f;
    public static final float MEGAMAN_CLIMBING_WIDTH_CORRECTION = Assets.instance.megaManAssets.climbingWidthCorrection;
    public static final float MEGAMAN_STANDING_WIDTH_CORRECTION = Assets.instance.megaManAssets.standShootWidthCorrection;
    public static final float MEGAMAN_FALL_WIDTH_CORRECTION = Assets.instance.megaManAssets.fallShootWidthCorrection;
    public static final float MEGAMAN_JUMP_WIDTH_CORRECTION = Assets.instance.megaManAssets.jumpShootWidthCorrection;

    public static final float LADDER_INTERSECTION_MIN_WIDTH = 12;

    public static final String MOBILE_CONTROL_ATLAS = "atlas/mobile_controls.atlas";
    public static final int MOVEMENT_BUTTON_WIDTH = 20;
    public static final int MOVEMENT_BUTTON_HEIGHT = 20;
    public static final int MOVEMENT_BUTTON_OFFSET = 3;
    public static final int BUTTON_SCALE = 1;

    //jump button constants
    public static final float JB_HORIZONTAL_OFFSET = 90;
    public static final float JB_VERTICAL_OFFSET = 5;

    //jump button constants
    public static final float FB_HORIZONTAL_OFFSET = 45;
    public static final float FB_VERTICAL_OFFSET = 15;
}
