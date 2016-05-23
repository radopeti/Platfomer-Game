package com.platformer.game.utils;

/**
 * Created by radopeti on 2016. 05. 17..
 * Enums
 */
public class Enums {
    public enum WalkingState{
        STANDING,
        RUNNING,
    }

    public enum Direction{
        LEFT,
        RIGHT
    }

    public enum JumpState{
        JUMPING,
        FALLING,
        GROUNDED,
        CLIMBING
    }
}
