package karakaz.mapgen.areacreation;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

import karakaz.mapgen.voronoicreation.Cell;

public class WaterArea extends Area{

	public WaterArea(Cell... cells) {
		super(cells);
	}
	
	@Override
	public Color determineColor() {
		return Color.BLACK;
	}
}
