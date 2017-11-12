package karakaz.mapgen;

import com.badlogic.gdx.Game;

public class MapGen extends Game {
	
	public static final String VERSION = "0.0.1 Pre-Alpha";
	public static final String LOG = "MapGen";
	
	@Override
	public void create () {
		setScreen(new MapRenderer());
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
