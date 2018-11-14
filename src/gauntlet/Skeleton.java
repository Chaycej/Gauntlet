package gauntlet;

import java.util.ArrayList;
import java.util.Random;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Skeleton extends Entity {
	public Vector velocity;
	private int countdown;
	private double[][] path;
	public boolean isDead;
	public int moves;
	int direction;
	
	public Skeleton(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.skeletonS));
		velocity = new Vector(vx, vy);
		countdown = 0;
		velocity = new Vector(vx, vy);
		countdown = 0;
		path = new double[25][25];
		isDead = false;
		moves = 0;
		direction = 0;
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
			return endCol - startCol;
		}
		if (startCol == endCol) {
			return endRow - startRow;
		}
		return Math.sqrt(Math.pow(endRow - startRow, 2) + Math.pow(endCol - startCol, 2));
	}
	
	/*
	 * Builds a grid of optimal adjacent moves using the A* algorithm
	 * 
	 */
	public void buildPath(Gauntlet gg, int destRow, int destCol) {
		int row = getRow();
		int col = getColumn();
		//north tile 
		if (row > 0) {
			this.path[row-1][col] = 10 + getDestinationDistance(row-1, col, destRow, destCol);
			
			// Set adjacent walls to poor pathfinding score
			if (gg.map[row-1][col] == 1) {
				this.path[row-1][col] += 1000;
			}
		}
		//south tile 
		if (row < gg.maxColumn) {
			this.path[row+1][col] = 10 + getDestinationDistance(row+1, col, destRow, destCol);
			
			// Set adjacent walls to poor pathfinding score
			if (gg.map[row+1][col] == 1) {
				this.path[row+1][col] += 1000;
			}
		}
		//west tile 
		if (col > 0) {
			this.path[row][col-1] = 10 + getDestinationDistance(row, col-1, destRow, destCol);
			
			// Set adjacent walls to poor pathfinding score
			if (gg.map[row][col-1] == 1) {
				this.path[row][col-1] += 1000;
			}
		}
		//east tile 
		if (col < gg.map[0].length) {
			this.path[row][col+1] = 10 + getDestinationDistance(row, col+1, destRow, destCol);
			
			// Set adjacent walls to poor pathfinding score
			if (gg.map[row][col+1] == 1) {
				this.path[row][col+1] += 1000;
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
	public int getMinPath(int row, int col) {
		
		ArrayList<Integer> directions = new ArrayList<>();
		int direction = 3;
		double min = this.path[row+1][col];
		
		if (this.path[row+1][col] < 1000) {
			directions.add(3);
		}
		
		if (this.path[row-1][col] < min) {
			directions.add(1);
			min = this.path[row-1][col];
			direction = 1;
		}
		if (this.path[row][col+1] < min) {
			directions.add(4);
			min = this.path[row][col+1];
			direction = 4;
		}
		if (this.path[row][col-1] < min) {
			directions.add(2);
			min = this.path[row][col-1];
			direction = 2;
		}
		
		Random random = new Random();
		int rand = random.nextInt(2) + 1;
		if (rand < 2) {
			return direction;
		}
		
		return directions.get(random.nextInt(directions.size()));
	}
	
	/*
	 * Returns the an adjacent cell for the ghost to take.
	 * 
	 * 1 - Up
	 * 2 - left
	 * 3 - down
	 * 4 - right
	 */
	public int getMaxPath(int row, int col) {
		ArrayList<Integer> directions = new ArrayList<>();
		int direction = 3;
		double max = this.path[row+1][col];
		
		if (this.path[row+1][col] < 1000) {
			directions.add(3);
		}
		if (this.path[row-1][col] > max && this.path[row-1][col] < 1000) {
			directions.add(1);
			max = this.path[row-1][col];
			direction = 1;
		}
		if (this.path[row][col+1] < max && this.path[row][col+1] < 1000) {
			directions.add(4);
			max = this.path[row][col+1];
			direction = 4;
		}
		if (this.path[row][col-1] < max && this.path[row][col-1] < 1000) {
			directions.add(2);
			max = this.path[row][col-1];
			direction = 2;
		}
		Random random = new Random();
		int rand = random.nextInt(2) + 1;
		if (rand < 2) {
			return direction;
		}
		return directions.get(random.nextInt(directions.size()));
	}

	/*
	 * Moves the ghost along a path to intercept pacman
	 */
	public void moveGhost(Gauntlet gg, int delta) {
		int row = getRow();
		int col = getColumn();
		// characters have stopped or are not moving
		if (this.getVelocity().getX() == 0 && this.getVelocity().getY() == 0) {
			System.out.println("Came in because its not moving");
			buildPath( gg, gg.warrior.getRow(), gg.warrior.getColumn());
			
			direction = this.isDead == true ? getMaxPath(row, col) : getMinPath(row, col);
			System.out.println("direction is  "+ direction);
			if (this.isDead) {
				this.moves += 1;
			}
			//going left
			if (direction == 2) {
				this.setVelocity(new Vector(-0.12f, 0f));
			}
			//going right
			if (direction == 4) {
				this.setVelocity(new Vector(0.12f, 0f));
			}
			//going down
			if (direction == 3) {
				this.setVelocity(new Vector(0f, 0.12f));
			}
			//going up
			if (direction == 1) {
				this.setVelocity(new Vector(0f, -0.12f));
			}
			this.update(delta);
		}
		// Moving down
		if (this.getVelocity().getY() > 0) {
			row = (int)(gg.skeleton.getY())/32;
			if (gg.map[row+1][col] == 1) {
				this.setVelocity(new Vector(0f, 0f));
			}
		}	
		// Moving up
		if (this.getVelocity().getY() < 0) {
			row = (int)(gg.skeleton.getY())/32;
			if (gg.map[row-1][col] == 1) {
				this.setVelocity(new Vector(0f, 0f));
			}
		}	
		// Moving right
		if (this.getVelocity().getX() > 0) {
			col = (int)(gg.skeleton.getX())/32;
			if (gg.map[row][col+1] == 1) {
				this.setVelocity(new Vector(0f, 0f));
			}
		}
		// Moving left
		if (this.getVelocity().getX() < 0) {
			col = (int)(gg.skeleton.getX());
			if (gg.map[row][col-1] == 1) {
				this.setVelocity(new Vector(0f, 0f));
			}
		}
	}
	
	public void turnGhostDead() {
		this.isDead = true;
		this.moves = 0;
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}