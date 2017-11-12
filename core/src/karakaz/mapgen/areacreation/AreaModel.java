package karakaz.mapgen.areacreation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.badlogic.gdx.math.Vector2;
import com.sun.scenario.effect.Merge;

import karakaz.mapgen.voronoicreation.Cell;
import karakaz.mapgen.voronoicreation.CellCorner;

public class AreaModel implements Comparable<AreaModel> {
	
	private ArrayList<Cell> cells;
	private double chanceOfBecomingLand;
	
	public AreaModel(Cell cell){
		cells = new ArrayList<Cell>();
		cells.add(cell);
		
		chanceOfBecomingLand = cell.isBrinkCell() ? 0 : 1;
	}
	
	public Collection<Cell> getCells(){
		return cells;
	}
	
	public void calculateChanceOfBecomingLand() {
		chanceOfBecomingLand *= getModelNeighboursStream().mapToDouble(AreaModel::getChanceOfBecomingLand).average().orElse(0.5);
	}
	
	public double getChanceOfBecomingLand() {
		return chanceOfBecomingLand;
	}
	
	public double getCurrentModelValue(){
		return (1 - chanceOfBecomingLand) * cells.stream().mapToDouble(Cell::getPolygonArea).sum();
	}
	
	@Override
	public int compareTo(AreaModel other) {
		return getCompareValue() - other.getCompareValue();
	}
	
	public int getCompareValue(){
		double neighbourModelValueSum = getModelNeighboursStream().mapToDouble(AreaModel::getCurrentModelValue).sum();
		return (int) (getCurrentModelValue() + 0.5 * neighbourModelValueSum);
	}
	
	private Stream<AreaModel> getModelNeighboursStream(){
		ArrayList<Cell> cellNeighbours = new ArrayList<Cell>();
		
		for(Cell cell : cells){
			Predicate<Cell> neighbourFilter = neighbour -> cell.getContinent().equals(neighbour.getContinent()) && !cells.contains(neighbour);
			cellNeighbours.addAll(cell.getNeighbours().stream().filter(neighbourFilter).collect(Collectors.toList()));
		}
		return cellNeighbours.stream().map(Cell::getAreaModel);
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof AreaModel && cells.containsAll(((AreaModel)o).getCells());
	}
	
	public void mergeWithBestNeighbour(){
		if(chanceOfBecomingLand <= 0) return;
		
		List<AreaModel> valueSortedNeighbours = getModelNeighboursStream().filter(neighbour -> neighbour.getChanceOfBecomingLand() > 0).sorted().collect(Collectors.toList());
		
		AreaModel king = null;
		float valueToBeat = 1000;
		
		for(AreaModel model : valueSortedNeighbours){
			int compareValue = model.getCompareValue();
			float relationValue = compareValue * centerToCornersAverage(model);
			System.out.println(relationValue);
			if(relationValue < valueToBeat){
				king = model;
				valueToBeat = relationValue;
			}
		}
		
		mergeWithModel(king);
	}

	private void mergeWithModel(AreaModel other) {
		cells.addAll(other.getCells());
		chanceOfBecomingLand = chanceOfBecomingLand + other.getChanceOfBecomingLand() / 2;
		other.getCells().forEach(cell -> cell.setAreaModel(this));
	}

	private float centerToCornersAverage(AreaModel areaModelNeighbour) {
		Vector2 centerOfArea = new Vector2();
		cells.forEach(cell -> centerOfArea.add(cell.getCenter()));
		centerOfArea.scl(1f / cells.size());
		
		HashSet<CellCorner> cellCorners = new HashSet<CellCorner>();
		for(Cell cell : areaModelNeighbour.getCells()){
			for(CellCorner corner : cell.getCorners()){
				cellCorners.add(corner);
			}
		}
		
		return (float)cellCorners.stream().mapToDouble(corner -> centerOfArea.dst(corner.getPosition())).average().orElse(1000000);
	}
	
}
