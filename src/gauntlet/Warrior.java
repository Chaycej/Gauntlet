package gauntlet;

import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Warrior extends Entity {
	public Vector velocity;
	private int countdown;
	int notPossible = 0;
	public int guardGotHit =-1;
	
	public Warrior(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.warriorS));
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
	
	public void checkNorth(StateBasedGame game) {
		Gauntlet gg = (Gauntlet)game;
		removeImage(ResourceManager.getImage(Gauntlet.warriorS));
		removeImage(ResourceManager.getImage(Gauntlet.warriorE));
		removeImage(ResourceManager.getImage(Gauntlet.warriorW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.warriorN));
		int col = getColumn();
		int row = getRow();
		if (gg.map[row-1][col]!=1) {
			gg.warrior.setVelocity(new Vector(0, -0.1f));
		} else {
			gg.warrior.setVelocity(new Vector(0, 0f));
		}
	}
	
	public void checkSouth(StateBasedGame game) {
		Gauntlet gg = (Gauntlet)game;
		removeImage(ResourceManager.getImage(Gauntlet.warriorN));
		removeImage(ResourceManager.getImage(Gauntlet.warriorE));
		removeImage(ResourceManager.getImage(Gauntlet.warriorW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.warriorS));
		int col = getColumn();
		int row = getRow();
		if (gg.map[row+1][col]!=1) {
			gg.warrior.setVelocity(new Vector(0, 0.1f));
		} else {
			gg.warrior.setVelocity(new Vector(0, 0f));
		}
	}
	
	public void checkEast(StateBasedGame game) {
		Gauntlet gg = (Gauntlet)game;
		removeImage(ResourceManager.getImage(Gauntlet.warriorN));
		removeImage(ResourceManager.getImage(Gauntlet.warriorS));
		removeImage(ResourceManager.getImage(Gauntlet.warriorW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.warriorE));
		int col = getColumn();
		int row = getRow();
		if (gg.map[row][col+1]!=1) {
			gg.warrior.setVelocity(new Vector(0.1f, 0));
		} else {
			gg.warrior.setVelocity(new Vector(0, 0f));
		}
	}
	
	public void checkWest(StateBasedGame game) {
		Gauntlet gg = (Gauntlet)game;
		removeImage(ResourceManager.getImage(Gauntlet.warriorN));
		removeImage(ResourceManager.getImage(Gauntlet.warriorS));
		removeImage(ResourceManager.getImage(Gauntlet.warriorE));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.warriorW));
		int col = getColumn();
		int row = getRow();
		if (gg.map[row][col+1]!=1) {
			gg.warrior.setVelocity(new Vector(-0.1f, 0));
		} else {
			gg.warrior.setVelocity(new Vector(0, 0f));
		}
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}