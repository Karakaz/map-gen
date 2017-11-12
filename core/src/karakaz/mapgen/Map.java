package karakaz.mapgen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import karakaz.mapgen.areacreation.AreaBaseCreator;
import karakaz.mapgen.continentcreation.ContinentBaseCreator;
import karakaz.mapgen.continentcreation.UnableToPairAllContinentPartsException;
import karakaz.mapgen.voronoicreation.Cell;
import karakaz.mapgen.voronoicreation.CellCorner;
import karakaz.mapgen.voronoicreation.CellEdge;

import net.mkonrad.cluster.GenLloyd;

import be.humphreys.simplevoronoi.GraphEdge;
import be.humphreys.simplevoronoi.Voronoi;

import com.badlogic.gdx.math.Vector2;

public class Map {
	
	public static Map currentMap;
	
	private int nrContinents;
	private int width, height;
	private float aspectRatio;
	
	private ContinentBaseCreator continentBase;
	private AreaBaseCreator areaCreator;
	
	public Map(int nrContinents, int width, int height) throws UnableToPairAllContinentPartsException{
		currentMap = this;
		this.nrContinents = nrContinents;
		this.width = width;
		this.height = height;
		
		aspectRatio = width / (float)height;
		
		continentBase = new ContinentBaseCreator(this);
		
		areaCreator = new AreaBaseCreator(this, 42, continentBase.getContinents());
	}
	
	public int getNrContinents(){
		return nrContinents;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public float getAspectRatio(){
		return aspectRatio;
	}
	
	public ContinentBaseCreator getContinentBaseCreator(){
		return continentBase;
	}
	
	public AreaBaseCreator getAreaBaseCreator(){
		return areaCreator;
	}
}
