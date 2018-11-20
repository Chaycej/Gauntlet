package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Skeleton extends Entity implements java.io.Serializable {
	public Vector velocity;
	private int countdown;
	private double[][] path;
	private double[][] visited; 
	public boolean isDead;
	public int moves;
	int direction;
	int previousTargetCol;
	int previousTargetRow;
	
	public Skeleton(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		velocity = new Vector(vx, vy);
		countdown = 0;
		velocity = new Vector(vx, vy);
		countdown = 0;
		path = new double[25][25];
		visited = new double[25][25];
		isDead = false;
		moves = 0;
		direction = 0;
		previousTargetCol = -1;
		previousTargetRow = -1;
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
	
	public void northAnimation() {
		removeImage(ResourceManager.getImage(Gauntlet.skeletonS));
		removeImage(ResourceManager.getImage(Gauntlet.skeletonE));
		removeImage(ResourceManager.getImage(Gauntlet.skeletonW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.skeletonN));
	}	
	
	public void southAnimation() {
		removeImage(ResourceManager.getImage(Gauntlet.skeletonN));
		removeImage(ResourceManager.getImage(Gauntlet.skeletonE));
		removeImage(ResourceManager.getImage(Gauntlet.skeletonW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.skeletonS));
	}
	
	public void eastAnimation() {
		removeImage(ResourceManager.getImage(Gauntlet.skeletonN));
		removeImage(ResourceManager.getImage(Gauntlet.skeletonS));
		removeImage(ResourceManager.getImage(Gauntlet.skeletonW));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.skeletonE));
	}
	
	public void westAnimation() {
		removeImage(ResourceManager.getImage(Gauntlet.skeletonN));
		removeImage(ResourceManager.getImage(Gauntlet.skeletonS));
		removeImage(ResourceManager.getImage(Gauntlet.skeletonE));
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.skeletonW));
	}
	
	/*
	 * Returns the distance between two points in a grid
	 */
	public double getDestinationDistance(int startRow, int startCol, int endRow, int endCol) {
		if (startRow == endRow) {
			return Math.abs(endCol - startCol);
		}
		if (startCol == endCol) {
			return Math.abs(endRow - startRow);
		}
		return Math.sqrt(Math.pow(Math.abs(endRow - startRow), 2) + Math.pow(Math.abs(endCol - startCol), 2));
	}
	
	/*
	 * Builds a grid of optimal adjacent moves using the A* algorithm
	 * 
	 */
	synchronized public void buildPath(Gauntlet gg, int destRow, int destCol) {
		int row = getRow();
		int col = getColumn();
		
		//north tile 
		if (row > 0) {
			if (gg.map[row-1][col] == 1 || this.visited[row-1][col] == 1) {		// Set adjacent walls to poor pathfinding score
				this.path[row-1][col] = this.path[row-1][col]+10000;
			} else {
				
				this.path[row-1][col] = 10 + getDestinationDistance(row-1, col, destRow, destCol);
			}
		}
		//south tile 
		if (row < gg.maxRow) {
			// Set adjacent walls to poor pathfinding score
			if (gg.map[row+1][col] == 1 || this.visited[row+1][col] == 1) {
				this.path[row+1][col] = this.path[row+1][col]+10000;
			} else {
				this.path[row+1][col] = 10 + getDestinationDistance(row+1, col, destRow, destCol);
			}
		}
		//west tile 
		if (col > 0) {
			// Set adjacent walls to poor pathfinding score
			if (gg.map[row][col-1] == 1 || this.visited[row][col-1] == 1) {
				this.path[row][col-1] = this.path[row][col-1]+10000;
			} else {
				this.path[row][col-1] = 10 + getDestinationDistance(row, col-1, destRow, destCol);
			}
		}
		//east tile 
		if (col < gg.maxColumn ) {
			// Set adjacent walls to poor pathfinding score
			if (gg.map[row][col+1] == 1 ||  this.visited[row][col+1]== 1) {
				this.path[row][col+1] = this.path[row][col+1]+10000;
			} else {
				this.path[row][col+1] = 10 + getDestinationDistance(row, col+1, destRow, destCol);
			}
		}
	}
	
	/*
	 * Returns the an adjacent cell for the ghost to take.
	 * 
	 * 1 - Up
	 * 2 - left
	 * 3 - down
	 * 4 - right
	 */
	synchronized public void getMinPath(int row, int col) {
		this.direction = 3;
		double min = this.path[row+1][col];
		if (this.path[row-1][col] < min) {
			min = this.path[row-1][col];
			this.direction = 1;
		}
		if (this.path[row][col+1] < min) {
			min = this.path[row][col+1];
			direction = 4;
		}
		if (this.path[row][col-1] < min) {
			min = this.path[row][col-1];
			direction = 2;
		}
		if (this.direction == 3) {
		}
	}
	
	/*
	 * Moves the ghost along a path to intercept pacman
	 */
	synchronized public void moveGhost(Gauntlet gg, int delta) {
		int row = getRow();
		int col = getColumn();
		
		if (previousTargetCol ==-1 || previousTargetRow ==-1 || previousTargetCol==col || previousTargetRow==row) {
			
			previousTargetCol = gg.warrior.getRow();
			previousTargetRow = gg.warrior.getColumn();
			
			for (int i=0; i<gg.maxRow; i++) {
				for (int j=0; j<gg.maxColumn; j++) {
					this.visited[i][j]=0;
				}
			}
		}
		buildPath( gg, gg.warrior.getRow(), gg.warrior.getColumn());
		getMinPath( row, col);
		this.visited[row][col] = 1;
		
		// Moving left
		if (direction == 2) {
			eastAnimation();
			this.setVelocity(new Vector(-0.12f, 0f));
		}
		//going right
		if (direction == 4) {
			westAnimation();
			this.setVelocity(new Vector(0.12f, 0f));
		}
		//going down
		if (direction == 3) {
			southAnimation();
			this.setVelocity(new Vector(0f, 0.12f));
		}
		//going up
		if (direction == 1) {
			northAnimation();
			this.setVelocity(new Vector(0f, -0.12f));
		}
	
		if (row-1 < 0 || row+1 > gg.maxRow-1 || col-1 < 0 || col+1 > gg.maxColumn-1) {
			this.setVelocity(new Vector(0f, 0f));
		}
		if (row == gg.warrior.getRow() && col == gg.warrior.getColumn()) {
			this.setVelocity(new Vector(0f, 0f));
		}
		this.update(delta);
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}