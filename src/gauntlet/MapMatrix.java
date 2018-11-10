package gauntlet;

import jig.Entity;

public class MapMatrix extends Entity{
	
	public int north = 0;
	public int south = 0;
	public int west = 0;
	public int east = 0;
	
	public MapMatrix(final float x, final float y, final float vx, final float vy) {
		super(x, y);
	}
}