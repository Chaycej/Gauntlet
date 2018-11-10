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
	
	public int curRow() {
		int row = (int) ((super.getY())/32);	
		return row;
	}
	
	public int curCol() {
		int col = (int) (super.getX()/32);
		return col;
	}
	
	public void checkNorth(StateBasedGame game) {
		Gauntlet gg = (Gauntlet)game;
		removeImage(ResourceManager.getImage(Gauntlet.rangerS));
		removeImage(ResourceManager.getImage(Gauntlet.rangerE));
		removeImage(ResourceManager.getImage(Gauntlet.rangerW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerN));
		int col = curCol();
		int row = curRow();
		if (gg.map[row-1][col]!=1) {
			gg.ranger.setVelocity(new Vector(0, -0.1f));
		} else {
			gg.ranger.setVelocity(new Vector(0, 0f));
		}
	}	
	
	public void checkSouth(StateBasedGame game) {
		Gauntlet gg = (Gauntlet)game;
		removeImage(ResourceManager.getImage(Gauntlet.rangerN));
		removeImage(ResourceManager.getImage(Gauntlet.rangerE));
		removeImage(ResourceManager.getImage(Gauntlet.rangerW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerS));
		int col = curCol();
		int row = curRow();
		if (gg.map[row+1][col]!=1) {
			gg.ranger.setVelocity(new Vector(0, 0.1f));
		} else {
			gg.ranger.setVelocity(new Vector(0, 0f));
		}
	}
	
	public void checkEast(StateBasedGame game) {
		Gauntlet gg = (Gauntlet)game;
		removeImage(ResourceManager.getImage(Gauntlet.rangerN));
		removeImage(ResourceManager.getImage(Gauntlet.rangerS));
		removeImage(ResourceManager.getImage(Gauntlet.rangerW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerE));
		int col = curCol();
		int row = curRow();
		if (gg.map[row][col+1]!=1) {
			gg.ranger.setVelocity(new Vector(0.1f, 0));
		} else {
			gg.ranger.setVelocity(new Vector(0, 0f));
		}
	}
	
	public void checkWest(StateBasedGame game) {
		Gauntlet gg = (Gauntlet)game;
		removeImage(ResourceManager.getImage(Gauntlet.rangerN));
		removeImage(ResourceManager.getImage(Gauntlet.rangerS));
		removeImage(ResourceManager.getImage(Gauntlet.rangerE));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerW));
		int col = curCol();
		int row = curRow();
		if (gg.map[row][col+1]!=1) {
			gg.ranger.setVelocity(new Vector(-0.1f, 0));
		} else {
			gg.ranger.setVelocity(new Vector(0, 0f));
		}
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}