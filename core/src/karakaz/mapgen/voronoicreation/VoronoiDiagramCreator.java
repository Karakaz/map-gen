package karakaz.mapgen.voronoicreation;

import java.util.List;
import java.util.Random;

import net.mkonrad.cluster.GenLloyd;

import be.humphreys.simplevoronoi.GraphEdge;
import be.humphreys.simplevoronoi.Voronoi;

import com.badlogic.gdx.math.Vector2;

public class VoronoiDiagramCreator {
	
	private int width, height;
	private float reversedAspectRatio;
	
	private int nrBasePoints;
	private int deadZone;
	private int nrCells;
	
	public VoronoiDiagramCreator(int width, int height){
		if(width <= 0 || height <= 0){
			throw new IllegalArgumentException("width and height must be greater than 0");
		}
		this.width = width;
		this.height = height;
		reversedAspectRatio = height / (float)width;
	}
	
	public VoronoiDiagram createVoronoiDiagram(int nrBasePoints, int deadZone, int nrCells){
		
		this.nrBasePoints = nrBasePoints;
		this.deadZone = deadZone;
		this.nrCells = nrCells;
		
		double[][] basePoints = generateRandomPoints();
		double[][] genLloydPoints = genLloyd(basePoints);
		List<GraphEdge> voronoiEdges = generateVoronoiDiagram(genLloydPoints);
		
		return new VoronoiDiagram(voronoiEdges);
	}
	
	private double[][] generateRandomPoints() {
		
		double[][] points = new double[nrBasePoints][2];
		Random r = new Random();
		
		int minX = deadZone;
		int minY = (int) (deadZone * reversedAspectRatio);
		int maxX = width - deadZone;
		int maxY = (int) (height - deadZone * reversedAspectRatio);
				
		for (int i = 0; i < nrBasePoints; i++) {
			points[i][0] = minX + r.nextInt(maxX - minX) + r.nextDouble();
			points[i][1] = minY + r.nextInt(maxY - minY) + r.nextDouble();
		}
		return points;
	}
	
	private double[][] genLloyd(double[][] basePoints){

		GenLloyd genLloyd = new GenLloyd(basePoints);
		genLloyd.calcClusters(nrCells);
		
		return genLloyd.getClusterPoints();
	}
	
	private List<GraphEdge> generateVoronoiDiagram(double[][] basePoints){
		
		double[] xVals = new double[basePoints.length];
		double[] yVals = new double[basePoints.length];
		
		for (int i = 0; i < basePoints.length; i++) {
			xVals[i] = basePoints[i][0];
			yVals[i] = basePoints[i][1];
		}
		
		Voronoi v = new Voronoi(0.00000001);
		
		return v.generateVoronoi(xVals , yVals, 0, width, 0, height);
	}
}
