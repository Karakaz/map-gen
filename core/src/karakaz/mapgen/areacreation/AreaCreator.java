package karakaz.mapgen.areacreation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;

import karakaz.mapgen.continentcreation.Continent;
import karakaz.mapgen.voronoicreation.Cell;
import karakaz.mapgen.voronoicreation.CellCorner;

public class AreaCreator {
	
	protected Continent continent;
	
	protected ArrayList<Area> areas;
	private ArrayList<Cell> taken;
	
	protected ArrayList<Cell> sortedCells;
	protected List<AreaModel> sortedAreaModels;
	
	public AreaCreator(Continent continent){
		this.continent = continent;
		
		areas = new ArrayList<Area>();
		taken = new ArrayList<Cell>();
		
		sortedCells = new ArrayList<Cell>(continent.getCells().stream().sorted().collect(Collectors.toList()));
	}

	public void buildAreas(){
		
		createModelAreas();
		
		nextPairingIteration();
		
//		makeBrinkCellsWaterAreas();
//		
//		manageSmallCells();
//		
		createAreasOfRemainingCells();
//		
		continent.registerAreas(areas);
	}

	private void createModelAreas() {
		sortedCells.forEach(cell -> cell.setAreaModel(new AreaModel(cell)));
		updateSortedAreaModels();
	}
	
	private void updateSortedAreaModels(){
		sortedAreaModels = sortedCells.stream().map(Cell::getAreaModel).distinct().sorted().collect(Collectors.toList());
	}
	
	private void nextPairingIteration() {
		sortedAreaModels.forEach(AreaModel::mergeWithBestNeighbour);
	}

//	private void makeBrinkCellsWaterAreas() {
//		sortedCells.stream().filter(Cell::isBrinkCell).forEach(cell -> createAndAddWaterArea(cell));
//	}
	
	private void createAndAddArea(Cell... cells){
		if(shouldBeLandType(cells)){
			createAndAddLandArea(cells);
		} else{
			createAndAddWaterArea(cells);
		}
	}
	
	private void createAndAddLandArea(Cell... cells){
		areas.add(new LandArea(cells));
		taken.addAll(Arrays.asList(cells));
	}

	protected void createAndAddWaterArea(Cell... cells) {
		areas.add(new WaterArea(cells));
		List<Cell> cellList = Arrays.asList(cells);
		taken.addAll(cellList);
		cellList.stream().forEach(Cell::registerAsWaterCell);
	}
	
	private void manageSmallCells() {
		for (int i = 0; i < sortedCells.size() / 2; i++) {
			Cell cell = sortedCells.get(i);
			if(!taken.contains(cell)){
				manageSmallCell(cell);
			}
		}
	}

	private void manageSmallCell(Cell cell) {
		Cell bestNeighbour = findBestNeighbour_OLD(cell);
		if(bestNeighbour != null){
			createAndAddArea(cell, bestNeighbour);
		}
	}

	private float calculatePairValue(Vector2 center, Collection<CellCorner> corners) {
		float sumDistance = 0;
		for(CellCorner corner : corners){
			sumDistance += center.dst(corner.getPosition());
		}
		return sumDistance / corners.size();
	}

	private boolean shouldBeLandType(Cell... cells) {
		int friendlyNeighbours = 0;
		int unfriendlyNeighbours = 0;
		int brinkNeighbours = 0;
		for(Cell cell : cells){
			for(Cell neighbour : cell.getNeighbours()){
				if(cell.getContinent().equals(neighbour.getContinent()) && !neighbour.isWaterCell()){
					friendlyNeighbours++;
				} else{
					unfriendlyNeighbours++;
				}
				if(neighbour.isBrinkCell()){
					brinkNeighbours++;
				}
			}
		}
		
		double landThreshold = (unfriendlyNeighbours + brinkNeighbours * 2) / (double)(friendlyNeighbours * 2 + unfriendlyNeighbours + brinkNeighbours);
		Random r = new Random();
		if(r.nextDouble() < landThreshold && continent.areAllCellsConnected(cells)){
			return false;
		}
		return true;
	}

	private void createAreasOfRemainingCells() {
		sortedCells.stream().filter(cell -> !taken.contains(cell)).forEach(cell -> createAndAddArea(cell));
	}
	
	private Cell findBestNeighbour_OLD(Cell cell) {
//		float valueToBeat = 80 - Math.abs(cell.getPolygon().area()) / 60; //512
		float valueToBeat = 50 - Math.abs(cell.getPolygon().area()) / 50; //1024
//		System.out.println(Math.abs(cell.getPolygon().area()));
		Cell king = null;
		for(Cell neighbour : cell.getNeighbours()){
			if(neighbour.getContinent().equals(continent) && !taken.contains(neighbour)){
				float pairValue = calculatePairValue(cell.getCenter(), neighbour.getCorners());
//				System.out.println(pairValue);
				if(pairValue < valueToBeat){
					king = neighbour;
					valueToBeat = pairValue;
				}
			}
		}
		return king;
	}
}
