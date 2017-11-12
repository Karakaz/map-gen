package karakaz.mapgen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ModifiedShapeRenderer extends ShapeRenderer{
	
	@Override
	public void polygon(float[] vertices, int offset, int count) {
		
		if(getCurrentType() == ShapeType.Line){
			super.polygon(vertices, offset, count);
		} else if(getCurrentType() != ShapeType.Filled){
			throw new GdxRuntimeException("Must call begin(ShapeType.Filled) or begin(ShapeType.Line)");
		}
		
	    if (count < 6){
	    	throw new IllegalArgumentException("Polygons must contain at least 3 points.");
	    }
	    if (count % 2 != 0){
	        throw new IllegalArgumentException("Polygons must have an even number of vertices.");
	    }
	    
	    ImmediateModeRenderer renderer = getRenderer();
	    Color color = getColor();
	    
	    final float firstX = vertices[0];
	    final float firstY = vertices[1];

        for (int i = offset, n = offset + count; i < n; i += 4) {

            final float x1 = vertices[i];
            final float y1 = vertices[i + 1];

            if (i + 2 >= count) {
                break;
            }

            final float x2 = vertices[i + 2];
            final float y2 = vertices[i + 3];

            final float x3;
            final float y3;

            if (i + 4 >= count) {
                x3 = firstX;
                y3 = firstY;
            } else {
                x3 = vertices[i + 4];
                y3 = vertices[i + 5];
            }
            renderer.color(color);
            renderer.vertex(x1, y1, 0);
            renderer.color(color);
            renderer.vertex(x2, y2, 0);
            renderer.color(color);
            renderer.vertex(x3, y3, 0);
        }
    }
}
