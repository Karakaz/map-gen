package karakaz.mapgen.areacreation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import sun.security.provider.certpath.CollectionCertStore;

import com.badlogic.gdx.graphics.Color;

import karakaz.mapgen.continentcreation.Continent;
import karakaz.mapgen.voronoicreation.Cell;

public abstract class Area {
	
	protected ArrayList<Cell> cells;
	protected Continent continent;
	protected Color color;
	
	public Area(Cell... cells){
		
		this.cells = new ArrayList<Cell>(Arrays.asList(cells));
		this.continent = cells[0].getContinent();
		
		color = determineColor();
	}
	
	public abstract Color determineColor();
	
	public Color getColor(){
		return color;
	}
	
	public Collection<Cell> getCells(){
		return cells;
	}
	
	public Continent getContinent(){
		return continent;
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null && o.getClass() == this.getClass() && cells.containsAll(((Area)o).getCells());
	}
	
	public static Area convertArea(Area areaToConvert){
		Cell[] cellArr = areaToConvert.getCells().toArray(new Cell[areaToConvert.getCells().size()]);
		
		if(areaToConvert instanceof WaterArea){
			return new LandArea(cellArr);
		} else if(areaToConvert instanceof LandArea){
			return new WaterArea(cellArr);
		} else{
			throw new RuntimeException("Trying to convert an illegal object");
		}
	}
	
}
