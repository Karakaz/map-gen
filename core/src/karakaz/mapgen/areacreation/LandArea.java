package karakaz.mapgen.areacreation;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;

import karakaz.mapgen.voronoicreation.Cell;

public class LandArea extends Area{

	private ArrayList<LandArea> neighbours;
	
	public LandArea(Cell... cells) {
		super(cells);
	}

	@Override
	public Color determineColor() {
		Color continentColor = continent.getColor();
		Random r = new Random();
		float redAdjustment = 0.2f * r.nextFloat() - 0.1f;
		float greenAdjustment = 0.2f * r.nextFloat() - 0.1f;
		float blueAdjustment = 0.2f * r.nextFloat() - 0.1f;
		return new Color(continentColor).add(redAdjustment, greenAdjustment, blueAdjustment, 0);
	}

}
