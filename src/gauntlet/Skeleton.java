package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Skeleton extends Entity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int health;
	public Vector velocity;
	private int countdown;
	private double[][] path;
	private double[][] visited; 
	public boolean isDead;
	public int moves;
	int direction;
	int previousTargetCol;
	int previousTargetRow;
	int xPos;
	int yPos;
	
	public Skeleton(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		this.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.skeletonS));
		this.health = 10;
		velocity = new Vector(vx, vy);
		countdown = 0;
		path = new double[Gauntlet.maxRow][Gauntlet.maxColumn];
		visited = new double[Gauntlet.maxRow][Gauntlet.maxColumn];
		isDead = false;
		moves = 0;
		direction = 0;
		previousTargetCol = -1;
		previousTargetRow = -1;
		this.setXPos((int)x);
		this.setYPos((int)y);
	}
	
	/*
	 *  isDead
	 *  
	 *  Returns true if the skeleton has no more health.
	 * 
	 */
	public boolean isDead() {
		return this.health <= 0;
	}
	
	public void kill() {
		this.flushImages();
		this.health = 0;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int points) {
		this.health = points;
	}
	
	public void setVelocity(final Vector v) {
		this.velocity = v;
	}

	public Vector getVelocity() {
		return this.velocity;
	}
	
	synchronized public int getXPos() {
		return this.xPos;
	}
	
	/*
	 *  Cache the skeleton's current x position.
	 */
	synchronized public void setXPos(int x) {
		this.xPos = x;
	}
	
	synchronized public int getYPos() {
		return (int)this.yPos;
	}
	
	/*
	 *  Cache the skeleton's current y position.
	 */
	synchronized public void setYPos(int y) {
		this.yPos = y;
	}
	
	public int getRow() {
		return this.getYPos()/32;	
	}
	
	public int getColumn() {
		return this.getXPos()/32;
	}
	
	public void flushImages() {
		this.removeImage(ResourceManager.getImage(Gauntlet.skeletonN));
		this.removeImage(ResourceManager.getImage(Gauntlet.skeletonS));
		this.removeImage(ResourceManager.getImage(Gauntlet.skeletonE));
		this.removeImage(ResourceManager.getImage(Gauntlet.skeletonW));
	}
	
	public void northAnimation() {
		this.flushImages();
		this.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.skeletonN));
	}	
	
	public void southAnimation() {
		this.flushImages();
		this.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.skeletonS));
	}
	
	public void eastAnimation() {
		this.flushImages();
		this.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.skeletonE));
	}
	
	public void westAnimation() {
		this.flushImages();
		this.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.skeletonW));
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
	synchronized public void buildPath(int destRow, int destCol) {
		int row = getRow();
		int col = getColumn();
		
		if (row < 0 || col < 0 || row >= Gauntlet.maxRow || col >= Gauntlet.maxColumn) {
			return;
		}
		
		//north tile 
		if (row > 0) {
			if (Gauntlet.map[row-1][col] == 1 || this.visited[row-1][col] == 1) {		// Set adjacent walls to poor pathfinding score
				this.path[row-1][col] = this.path[row-1][col]+10000;
			} else {
				this.path[row-1][col] = 10 + getDestinationDistance(row-1, col, destRow, destCol);
			}
		}
		//south tile 
		if (row < Gauntlet.maxRow) {
			// Set adjacent walls to poor pathfinding score
			if (Gauntlet.map[row+1][col] == 1 || this.visited[row+1][col] == 1) {
				this.path[row+1][col] = this.path[row+1][col]+10000;
			} else {
				this.path[row+1][col] = 10 + getDestinationDistance(row+1, col, destRow, destCol);
			}
		}
		//west tile 
		if (col > 0) {
			// Set adjacent walls to poor pathfinding score
			if (Gauntlet.map[row][col-1] == 1 || this.visited[row][col-1] == 1) {
				this.path[row][col-1] = this.path[row][col-1]+10000;
			} else {
				this.path[row][col-1] = 10 + getDestinationDistance(row, col-1, destRow, destCol);
			}
		}
		//east tile 
		if (col < Gauntlet.maxColumn ) {
			// Set adjacent walls to poor pathfinding score
			if (Gauntlet.map[row][col+1] == 1 ||  this.visited[row][col+1]== 1) {
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
		
		if (row < 0 || col < 0 || row >= Gauntlet.maxRow || col > Gauntlet.maxColumn) {
			return;
		}
		
		this.direction = 0;
		double min = 10000;
		
		if (row-1 >= 0) {
			if (this.path[row-1][col] < min) {
				min = this.path[row-1][col];
				this.direction = 1;
			}
		}
		if (col+1 <= Gauntlet.maxColumn ) {
			if (this.path[row][col+1] < min) {
				min = this.path[row][col+1];
				this.direction = 4;
			}
		}
		if (col-1 >= 0) {
			if (this.path[row][col-1] < min) {
				min = this.path[row][col-1];
				this.direction = 2;
			}
		}
		if (row+1 <= Gauntlet.maxRow) {
			if (this.path[row+1][col] < min) {
				min = this.path[row+1][col];
				this.direction = 3;
			}
		}
	}
	
	/*
	 * Moves the ghost along a path to intercept the warrior.
	 */
	synchronized public void moveGhost(Gauntlet gauntlet, int delta) {
		int row = this.getRow();
        int col = this.getColumn();
        
        if (col < 0 || col >= Gauntlet.maxColumn || row < 0 || row >= Gauntlet.maxRow) {
        	return;
        }
        
        int targetCol = -1;
        int targetRow = -1;
        if (previousTargetCol ==-1 || previousTargetRow ==-1 || previousTargetCol==col || previousTargetRow==row) {
            for (int i = 0; i < Gauntlet.maxRow; i++) {
                for (int j = 0; j < Gauntlet.maxColumn; j++) {
                    this.visited[i][j] = 0;
                }
            }
        }
        
        int warriorTargetRow = gauntlet.warrior.getRow();
        int warriorTargetCol = gauntlet.warrior.getColumn();
        int rangerTargetRow = gauntlet.ranger.getRow();
        int rangerTargetCol = gauntlet.ranger.getColumn();
        int diffRowW = Math.abs(row - warriorTargetRow);
        int diffColW = Math.abs(col - warriorTargetCol);
        int diffRowR = Math.abs(row - rangerTargetRow);
        int diffColR = Math.abs(col - rangerTargetCol);
        int totalDiffWarrior = diffRowW+diffColW;
        int totalDiffRanger = diffRowR+diffColR;
        
        if (totalDiffRanger <= totalDiffWarrior) {
            previousTargetCol = rangerTargetCol; 
            previousTargetRow = rangerTargetRow;
            targetCol = rangerTargetCol;
            targetRow =  rangerTargetRow;
        } else {
            previousTargetCol = warriorTargetCol; 
            previousTargetRow = warriorTargetRow;
            targetCol = warriorTargetCol;
            targetRow = warriorTargetRow;
        }
        
        buildPath(targetRow, targetCol);
		getMinPath(row, col);
		this.visited[row][col] = 1;
	
		// Moving left
		if (direction == 2) {
			this.eastAnimation();
			this.setVelocity(new Vector(-0.07f, 0f));
		}
		
		//going right
		if (direction == 4) {
			this.westAnimation();
			this.setVelocity(new Vector(0.07f, 0f));
		}
		
		//going down
		if (direction == 3) {
			this.southAnimation();
			this.setVelocity(new Vector(0f, 0.07f));
		}
		
		//going up
		if (direction == 1) {
			this.northAnimation();
			this.setVelocity(new Vector(0f, -0.07f));
		}
		
		if (row-1 < 0 || row+1 > Gauntlet.maxRow-1 || col-1 < 0 || col+1 > Gauntlet.maxColumn-1) {
			this.setVelocity(new Vector(0f, 0f));
		}
		if (row == gauntlet.warrior.getRow() && col == gauntlet.warrior.getColumn()) {
			this.setVelocity(new Vector(0f, 0f));
		}
		if (row == gauntlet.ranger.getRow() && col == gauntlet.ranger.getColumn()) {
			this.setVelocity(new Vector(0f, 0f));
		}
		
		this.setXPos((int)this.getX());
		this.setYPos((int)this.getY());
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}