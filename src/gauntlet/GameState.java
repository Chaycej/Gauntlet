package gauntlet;

import java.util.concurrent.atomic.*;

public class GameState implements java.io.Serializable {
	
	private String warriorDirection;
	private String rangerDirection;
	private AtomicInteger warriorX;
	private AtomicInteger warriorY;
	private AtomicInteger rangerX;
	private AtomicInteger rangerY;
	
	public GameState() {
		this.warriorX = new AtomicInteger();
		this.warriorY = new AtomicInteger();
		this.warriorDirection = "do";
		this.rangerX = new AtomicInteger();
		this.rangerY = new AtomicInteger();
		this.rangerDirection = "do";
	}
	
	/*
	 *  Sets the warrior's new position
	 */
	synchronized void setWarriorPosition(int newx, int newy) {
		this.warriorX.set(newx);
		this.warriorY.set(newy);
	}
	
	/*
	 *  Sets the ranger's new position
	 */
	synchronized void setRangerPosition(int newx, int newy) {
		this.rangerX.set(newx);
		this.rangerY.set(newy);
	}
	
	/*
	 *  Sets the warrior's new direction
	 */
	synchronized void setWarriorDirection(String direction) {
		this.warriorDirection = direction;
	}
	
	/*
	 *  Returns the warrior's most recent direction
	 */
	synchronized String getWarriorDirection() {
		return this.warriorDirection;
	}
	
	/*
	 *  Sets the ranger's new direction
	 */
	synchronized void setRangerDirection(String direction) {
		this.rangerDirection = direction;
	}
	
	/*
	 *  Returns the ranger's new direction
	 */
	synchronized String getRangerDirection() {
		return this.rangerDirection;
	}
	
	/*
	 *  Gets the warrior's x position
	 */
	synchronized int getWarriorX() {
		return this.warriorX.intValue();
	}
	
	/*
	 * Gets the warrior's y position
	 */
	synchronized int getWarriorY() {
		return this.warriorY.intValue();
	}
	
	/*
	 *  Gets the warrior's current row that it is occupying
	 */
	synchronized int getWarriorRow() {
		return this.warriorY.intValue()/32;
	}
	
	/*
	 *  Gets the warrior's current column that it is occupying
	 */
	synchronized int getWarriorColumn() {
		return this.warriorX.intValue()/32;
	}
	
	/*
	 *  Gets the current ranger's x position
	 */
	synchronized int getRangerX() {
		return this.rangerX.intValue();
	}
	
	/*
	 *  Gets the current ranger's y position
	 */
	synchronized int getRangerY() {
		return this.rangerY.intValue();
	}
}