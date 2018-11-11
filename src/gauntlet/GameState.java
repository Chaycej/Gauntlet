package gauntlet;

public class GameState {
	
	private int row;
	private int col;
	
	public GameState(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public void moveUp() {
		this.row--;
	}
	
	public void moveDown() {
		this.row++;
	}
	
	public void moveLeft() {
		this.col--;
	}
	
	public void moveRight() {
		this.col++;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.col;
	}
}