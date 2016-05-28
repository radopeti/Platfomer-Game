package com.platformer.game.desktop;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.platformer.game.PlatformerGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		TexturePacker.process("megaman", "atlas", "megaman.atlas");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new PlatformerGame(), config);
	}
}
