package com.platformer.game.listeners;

/**
 * Created by Peter Rado on 2016. 06. 21..
 * The interface specifies the mobile control functions
 */
public interface MobileControlListener {
    boolean isLeftButtonPressed();
    boolean isRightButtonPressed();
    boolean isUpButtonPressed();
    boolean isDownButtonPressed();
    boolean isJumpButtonPressed();
    boolean isFireButtonPressed();
}
