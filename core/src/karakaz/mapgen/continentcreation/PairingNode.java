package karakaz.mapgen.continentcreation;

import java.util.ArrayList;
import java.util.Collections;

import karakaz.mapgen.voronoicreation.Cell;

public class PairingNode {
	
	private Cell cell;
	
	private ArrayList<Cell> remainingCells;
	private int nrRemaining;
	
	public PairingNode(ArrayList<Cell> remainingCells, Cell cell){
		this.remainingCells = new ArrayList<Cell>(remainingCells);
		this.remainingCells.remove(cell);
		this.cell = cell;
		
		nrRemaining = this.remainingCells.size();
	}
	
	public ArrayList<Cell> getRandomPairs(){
		
		if(nrRemaining == 0){
			
			ArrayList<Cell> pairList = new ArrayList<Cell>();
			pairList.add(cell);
			return pairList;
			
		} else if(nrRemaining % 2 == 0){
			
			return attemptedAtListFromCell(remainingCells.get(0));
			
		} else{
		
			ArrayList<Cell> neighbours = new ArrayList<Cell>(cell.getNeighbours());
			Collections.shuffle(neighbours);
			
			for(Cell c : neighbours){
				if(remainingCells.contains(c)){
					ArrayList<Cell> pairList = attemptedAtListFromCell(c);
					if(pairList != null){
						return pairList;
					}
				}
			}
		}
		return null;
	}
	
	private ArrayList<Cell> attemptedAtListFromCell(Cell c){
		ArrayList<Cell> pairList = new PairingNode(remainingCells, c).getRandomPairs();
		if(pairList != null){
			pairList.add(cell);
			return pairList;
		}
		return null;
	}
}
