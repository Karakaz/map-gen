package karakaz.mapgen.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import karakaz.mapgen.MapGen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "MapGen";
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new MapGen(), config);
	}
}
