package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

class Ranger extends Entity {
	
	private int health;
	private int maxHealth;
	private float fireRate;
	private GameState.Direction direction;
	public Vector velocity;
	private int countdown;
	
	public Ranger(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerS));
		this.fireRate = 0.1f;
		this.maxHealth = 100;
		this.health = maxHealth;
		this.direction = GameState.Direction.DOWN;
		this.velocity = new Vector(vx, vy);
		this.countdown = 0;
	}
	
	/*
	 *  isDead
	 * 
	 *  Returns true if the ranger has no more health.
	 */
	public boolean isDead() {
		if(this.health <= 0) {
			this.flush();
			return true;
		} else
		    return false;
	}
	/*
	 *  takeHit
	 * 
	 *  Decreases the ranger's health if an enemy successfully attacks.
	 */
	public void takeHit() {
		if (!this.isDead()) {
			this.health -= 1;
		}
	}
	public float getFireRate() {
		return this.fireRate;
	}
	private void increaseFireRate() {
		this.fireRate += 0.1f;
	}
	
	public void potion(Powerups.PowerupType type) {
		if(type == Powerups.PowerupType.lower) {
		    this.health += this.maxHealth * .25;
		} else if (type == Powerups.PowerupType.normal) {
			this.health += this.maxHealth * .50;
		} else if (type == Powerups.PowerupType.max) {
	        this.health = this.maxHealth;
		} else if (type == Powerups.PowerupType.maxPlus) {
			this.maxHealth += 20;
			this.health = this.maxHealth;
		}else if(type == Powerups.PowerupType.fireRatePlus) {
			if (this.getFireRate() <= 0.5f)
				this.increaseFireRate();
		}
		
		if(this.health > this.maxHealth)
			this.health = this.maxHealth;
	}
	
	public void setHealth(int newHealth) {
		this.health = newHealth;
	}
	
	public int getHealth() {
		return this.health;
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
	
	/*
	 *  flush
	 * 
	 *  Removes all images from the ranger entity.
	 */
	private void flush() {
		removeImage(ResourceManager.getImage(Gauntlet.rangerN));
		removeImage(ResourceManager.getImage(Gauntlet.rangerS));
		removeImage(ResourceManager.getImage(Gauntlet.rangerE));
		removeImage(ResourceManager.getImage(Gauntlet.rangerW));
	}
	
	public void northAnimation() {
		this.flush();
		this.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerN));
	}	
	
	public void southAnimation() {
		this.flush();
		this.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerS));
	}
	
	public void eastAnimation() {
		this.flush();
		this.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerE));
	}
	
	public void westAnimation() {
		this.flush();
		this.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.rangerW));
	}
	
	public void updateAnimation(GameState.Direction direction) {
		if (direction == GameState.Direction.UP) {
			this.northAnimation();
		} else if (direction == GameState.Direction.DOWN) {
			this.southAnimation();
		} else if (direction == GameState.Direction.LEFT) {
			this.westAnimation();
		} else if (direction == GameState.Direction.RIGHT) {
			this.eastAnimation();
		}
	}
	
	public GameState.Direction getDirection() {
		return this.direction;
	}
	
	public void setDirection(GameState.Direction direction) {
		this.direction = direction;
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}