package gauntlet;

import org.newdawn.slick.state.StateBasedGame;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Ranger extends Entity {
	public Vector velocity;
	private int countdown;
	int notPossible = 0;
	public int guardGotHit =-1;
	
	public Ranger(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerS));
		velocity = new Vector(vx, vy);
		countdown = 0;
	}
	
	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}
