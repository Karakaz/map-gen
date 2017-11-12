package karakaz.mapgen.continentcreation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import karakaz.mapgen.Map;
import karakaz.mapgen.areacreation.AreaBaseCreator;
import karakaz.mapgen.voronoicreation.Cell;
import karakaz.mapgen.voronoicreation.CellCorner;
import karakaz.mapgen.voronoicreation.CellEdge;
import karakaz.mapgen.voronoicreation.VoronoiDiagram;
import karakaz.mapgen.voronoicreation.VoronoiDiagramCreator;
import net.mkonrad.cluster.GenLloyd;
import be.humphreys.simplevoronoi.GraphEdge;
import be.humphreys.simplevoronoi.Voronoi;

import com.badlogic.gdx.math.Vector2;

public class ContinentBaseCreator {
	
	private Map map;
	
	private VoronoiDiagramCreator voronoiCreator;
	
	public ArrayList<Cell> pairedContinentParts;

	private VoronoiDiagram voronoiDiagram;
	
	private ArrayList<Continent> continents;
	
	public ContinentBaseCreator(Map map) throws UnableToPairAllContinentPartsException{
		this.map = map;
		
		voronoiCreator = new VoronoiDiagramCreator(map.getWidth(), map.getHeight());
		voronoiDiagram = voronoiCreator.createVoronoiDiagram(map.getNrContinents() * 20, 80, 32);
		
		pairedContinentParts = new PairingTree().getPairedList(voronoiDiagram.getCells());
		
		continents = new ArrayList<Continent>();
		int id = 1;
		for (int i = 0; i + 1 < pairedContinentParts.size(); i += 2) {
			continents.add(new Continent(id++, pairedContinentParts.get(i), pairedContinentParts.get(i + 1)));
		}
	}
	
	public Collection<CellEdge> getEdges(){
		return voronoiDiagram.getEdges();
	}
	
	public Collection<Cell> getCells(){
		return voronoiDiagram.getCells();
	}

	public Collection<Continent> getContinents() {
		return continents;
	}
}
