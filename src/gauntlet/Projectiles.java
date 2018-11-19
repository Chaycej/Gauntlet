package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Projectiles extends Entity {
	public Vector velocity;
	private int countdown;
	private float speed = 0.05f;
	
	public Projectiles(final float x, final float y, String direction) {
		super(x, y);
		if (direction == "N") {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowN));
			this.velocity = new Vector(0.0f, -speed);
		}else if (direction == "S") {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowS));
			this.velocity = new Vector(0.0f, speed);
		}else if (direction == "E") {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowE));
			this.velocity = new Vector(speed, 0.0f);
		}else if (direction == "W") {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowW));
			this.velocity = new Vector(-speed, 0.0f);
		}
		countdown = 0;
	}
	
	public int getRow() {
		int row = (int) ((super.getY())/32);	
		return row;
	}
	
	public int getColumn() {
		int col = (int) (super.getX()/32);
		return col;
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}