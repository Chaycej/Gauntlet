package gauntlet;

import jig.Entity;
import jig.ResourceManager;

public class Keys extends Entity {
	boolean keyUsed;
	
	public Keys(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.KeyHDown));
		keyUsed = false;
	}
}