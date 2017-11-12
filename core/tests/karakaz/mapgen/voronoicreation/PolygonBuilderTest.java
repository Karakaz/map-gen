package karakaz.mapgen.voronoicreation;

import static org.junit.Assert.*;

import java.util.ArrayList;

import karakaz.mapgen.voronoicreation.CellCorner;
import karakaz.mapgen.voronoicreation.CellEdge;
import karakaz.mapgen.voronoicreation.PolygonBuilder;

import org.junit.Test;

import com.badlogic.gdx.math.Polygon;

public class PolygonBuilderTest {
	
	private ArrayList<CellEdge> edges;
	private ArrayList<CellCorner> corners;
	
	@Test
	public void testTriangleBuildPolygon() {
		
		setUpEdgesAndCornersForTriangle();
		
		Polygon triangle = new PolygonBuilder(null, edges, corners).buildPolygon();
		
		assertTrue(triangle != null);
		assertTrue(triangle instanceof Polygon);
		
		float[] vertices = triangle.getVertices();
		
		assertTrue(vertices.length == 6);
		
		for (int i = 0; i < vertices.length; i += 2) {
			float x = vertices[i];
			float y = vertices[i + 1];
			
			System.out.println("x: " + x + ", y: " + y);
			
			if(x == 0.5f){
				assertTrue(y == 0f);
			} else if(x == 2f){
				assertTrue(y == 1.5f);
			} else if(x == 1f){
				assertTrue(y == 1.75);
			} else{
				fail("triangle contained an illegal value for x: " + x);
			}
		}
	}

	private void setUpEdgesAndCornersForTriangle() {
		edges = new ArrayList<CellEdge>();
		corners = new ArrayList<CellCorner>();
		
		CellCorner corner1 = new CellCorner(0.5f, 0);
		CellCorner corner2 = new CellCorner(2, 1.5f);
		CellCorner corner3 = new CellCorner(1, 1.75f);
		
		CellEdge edge1 = new CellEdge(corner1, corner2);
		CellEdge edge2 = new CellEdge(corner2, corner3);
		CellEdge edge3 = new CellEdge(corner3, corner1);
		
		corner1.addEdge(edge1);
		corner1.addEdge(edge3);
		
		corner2.addEdge(edge1);
		corner2.addEdge(edge2);
		
		corner3.addEdge(edge2);
		corner3.addEdge(edge3);
		
		corners.add(corner1);
		corners.add(corner2);
		corners.add(corner3);
		
		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);
	}

}
