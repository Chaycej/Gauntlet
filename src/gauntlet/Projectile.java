package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Projectile extends Entity implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	public Vector velocity;
	private int countdown;
	private float speed = 0.5f;
	private int xPos;
	private int yPos;
	private GameState.Direction direction;
	
	public Projectile(final float x, final float y, GameState.Direction direction) {
		super(x, y);
		
		if (direction == GameState.Direction.UP) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowN));
			this.velocity = new Vector(0.0f, -speed);
		}else if (direction == GameState.Direction.DOWN) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowS));
			this.velocity = new Vector(0.0f, speed);
		}else if (direction == GameState.Direction.RIGHT) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowE));
			this.velocity = new Vector(speed, 0.0f);
		}else if (direction == GameState.Direction.LEFT) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowW));
			this.velocity = new Vector(-speed, 0.0f);
		}
		
		this.xPos = (int)x;
		this.yPos = (int)y;
		this.direction = direction;
		
		countdown = 0;
	}
	
	public int getRow() {
		int row = (int) ((this.getY())/32);	
		return row;
	}
	
	public int getColumn() {
		int col = (int) (this.getX()/32);
		return col;
	}
	
	public int getXPos() {
		return this.xPos;
	}
	
	synchronized public void setXPos(int newX) {
		this.xPos = newX;
	}
	
	public int getYPos() {
		return this.yPos;
	}
	
	synchronized public void setYPos(int newY) {
		this.yPos = newY;
	}
	
	public GameState.Direction getDirection() {
		return this.direction;
	}
	
	/*
	 *  addImage
	 * 
	 *  Used to mount an image to a projectile if it doesn't have an image.
	 *  This method is only used for when client reads in the new coordinates
	 *  of the server's projectiles.
	 */
	public void addImage() {
		if (this.direction == GameState.Direction.UP) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowN));
		} else if (this.direction == GameState.Direction.DOWN) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowS));
		} else if (this.direction == GameState.Direction.RIGHT) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowE));
		} else if (this.direction == GameState.Direction.LEFT) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.arrowW));
		}
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}