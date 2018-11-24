package gauntlet;

import jig.Entity;

public class MapMatrix extends Entity {
	public int traveled;
	
	public MapMatrix(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		traveled = 0;
	}
}