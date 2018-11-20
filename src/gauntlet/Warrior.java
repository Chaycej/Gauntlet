package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Warrior extends Entity {
	private GameState.Direction direction;
	public Vector velocity;
	private int countdown;
	
	public Warrior(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.warriorS));
		this.velocity = new Vector(vx, vy);
		this.direction = GameState.Direction.DOWN;
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
	
	public void updateAnimation() {
		if (this.direction == GameState.Direction.UP) {
			this.northAnimation();
		} else if (this.direction == GameState.Direction.DOWN) {
			this.southAnimation();
		} else if (this.direction == GameState.Direction.LEFT) {
			this.westAnimation();
		} else if (this.direction == GameState.Direction.RIGHT) {
			this.eastAnimation();
		}
	}
	
	public GameState.Direction getDirection() {
		return this.direction;
	}
	
	public void setDireciton(GameState.Direction direction) {
		this.direction = direction;
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}