package com.bonicelli.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setResizable(false);
		config.setForegroundFPS(60);
		config.setWindowedMode(1000,1000);
		config.useVsync(true);
		config.setTitle("Peggle 1.0");
		new Lwjgl3Application(new FakePeggle(), config);
	}
}
