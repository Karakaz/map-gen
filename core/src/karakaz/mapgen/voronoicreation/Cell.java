package karakaz.mapgen.voronoicreation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import karakaz.mapgen.areacreation.AreaModel;
import karakaz.mapgen.continentcreation.Continent;
import be.humphreys.simplevoronoi.Site;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Cell implements Comparable<Cell>{
	
	private int id;
	private Vector2 center;
	
	private ArrayList<CellCorner> corners;
	private ArrayList<CellEdge> edges;
	private ArrayList<Cell> neighbors;
	
	public ArrayList<Cell> noPairs;
	public Cell previousNeighbour;
	private Polygon polygon;
	private Continent continent;
	private boolean brinkCell;
	private boolean waterCell;
	private AreaModel areaModel;
	
	public Cell(Site site){
		id = site.sitenbr;
		center = new Vector2((float)site.coord.x, (float)site.coord.y);
		
		corners = new ArrayList<CellCorner>();
		edges = new ArrayList<CellEdge>();
		neighbors = new ArrayList<Cell>();
		
		noPairs = new ArrayList<Cell>();
	}
	
	public int getId(){
		return id;
	}
	
	public Vector2 getCenter(){
		return center;
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof Cell && ((Cell)o).getId() == id;
	}

	@Override
	public int hashCode() {
		return new Random(id).nextInt();
	}
	
	public void addNeighbor(Cell poly) {
		if(!neighbors.contains(poly)){
			neighbors.add(poly);
		}
	}

	public void addCorner(CellCorner corner) {
		if(!corners.contains(corner)){
			corners.add(corner);
		}
		if(corner.isOnBrink()){
			brinkCell = true;
		}
	}

	public void addEdge(CellEdge edge) {
		if(!edges.contains(edge)){
			edges.add(edge);
		}
	}
	
	public Collection<Cell> getNeighbours(){
		return neighbors;
	}
	
	public Collection<CellEdge> getEdges(){
		return edges;
	}
	
	public Collection<CellCorner> getCorners(){
		return corners;
	}
	
	public Polygon getPolygon(){
		if(polygon == null){
			polygon = new PolygonBuilder(this, edges, corners).buildPolygon();
		}
		return polygon;
	}
	
	public float getPolygonArea(){
		return Math.abs(getPolygon().area());
	}

	public void setContinent(Continent continent) {
		this.continent = continent;
	}
	
	public Continent getContinent() {
		return continent;
	}
	
	public boolean isBrinkCell() {
		return brinkCell;
	}
	
	public boolean isWaterCell(){
		return waterCell || brinkCell;
	}
	
	public void registerAsWaterCell(){
		waterCell = true;
	}
	
	public int compareTo(Cell other) {
        return (int) (Math.abs(getPolygon().area()) - Math.abs(other.getPolygon().area()));
    }
	
	public void setAreaModel(AreaModel areaModel){
		this.areaModel = areaModel;
	}
	
	public AreaModel getAreaModel(){
		return areaModel;
	}
}