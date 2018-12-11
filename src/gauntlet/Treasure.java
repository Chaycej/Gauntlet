package gauntlet;

import jig.Entity;
import jig.ResourceManager;

public class Treasure extends Entity {
	boolean keyUsed;
	
	public Treasure (final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.treasureChest));
		keyUsed = false;
	}
}
