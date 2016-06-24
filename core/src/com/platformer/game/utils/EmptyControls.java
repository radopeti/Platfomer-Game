package com.platformer.game.utils;

import com.platformer.game.listeners.MobileControlListener;

/**
 * Created by hátén on 2016. 06. 24..
 */
public class EmptyControls implements MobileControlListener {
    @Override
    public boolean isLeftButtonPressed() {
        return false;
    }

    @Override
    public boolean isRightButtonPressed() {
        return false;
    }

    @Override
    public boolean isUpButtonPressed() {
        return false;
    }

    @Override
    public boolean isDownButtonPressed() {
        return false;
    }

    @Override
    public boolean isJumpButtonPressed() {
        return false;
    }

    @Override
    public boolean isFireButtonPressed() {
        return false;
    }
}
