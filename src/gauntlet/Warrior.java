package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Warrior extends Entity {
	public Vector velocity;
	private int countdown;
	
	public int prevX;
	public int prevY;
	
	public Warrior(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.warriorS));
		this.velocity = new Vector(vx, vy);
		this.prevX = 0;
		this.prevY = 0;
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
		removeImage(ResourceManager.getImage(Gauntlet.warriorS));
		removeImage(ResourceManager.getImage(Gauntlet.warriorE));
		removeImage(ResourceManager.getImage(Gauntlet.warriorW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.warriorN));
	}
	
	public void southAnimation() {
		removeImage(ResourceManager.getImage(Gauntlet.warriorN));
		removeImage(ResourceManager.getImage(Gauntlet.warriorE));
		removeImage(ResourceManager.getImage(Gauntlet.warriorW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.warriorS));
	}
	
	public void eastAnimation() {
		removeImage(ResourceManager.getImage(Gauntlet.warriorN));
		removeImage(ResourceManager.getImage(Gauntlet.warriorS));
		removeImage(ResourceManager.getImage(Gauntlet.warriorW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.warriorE));
	}
	
	public void westAnimation() {
		removeImage(ResourceManager.getImage(Gauntlet.warriorN));
		removeImage(ResourceManager.getImage(Gauntlet.warriorS));
		removeImage(ResourceManager.getImage(Gauntlet.warriorE));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.warriorW));
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}