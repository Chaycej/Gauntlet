package gauntlet;

public class GameState {
	
	private String direction;
	private int x;
	private int y;
	
	public GameState(String direction, int x, int y) {
		this.direction = direction;
		this.x = x;
		this.y = y;
	}
	
	public void updatePosition(int newx, int newy) {
		this.x = newx;
		this.y = newy;
	}
	
	public String getDirection() {
		return this.direction;
	}
	
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getRow() {
		return this.y/32;
	}
	
	public int getColumn() {
		return this.x/32;
	}
}