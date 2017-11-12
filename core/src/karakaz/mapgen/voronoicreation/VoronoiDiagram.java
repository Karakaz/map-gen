package karakaz.mapgen.voronoicreation;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;


import be.humphreys.simplevoronoi.GraphEdge;

public class VoronoiDiagram {

	private HashMap<CellCorner, CellCorner> allCorners;
	private HashMap<CellEdge, CellEdge> allEdges;
	private HashMap<Cell, Cell> allCells;

	public VoronoiDiagram(List<GraphEdge> graphEdges) {
		buildAreaParts(graphEdges);
	}
	
	public Collection<CellCorner> getCorners(){
		return allCorners.values();
	}
	
	public Collection<CellEdge> getEdges(){
		return allEdges.values();
	}
	
	public Collection<Cell> getCells(){
		return allCells.values();
	}
	
	private void buildAreaParts(List<GraphEdge> graphEdges) {
		
		allCorners = new HashMap<CellCorner, CellCorner>();
		allEdges = new HashMap<CellEdge, CellEdge>();
		allCells = new HashMap<Cell, Cell>();
		
		CellCorner[] corners = new CellCorner[2];
		CellEdge edge;
		Cell[] cells = new Cell[2];
		
		for(GraphEdge graphEdge : graphEdges){

			if(graphEdge.x1 == graphEdge.x2 && graphEdge.y1 == graphEdge.y2){
				continue; //Have no use for zero length edges
			}
			
			for (int i = 0; i < corners.length; i++) {
				float x = (float)(i == 0 ? graphEdge.x1 : graphEdge.x2);
				float y = (float)(i == 0 ? graphEdge.y1 : graphEdge.y2);
				
				corners[i] = new CellCorner(x, y);
				if(allCorners.containsKey(corners[i])){
					corners[i] = allCorners.get(corners[i]);
				} else{
					allCorners.put(corners[i], corners[i]);
				}
			}
			
			edge = new CellEdge(corners[0], corners[1]);
			allEdges.put(edge, edge);

			for (int i = 0; i < cells.length; i++) {
				cells[i] = new Cell(i == 0 ? graphEdge.site1 : graphEdge.site2);
				
				if(allCells.containsKey(cells[i])){
					cells[i] = allCells.get(cells[i]);
				} else{
					allCells.put(cells[i], cells[i]);
				}
				
			}
			
			corners[0].addCell(cells[0]);
			corners[0].addCell(cells[1]);
			
			corners[1].addCell(cells[0]);
			corners[1].addCell(cells[1]);
			
			edge.addCell(cells[0]);
			edge.addCell(cells[1]);
			
			cells[0].addNeighbor(cells[1]);
			cells[1].addNeighbor(cells[0]);
			
			cells[0].addCorner(corners[0]);
			cells[0].addCorner(corners[1]);
			
			cells[1].addCorner(corners[0]);
			cells[1].addCorner(corners[1]);
			
			cells[0].addEdge(edge);
			cells[1].addEdge(edge);
		}
		
	}
	
}
