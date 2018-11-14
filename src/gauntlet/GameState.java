package gauntlet;

import java.util.concurrent.atomic.*;

public class GameState {
	
	private String warriorDirection;
	private AtomicInteger warriorX;
	private AtomicInteger warriorY;
	private AtomicInteger rangerX;
	private AtomicInteger rangerY;
	
	public GameState() {
		this.warriorX = new AtomicInteger();
		this.warriorY = new AtomicInteger();
		this.rangerX = new AtomicInteger();
		this.rangerY = new AtomicInteger();
	}
	
	public void setWarriorPosition(int newx, int newy) {
		this.warriorX.set(newx);
		this.warriorY.set(newy);
	}
	
	public void setRangerPosition(int newx, int newy) {
		this.rangerX.set(newx);
		this.rangerY.set(newy);
	}
	
	public void setWarriorDirection(String direction) {
		this.warriorDirection = direction;
	}
	
	public String getWarriorDirection() {
		return this.warriorDirection;
	}
	
	public int getWarriorX() {
		return this.warriorX.intValue();
	}
	
	public int getWarriorY() {
		return this.warriorY.intValue();
	}
	
	public int getWarriorRow() {
		return this.warriorY.intValue()/32;
	}
	
	public int getWarriorColumn() {
		return this.warriorX.intValue()/32;
	}
	
	public int getRangerX() {
		return this.rangerX.intValue();
	}
	
	public int getRangerY() {
		return this.rangerY.intValue();
	}
}