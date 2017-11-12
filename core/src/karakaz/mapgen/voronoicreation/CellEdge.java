package karakaz.mapgen.voronoicreation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class CellEdge {
	
	private ArrayList<CellCorner> corners;
	private ArrayList<Cell> cells;
	
	public CellEdge(CellCorner corner1, CellCorner corner2){
		corners = new ArrayList<CellCorner>();
		corners.add(corner1);
		corners.add(corner2);
		
		corner1.addEdge(this);
		corner2.addEdge(this);
		
		cells = new ArrayList<Cell>();
	}
	
	public CellCorner getCorner(int zeroOrOne){
		if(zeroOrOne == 0 || zeroOrOne == 1){
			return corners.get(zeroOrOne);
		} else{
			throw new IllegalArgumentException("Attempted to get an illegal corner: " + zeroOrOne);
		}
	}
	
	public Collection<Cell> getCells(){
		return cells;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof CellEdge){
			CellEdge other = ((CellEdge) o);
			return (other.getCorner(0).equals(getCorner(0)) && other.getCorner(1).equals(getCorner(1))) ||
				   (other.getCorner(0).equals(getCorner(1)) && other.getCorner(1).equals(getCorner(0)));
				   
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new Random((long) (corners.get(0).getX() * 10000000)).nextInt();
	}
	
	public void addCell(Cell cell) {
		if(!cells.contains(cell)){
			cells.add(cell);
		}
	}

	public boolean containsCorner(CellCorner curCorner){
		return corners.contains(curCorner);
	}

	public CellCorner getOtherCorner(CellCorner corner) {
		return corner.equals(getCorner(0)) ? getCorner(1) : getCorner(0);
	}
}
