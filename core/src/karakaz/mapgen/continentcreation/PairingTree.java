package karakaz.mapgen.continentcreation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;

import karakaz.mapgen.voronoicreation.Cell;

public class PairingTree {
	
	private PairingNode root;
	
	public ArrayList<Cell> getPairedList(Collection<Cell> allCells) throws UnableToPairAllContinentPartsException{
		
		ArrayList<Cell> sortedCellList = sortCellList(allCells);
		
		root = new PairingNode(sortedCellList, sortedCellList.get(0));
		
		ArrayList<Cell> pairList = root.getRandomPairs();
		
		if(pairList == null){
			throw new UnableToPairAllContinentPartsException();
		}
		
		return pairList;
	}
	
	private ArrayList<Cell> sortCellList(Collection<Cell> cells){
		ArrayList<Cell> sortedList = new ArrayList<Cell>(cells);
		
		Collections.sort(sortedList, new Comparator<Cell>(){
			@Override
			public int compare(Cell c1, Cell c2) {
				return c1.getCenter().x >= c2.getCenter().x ? 1 : -1;
			}
		});
		return sortedList;
	}
}
