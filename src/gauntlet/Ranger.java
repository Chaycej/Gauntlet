package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Ranger extends Entity {
	public Vector velocity;
	private int countdown;
	
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
	
	public int getRow() {
		int row = (int) ((super.getY())/32);	
		return row;
	}
	
	public int getColumn() {
		int col = (int) (super.getX()/32);
		return col;
	}
	
	public void northAnimation() {
		removeImage(ResourceManager.getImage(Gauntlet.rangerS));
		removeImage(ResourceManager.getImage(Gauntlet.rangerE));
		removeImage(ResourceManager.getImage(Gauntlet.rangerW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerN));
	}	
	
	public void southAnimation() {
		removeImage(ResourceManager.getImage(Gauntlet.rangerN));
		removeImage(ResourceManager.getImage(Gauntlet.rangerE));
		removeImage(ResourceManager.getImage(Gauntlet.rangerW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerS));
	}
	
	public void eastAnimation() {
		removeImage(ResourceManager.getImage(Gauntlet.rangerN));
		removeImage(ResourceManager.getImage(Gauntlet.rangerS));
		removeImage(ResourceManager.getImage(Gauntlet.rangerW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerE));
	}
	
	public void westAnimation() {
		removeImage(ResourceManager.getImage(Gauntlet.rangerN));
		removeImage(ResourceManager.getImage(Gauntlet.rangerS));
		removeImage(ResourceManager.getImage(Gauntlet.rangerE));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerW));
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}