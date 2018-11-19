package gauntlet;

import java.util.concurrent.atomic.*;

public class GameState implements java.io.Serializable {
	
	enum Direction {
		UP, DOWN, RIGHT, LEFT, STOP;
	}
	
	private Direction warriorDirection;
	private boolean warriorIsMoving;
	private Direction rangerDirection;
	private boolean rangerIsMoving;
	private AtomicInteger warriorX;
	private AtomicInteger warriorY;
	private AtomicInteger rangerX;
	private AtomicInteger rangerY;
	
	public GameState() {
		this.warriorX = new AtomicInteger(200);
		this.warriorY = new AtomicInteger(200);
		this.warriorDirection = Direction.DOWN;
		this.warriorIsMoving = false;
		this.rangerX = new AtomicInteger(280);
		this.rangerY = new AtomicInteger(200);
		this.rangerDirection = Direction.DOWN;
		this.rangerIsMoving = false;
	}
	
	/*
	 *  Sets the warrior's new position.
	 */
	synchronized void setWarriorPosition(int newx, int newy) {
		this.warriorX.set(newx);
		this.warriorY.set(newy);
	}
	
	/*
	 *  Sets the ranger's new position.
	 */
	synchronized void setRangerPosition(int newx, int newy) {
		this.rangerX.set(newx);
		this.rangerY.set(newy);
	}
	
	/*
	 *  Sets the warrior's new direction.
	 */
	synchronized void setWarriorDirection(Direction direction) {
		this.warriorDirection = direction;
	}
	
	/*
	 *  Returns the warrior's most recent direction.
	 */
	synchronized Direction getWarriorDirection() {
		return this.warriorDirection;
	}
	
	/*
	 *  Returns true if the warrior is moving.
	 */
	public boolean warriorIsMoving() {
		return this.warriorIsMoving;
	}
	
	/*
	 *  Sets the warrior's movement
	 */
	public void setWarriorMovement(boolean bool) {
		this.warriorIsMoving = bool;
	}
	
	/*
	 *  Sets the ranger's new direction.
	 */
	synchronized void setRangerDirection(Direction direction) {
		this.rangerDirection = direction;
	}
	
	/*
	 *  Returns the ranger's new direction.
	 */
	synchronized Direction getRangerDirection() {
		return this.rangerDirection;
	}
	
	/*
	 *  Returns true if the ranger is moving.
	 */
	public boolean rangerIsMoving() {
		return this.rangerIsMoving;
	}
	
	/*
	 *  Sets the ranger's movement
	 */
	public void setRangerMovement(boolean bool) {
		this.rangerIsMoving = bool;
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