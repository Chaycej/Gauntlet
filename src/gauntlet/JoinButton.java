package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class JoinButton extends Entity {
	
	public JoinButton(final float x, final float y) {
		super(x, y);
		 
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.JOIN_GAME_RSC));
	}
}
