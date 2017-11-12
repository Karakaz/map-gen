package karakaz.mapgen.areacreation;

import java.util.ArrayList;

import karakaz.mapgen.continentcreation.Continent;
import karakaz.mapgen.voronoicreation.Cell;

public class WaterAreaCreator extends AreaCreator{
	
	public WaterAreaCreator(Continent continent){
		super(continent);
	}
	
	@Override
	public void buildAreas(){
		for(Cell cell : sortedCells){
			createAndAddWaterArea(cell);
		}
		continent.registerAreas(areas);
	}
	
}
