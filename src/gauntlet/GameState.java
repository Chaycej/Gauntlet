package gauntlet;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.atomic.*;

public class GameState implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	enum Direction {
		UP, DOWN, RIGHT, LEFT, STOP;
	}
	
	public Vector<Projectile> warriorProjectiles;
	private Direction warriorDirection;
	private boolean warriorIsMoving;
	private AtomicInteger warriorX;
	private AtomicInteger warriorY;
	private AtomicInteger warriorHealth;
	
	public Vector<Projectile> rangerProjectiles;
	private Direction rangerDirection;
	private boolean rangerIsMoving;
	private AtomicInteger rangerX;
	private AtomicInteger rangerY;
	private AtomicInteger rangerHealth;
	
	public ArrayList<Skeleton> skeletons;
	public ArrayList<Powerups> potions;
	
	public GameState() {
		warriorProjectiles = new Vector<>();
		this.warriorDirection = Direction.DOWN;
		this.warriorIsMoving = false;
		this.warriorX = new AtomicInteger(200);
		this.warriorY = new AtomicInteger(200);
		this.warriorHealth = new AtomicInteger(100);
		
		rangerProjectiles = new Vector<>();
		this.rangerDirection = Direction.DOWN;
		this.rangerIsMoving = false;
		this.rangerX = new AtomicInteger(280);
		this.rangerY = new AtomicInteger(200);
		this.rangerHealth = new AtomicInteger(100);
		
		skeletons = new ArrayList<>();
		potions = new ArrayList<>();
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
	synchronized public boolean warriorIsMoving() {
		return this.warriorIsMoving;
	}
	
	/*
	 *  Sets the warrior's movement
	 */
	synchronized public void setWarriorMovement(boolean bool) {
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
	
	/*
	 *  Sets the rangers's health
	 */
	synchronized void setRangerHealth(int health) {
		this.rangerHealth.set(health);
	}
	
	/*
	 *  Gets the rangers's health
	 */
	synchronized int getRangerHealth() {
		return this.rangerHealth.intValue();
	}

	/*
	 *  Sets the warrior's health
	 */
	synchronized void setWarriorHealth(int health) {
		this.warriorHealth.set(health);
	}
	
	/*
	 *  Gets the warrior's health
	 */
	synchronized int getWarriorHealth() {
		return this.warriorHealth.intValue();
	}

}
