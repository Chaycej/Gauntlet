package gauntlet;

import jig.Entity;
import jig.ResourceManager;
//import jig.Vector;

class Powerups extends Entity implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private int countdown;
	private int xPos;
	private int yPos;
	private String type;
	
	public Powerups(final float x, final float y, int i) {
		super(x, y);
        
		if (i == 0) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.LowerHealthPotion));
			type = "lower";
		}else if(i==1) {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.HealthPotion));
			type = "normal";
		}else {
			addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.HigherHealthPotion));
		    type = "max";
		}
		
		this.xPos = (int)x;
		this.yPos = (int)y;
		
	}
	
	public String getType() {
		return type;
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
		
	public void update(final int delta) {
		//translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}