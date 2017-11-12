package karakaz.mapgen;

import java.util.Iterator;
import java.util.Random;

import javafx.scene.input.KeyCode;

import karakaz.mapgen.areacreation.Area;
import karakaz.mapgen.areacreation.LandArea;
import karakaz.mapgen.continentcreation.Continent;
import karakaz.mapgen.continentcreation.UnableToPairAllContinentPartsException;
import karakaz.mapgen.voronoicreation.Cell;
import karakaz.mapgen.voronoicreation.CellCorner;
import karakaz.mapgen.voronoicreation.CellEdge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ShortArray;

public class MapRenderer implements Screen, InputProcessor {

	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	
	private Map map;
	
	private float newMapCountdown;
	private boolean continentPartEdges;
	private boolean continentPartPairs;
	private boolean areas;
	private boolean cellEdges;
	
	public MapRenderer(){
		camera = new OrthographicCamera();
		shapeRenderer = new ModifiedShapeRenderer();
		
		continentPartEdges = false;
		continentPartPairs = false;
		areas = true;
		cellEdges = true;
		
		newMapCountdown = 0;
	}
	
	@Override
	public void show() {
		camera.setToOrtho(false, 1280, 720);
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		newMapCountdown -= delta;
		if(newMapCountdown <= 0){
			try {
				map = new Map(7, 1280, 720);
			} catch (UnableToPairAllContinentPartsException e) {
				e.printStackTrace();
				Gdx.app.exit();
			}
			newMapCountdown = 60;
		}
		
		if(map != null){
			drawMap();
		}
	}
		
	private void drawMap(){
		if(areas){
			drawPolygons();
		}
		
		shapeRenderer.begin(ShapeType.Line);
		if(cellEdges){
			drawAreaPartEdges();
		}
		if(continentPartEdges){
			drawContinentPartEdges();
		}
		if(continentPartPairs){
			drawContinentPartPairs();
		}
		
		shapeRenderer.end();
	}

	private void drawPolygons() {
		shapeRenderer.begin(ShapeType.Filled);
		
		for(Continent continent : map.getAreaBaseCreator().getContinents()){
			for(Area area : continent.getAreas()){
				shapeRenderer.setColor(area.getColor());
				for(Cell cell : area.getCells()){
					float[] vertices = cell.getPolygon().getVertices();
					short[] a = new EarClippingTriangulator().computeTriangles(vertices).toArray();
					for (int i = 0; i + 2  < a.length; i += 3) {
						shapeRenderer.triangle(vertices[a[i] * 2], vertices[a[i] * 2 + 1], vertices[a[i + 1] * 2], vertices[a[i + 1] * 2 + 1], vertices[a[i + 2] * 2], vertices[a[i + 2] * 2 + 1]);
					}
				}
				if(cellEdges && area.getCells().size() == 2){
					shapeRenderer.setColor(Color.WHITE);
					Iterator<Cell> cellIterator = area.getCells().iterator();
					shapeRenderer.line(cellIterator.next().getCenter(), cellIterator.next().getCenter());
				}
			}
		}
		shapeRenderer.end();
	}
	
	private void drawContinentPartEdges(){
		shapeRenderer.setColor(new Color(Color.RED));
		Gdx.gl.glLineWidth(1.5f);
		for(CellEdge edge : map.getContinentBaseCreator().getEdges()){
			CellCorner corner1 = edge.getCorner(0);
			CellCorner corner2 = edge.getCorner(1);
			shapeRenderer.line(corner1.getX(), corner1.getY(), corner2.getX(), corner2.getY());
		}
	}
	
	private void drawContinentPartPairs(){
		shapeRenderer.setColor(Color.WHITE);
		Gdx.gl.glLineWidth(2f);
		for (int i = 0; i < map.getContinentBaseCreator().pairedContinentParts.size(); i += 2) {
			Cell p1 = map.getContinentBaseCreator().pairedContinentParts.get(i);
			Cell p2 = map.getContinentBaseCreator().pairedContinentParts.get(i + 1);
			shapeRenderer.line(p1.getCenter().x, p1.getCenter().y, p2.getCenter().x, p2.getCenter().y);
		}
	}
	
	private void drawAreaPartEdges(){
		shapeRenderer.setColor(Color.LIGHT_GRAY);
		Gdx.gl.glLineWidth(1f);
		for(CellEdge edge : map.getAreaBaseCreator().getEdges()){
			CellCorner corner1 = edge.getCorner(0);
			CellCorner corner2 = edge.getCorner(1);
			shapeRenderer.line(corner1.getX(), corner1.getY(), corner2.getX(), corner2.getY());
		}
	}
	
	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
		case Keys.N:
			newMapCountdown = 0;
			break;
		case Keys.C:
			continentPartEdges = !continentPartEdges;
			break;
		case Keys.B:
			continentPartPairs = !continentPartPairs;
			break;
		case Keys.E:
			cellEdges = !cellEdges;
			break;
		case Keys.SPACE:
			areas = !areas;
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
