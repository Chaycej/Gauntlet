package gauntlet;

public class GameState {
	
	private int x;
	private int y;
	
	public GameState(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void updatePosition(int newx, int newy) {
		this.x = newx;
		this.y = newy;
	}
	
	public int getRow() {
		return this.x/32;
	}
	
	public int getColumn() {
		return this.y/32;
	}

}
