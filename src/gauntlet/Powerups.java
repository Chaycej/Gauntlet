package gauntlet;

import jig.Entity;
import jig.ResourceManager;

class Powerups extends Entity implements java.io.Serializable {
	
	public enum PowerupType {
		lower, normal, max, maxPlus, fireRatePlus;
	}
	
	private static final long serialVersionUID = 1L;
	private int countdown;
	private int xPos;
	private int yPos;
	private PowerupType type;
	
	public Powerups(final float x, final float y, PowerupType type) {
		super(x, y);
        
		if (type == PowerupType.lower) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.LowerHealthPotion));
		} else if (type == PowerupType.normal) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.HealthPotion));

		}else if(type == PowerupType.max){
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.HigherHealthPotion));
	    } else if(type == PowerupType.maxPlus){
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.IncreaseHealth));
		}else {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.IncreaseFire));
		}
		
		this.type = type;
		this.xPos = (int)x;
		this.yPos = (int)y;
		
	}
	
	public PowerupType getType() {
		return this.type;
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
	
	public static PowerupType getRandomPowerUp(int random) {
		PowerupType newType;
		if (random <= 50) 
			newType = PowerupType.lower;
		else if (50 < random && random <= 85)
		    newType = PowerupType.normal;
		else
		    newType = PowerupType.max;
		
		return newType;
	}
	
	public void update(final int delta) {
		//translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}