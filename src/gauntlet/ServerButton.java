package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class ServerButton extends Entity {
	
	public ServerButton(final float x, final float y) {
		super(x, y);
		 
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.HOST_GAME_RSC));
	}
}
