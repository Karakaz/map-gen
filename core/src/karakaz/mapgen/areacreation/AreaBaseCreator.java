package karakaz.mapgen.areacreation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import karakaz.mapgen.Map;
import karakaz.mapgen.continentcreation.Continent;
import karakaz.mapgen.voronoicreation.Cell;
import karakaz.mapgen.voronoicreation.CellEdge;
import karakaz.mapgen.voronoicreation.VoronoiDiagram;
import karakaz.mapgen.voronoicreation.VoronoiDiagramCreator;

public class AreaBaseCreator {
	
	private Map map;
	private VoronoiDiagramCreator voronoiCreator;
	
	private VoronoiDiagram voronoiDiagram;
	private Collection<Continent> continents;
	
	private ArrayList<Continent> sortedContinents;
	
	public AreaBaseCreator(Map map, int areasToCreate, Collection<Continent> continents){
		this.map = map;
		
		voronoiCreator = new VoronoiDiagramCreator(map.getWidth(), map.getHeight());
		voronoiDiagram = voronoiCreator.createVoronoiDiagram(areasToCreate * 150, 0, 1024);
		
		this.continents = continents;
		sortedContinents = new ArrayList<Continent>(continents);
		
		setUpContinents();
	}
	

	public Collection<CellEdge> getEdges(){
		return voronoiDiagram.getEdges();
	}
	
	public Collection<Cell> getCells(){
		return voronoiDiagram.getCells();
	}
	
	public Collection<Continent> getContinents(){
		return continents;
	}
	
	private void setUpContinents() {
		connectCellsAndContinents();
		createAreasBasedOnConnectivity();
		convertSmallContinentsToWaterContinents();
		convertExcessContinents();
	}

	private void connectCellsAndContinents(){
		for(Continent continent : continents){
			for(Cell cell : voronoiDiagram.getCells()){
				if(continent.containsPoint(cell.getCenter())){
					continent.addCell(cell);
					cell.setContinent(continent);
				}
			}
		}
	}
	
	private void createAreasBasedOnConnectivity() {
		for(Continent continent : continents){
			if(continent.areAllCellsConnected()){
				new AreaCreator(continent).buildAreas();
			} else{
				new WaterAreaCreator(continent).buildAreas();
			}
		}
	}

	private void convertSmallContinentsToWaterContinents() {
		for(Continent continent : continents){
			int landAreaCount = 0;
			for(Area area : continent.getAreas()){
				if(area instanceof LandArea){
					landAreaCount++;
				}
			}
			if(landAreaCount < 5){
				convertContinentToWater(continent);
			}
		}
	}
	
	private void convertContinentToWater(Continent continent) {
		ArrayList<Area> areas = (ArrayList<Area>)continent.getAreas();
		for (int i = 0; i < areas.size(); i++) {
			if(areas.get(i) instanceof LandArea){
				continent.convertArea(areas.get(i));
				i = -1;
			}
		}
	}

	private void convertExcessContinents(){
		boolean biggest = true;
		int nrLandContinents = countNrLandContinents();
		while(nrLandContinents > map.getNrContinents()){
			Collections.sort(sortedContinents);
			convertContinentToWater((biggest ? findBiggestContinent() : findSmallestContinent()));
			nrLandContinents--;
			biggest = !biggest;
		}
	}


	private Continent findSmallestContinent() {
		for(int i = 0; i < sortedContinents.size(); i++){
			Continent continent = sortedContinents.get(i);
			if(continent.getLandAreas().size() > 0){
				return continent;
			}
		}
		return null;
	}


	private Continent findBiggestContinent() {
		for(int i = sortedContinents.size() - 1; i >= 0; i--){
			Continent continent = sortedContinents.get(i);
			if(continent.getLandAreas().size() > 0){
				return continent;
			}
		}
		return null;
	}


	private int countNrLandContinents(){
		int count = 0;
		for(Continent continent : continents){
			if(continent.getLandAreas().size() > 0){
				count++;
			}
		}
		return count;
	}
}
