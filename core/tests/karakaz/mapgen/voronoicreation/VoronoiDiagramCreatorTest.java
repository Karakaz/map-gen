package karakaz.mapgen.voronoicreation;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

import org.junit.Test;

public class VoronoiDiagramCreatorTest {

	@Test
	public void testCreateVoronoiDiagram() {
		VoronoiDiagramCreator creator = new VoronoiDiagramCreator(1280, 720);
		VoronoiDiagram diagram = creator.createVoronoiDiagram(80, 100, 8);
		
		assertEquals(8, diagram.getCells().size());

		checkNeighbourConnectivity(diagram.getCells());
		checkCellConnectivity(diagram);
		checkEdgeConnectivity(diagram);
	}
	
	private void checkNeighbourConnectivity(Collection<Cell> cells){
		
		HashMap<Cell, Integer> neighbourLinks = new HashMap<Cell, Integer>();
		
		for(Cell cell : cells){
			for(Cell neighbour : cell.getNeighbours()){
				neighbourLinks.put(neighbour, 1 + (neighbourLinks.containsKey(neighbour) ? neighbourLinks.remove(neighbour) : 0));
			}
		}
		
		for(Cell cell : cells){
			assertEquals(cell.getNeighbours().size(), neighbourLinks.get(cell).intValue());
		}
	}
	private void checkCellConnectivity(VoronoiDiagram diagram){
		
		HashMap<CellEdge, Integer> edgeLinks = new HashMap<CellEdge, Integer>();
		HashMap<CellCorner, Integer> cornerLinks = new HashMap<CellCorner, Integer>();
		
		for(Cell cell : diagram.getCells()){
			for(CellEdge edge : cell.getEdges()){
				edgeLinks.put(edge, 1 + (edgeLinks.containsKey(edge) ? edgeLinks.remove(edge) : 0));
			}
			for(CellCorner corner : cell.getCorners()){
				cornerLinks.put(corner, 1 + (cornerLinks.containsKey(corner) ? cornerLinks.remove(corner) : 0));
			}
		}
		for(CellEdge edge : diagram.getEdges()){
			assertEquals(edge.getCells().size(), edgeLinks.get(edge).intValue());
		}
		for(CellCorner corner : diagram.getCorners()){
			assertEquals(corner.getCells().size(), cornerLinks.get(corner).intValue());
		}
	}
	private void checkEdgeConnectivity(VoronoiDiagram diagram){
		
		HashMap<CellCorner, Integer> cornerLinks = new HashMap<CellCorner, Integer>();
		
		for(CellEdge edge : diagram.getEdges()){
			CellCorner corner = edge.getCorner(0);
			cornerLinks.put(corner, 1 + (cornerLinks.containsKey(corner) ? cornerLinks.remove(corner) : 0));
			corner = edge.getCorner(1);
			cornerLinks.put(corner, 1 + (cornerLinks.containsKey(corner) ? cornerLinks.remove(corner) : 0));
		}
		for(CellCorner corner : diagram.getCorners()){
			assertEquals(corner.getEdges().size(), cornerLinks.get(corner).intValue());
		}
	}
}
