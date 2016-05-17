package com.custardgames.sudokil.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.custardgames.sudokil.Core;

public class DesktopLauncher
{
	public static void main(String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = Core.TITLE;
		config.width = Core.WIDTH * Core.SCALE;
		config.height = Core.HEIGHT * Core.SCALE;
		config.backgroundFPS = 0;
		config.foregroundFPS = 0;
		config.vSyncEnabled = Core.VSYNC;
		config.fullscreen = Core.FULLSCREEN;
		new LwjglApplication(new Core(), config);
	}
}
