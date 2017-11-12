package karakaz.mapgen.voronoicreation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import karakaz.mapgen.Map;

import com.badlogic.gdx.math.Vector2;

public class CellCorner {
	
	private Vector2 position;
	private ArrayList<CellEdge> edges;
	private ArrayList<Cell> cells;
	private boolean onBrink;
	
	public CellCorner(float x, float y){
		position = new Vector2(x, y);
		
		onBrink = calcIfOnBrink();
		
		edges = new ArrayList<CellEdge>();
		cells = new ArrayList<Cell>();
	}
	
	private boolean calcIfOnBrink(){
		return position.x <= 0 || position.y <= 0 || position.x >= Map.currentMap.getWidth() || position.y >= Map.currentMap.getHeight();
	}
	
	public boolean isOnBrink(){
		return onBrink;
	}
	
	public boolean isOnXBrink(){
		return position.x <= 0 || position.x >= Map.currentMap.getWidth();
	}
	
	public boolean isOnYBrink(){
		return position.y <= 0 || position.y >= Map.currentMap.getHeight();
	}
	
	public float getX(){
		return position.x;
	}
	
	public float getY(){
		return position.y;
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	@Override
	public boolean equals(Object o){
		if(o != null && o instanceof CellCorner){
			CellCorner other = ((CellCorner) o);
			return other.getX() == getX() && other.getY() == getY();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new Random((long) (getY() * 10000000)).nextInt();
	}
		
	public void addCell(Cell cell) {
		if(!cells.contains(cell)){
			cells.add(cell);
		}
	}
	
	public void addEdge(CellEdge edge){
		if(!edges.contains(edge)){
			edges.add(edge);
		}
	}

	public Collection<Cell> getCells() {
		return cells;
	}

	public Collection<CellEdge> getEdges() {
		return edges;
	}
}
