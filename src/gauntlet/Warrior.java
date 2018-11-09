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
	
	public void northCheck(StateBasedGame game) {
		Gauntlet bg = (Gauntlet)game;
		int col = CurCol();
		int row = CurRow();
		if (bg.map[col][row] != 1 ) {
			velocity = new Vector(0f, 0f);
			bg.warrior.notPossible=1;
		} else {
			bg.warrior.velocity = new Vector(0f, -.13f);
		}
		bg.warrior.setVelocity(velocity);
		countdown=100;
	}
	
	public void southCheck(StateBasedGame game) {
		Gauntlet bg = (Gauntlet)game;
		int col = CurCol();
		int row = CurRow();
		if (bg.map[col][row] != 1 ) {
			velocity = new Vector(0f, 0f);
		} else {
			
			bg.warrior.velocity = new Vector(0f, .13f);
		}
		bg.warrior.setVelocity(velocity);
		countdown=100;
	}
	
	public void westCheck(StateBasedGame game) {
		Gauntlet bg = (Gauntlet)game;
		int col = CurCol();
		int row = CurRow();
		if (bg.mapM[col][row].west != 1 ) {
			velocity = new Vector(0f, 0f);
			bg.warrior.notPossible=1;
		} else {
			
			bg.warrior.velocity = new Vector(-.13f, 0);
		}
		
		bg.warrior.setVelocity(velocity);
		countdown=100;
	}
	
	public void eastCheck(StateBasedGame game) {
		Gauntlet bg = (Gauntlet)game;
		int col = CurCol();
		int row = CurRow();
		if (bg.map[col][row] != 1 ) {
			velocity = new Vector(0f, 0f);
			bg.warrior.notPossible=1;
		} else {
			 
			bg.warrior.velocity = new Vector(.13f, 0);
		}
		bg.warrior.setVelocity(velocity);
		countdown=100;
	}
	
	public int CurRow() {
		int row = (int) ((super.getY())/32);		//have to accommodate for the 120 units in the top 
		return row;
	}
	
	public int CurCol() {
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
