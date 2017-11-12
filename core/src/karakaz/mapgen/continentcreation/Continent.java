package karakaz.mapgen.continentcreation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import jdk.nashorn.internal.runtime.FindProperty;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import karakaz.mapgen.areacreation.Area;
import karakaz.mapgen.areacreation.LandArea;
import karakaz.mapgen.voronoicreation.Cell;
import karakaz.mapgen.voronoicreation.CellCorner;

public class Continent implements Comparable<Continent>{

	private int id;
	private ArrayList<Cell> parts;
	
	private ArrayList<Cell> cells;
	private ArrayList<Area> areas;
	private Color color;
	
	public Continent(int id, Cell part1, Cell part2) {
		this.id = id;
		
		parts = new ArrayList<Cell>();
		parts.add(part1);
		parts.add(part2);
		
		cells = new ArrayList<Cell>();
		areas = new ArrayList<Area>();
		
		color = new Color(new Random().nextInt());
	}
	
	public Cell getPart(int zeroOrOne){
		return parts.get(zeroOrOne);
	}

	public boolean containsPoint(Vector2 point) {
		return(parts.get(0).getPolygon().contains(point.x, point.y) ||
			   parts.get(1).getPolygon().contains(point.x, point.y));
	}

	public void addCell(Cell cell) {
		if(!cells.contains(cell)){
			cells.add(cell);
		}
	}

	public Collection<Cell> getCells() {
		return cells;
	}

	public Color getColor() {
		return color;
	}
	
	public boolean areAllCellsConnected(){
		return areAllCellsConnected(new Cell[0]);
	}
	
	public boolean areAllCellsConnected(Cell... exceptions){
		ArrayList<Cell> visited = new ArrayList<Cell>();
		for(Cell exceptionCell : exceptions){
			visited.add(exceptionCell);
		}
		Cell spreadFromCell = firstNonBrinkCell(exceptions);
		if(spreadFromCell != null){
			checkConnectivityFromPoint(spreadFromCell, visited);
			int nonBrinkCells = countNonBrinkCells();
			return visited.size() == nonBrinkCells;
		}
		return false;
	}
	
	private Cell firstNonBrinkCell(Cell... exceptions) {
		for(Cell cell : cells){
			if(!cell.isWaterCell() && isNotExceptionCell(cell, exceptions)){
				return cell;
			}
		}
		return null;
	}

	private boolean isNotExceptionCell(Cell cell, Cell... exceptions) {
		for(Cell exceptionCell : exceptions){
			if(cell.equals(exceptionCell)){
				return false;
			}
		}
		return true;
	}

	private void checkConnectivityFromPoint(Cell cell, ArrayList<Cell> visited){
		visited.add(cell);
		for(Cell neighbour : cell.getNeighbours()){
			if(cells.contains(neighbour) && !visited.contains(neighbour) && !neighbour.isWaterCell()){
				checkConnectivityFromPoint(neighbour, visited);
			}
		}
	}

	private int countNonBrinkCells() {
		int count = 0;
		for(Cell cell : cells){
			if(!cell.isWaterCell()){
				count++;
			}
		}
		return count;
	}

	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof Continent && ((Continent)o).getId() == id;
	}

	public int getId() {
		return id;
	}
	
	public void registerAreas(Collection<Area> areas){
		this.areas = new ArrayList<Area>(areas);
	}
	
	public Collection<Area> getAreas(){
		return areas;
	}

	public void convertArea(Area area) {
		if(!areas.remove(area)){
			throw new RuntimeException("Trying to convert area not contained in continent");
		}
		
		areas.add(Area.convertArea(area));
	}

	@Override
	public int compareTo(Continent continent) {
		return getLandAreas().size() - continent.getLandAreas().size();
	}

	public Collection<Area> getLandAreas() {
		ArrayList<Area> landAreas = new ArrayList<Area>();
		for(Area area : areas){
			if(area instanceof LandArea){
				landAreas.add(area);
			}
		}
		return landAreas;
	}
}
