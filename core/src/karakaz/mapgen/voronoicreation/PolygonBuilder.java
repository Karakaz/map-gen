package karakaz.mapgen.voronoicreation;

import java.util.ArrayList;
import java.util.Collection;

import karakaz.mapgen.Map;

import sun.security.provider.certpath.Vertex;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class PolygonBuilder {
	
	private ArrayList<Float> vertices;
	
	private Cell cell;
	private Collection<CellEdge> edges;
	private Collection<CellCorner> corners;
	
	private CellEdge startEdge;
	private CellCorner startCorner;
	
	private ArrayList<CellCorner> takenCorners;

	
	public PolygonBuilder(Cell cell, Collection<CellEdge> edges, Collection<CellCorner> corners){
		this.cell = cell;
		this.edges = edges;
		this.corners = corners;
	}
	
	public Polygon buildPolygon(){
		
		vertices = new ArrayList<Float>();
		
		findStartCorner();
		
		addCornersInOrder();
		
		addCornerPointIfNeeded();
		
		return new Polygon(verticesToFloatArray());
	}
	
	private void findStartCorner() {
		startEdge = edges.iterator().next();
		startCorner = startEdge.getCorner(0);
		if(edges.size() < corners.size()){
			setOneEndOfGraph(startEdge, startCorner);
		}
	}
	
	private void setOneEndOfGraph(CellEdge prevEdge, CellCorner prevCorner) {
		for(CellEdge edge : edges){
			if((!edge.equals(prevEdge)) && edge.containsCorner(prevCorner)){
				setOneEndOfGraph(edge, edge.getOtherCorner(prevCorner));
				return;
			}
		}
		startCorner = prevCorner;
		startEdge = prevEdge;
	}

	private void addCornersInOrder() {
		takenCorners = new ArrayList<CellCorner>();

		addCorner(startCorner);
		addRemainingCorners(startEdge, startEdge.getOtherCorner(startCorner));
	}
	
	private void addCorner(CellCorner corner){
		vertices.add(corner.getX());
		vertices.add(corner.getY());
		takenCorners.add(corner);
	}
	
	private void addRemainingCorners(CellEdge prevEdge, CellCorner prevCorner){
		addCorner(prevCorner);
		for(CellEdge edge : edges){
			if(!edge.equals(prevEdge) && edge.containsCorner(prevCorner)){
				CellCorner otherCorner = edge.getOtherCorner(prevCorner);
				if(!otherCorner.equals(startCorner)){
					addRemainingCorners(edge, otherCorner);
				}
				return;
			}
		}
		if(takenCorners.size() < corners.size()){
			CellCorner nearestCorner = findNearestCorner(prevCorner);
			CellEdge nearestEdge = findAppropriateEdge(nearestCorner);
			
			addCorner(nearestCorner);
			addRemainingCorners(nearestEdge, nearestEdge.getOtherCorner(nearestCorner));
		}
	}
	
	private CellCorner findNearestCorner(CellCorner relativeCorner) {
		CellCorner king = null;
		for(CellCorner corner : corners){
			if(!takenCorners.contains(corner)){
				if(king == null || distance(corner, relativeCorner) < distance(king, relativeCorner)){
					king = corner;
				}
			}
		}
		return king;
	}
	
	private float distance(CellCorner a, CellCorner b){
		return a.getPosition().dst(b.getPosition());
	}
	
	private CellEdge findAppropriateEdge(CellCorner relativeCorner){
		for(CellEdge edge : relativeCorner.getEdges()){
			if(edge.getCells().contains(cell)){
				return edge;
			}
		}
		return null;
	}

	private void addCornerPointIfNeeded() {
		CellCorner curCorner = takenCorners.get(takenCorners.size() - 1);
		if(startCorner.isOnBrink() && curCorner != null && curCorner.isOnBrink()){
			if(startCorner.isOnXBrink()){
				if(curCorner.isOnYBrink()){
					vertices.add(startCorner.getX());
					vertices.add(curCorner.getY());
				}
			} else if(startCorner.isOnYBrink()){
				if(curCorner.isOnXBrink()){
					vertices.add(curCorner.getX());
					vertices.add(startCorner.getY());
				}
			}
		}
	}
	
	private float[] verticesToFloatArray() {
		float[] arr = new float[vertices.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = vertices.get(i);
		}
		return arr;
	}
}
