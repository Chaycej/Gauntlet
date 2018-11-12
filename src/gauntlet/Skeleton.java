package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Skeleton extends Entity {
	public Vector velocity;
	private int countdown;
	
	public Skeleton(final float x, final float y, final float vx, final float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.skeletonS));
		velocity = new Vector(vx, vy);
		countdown = 0;
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
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
		}
	}
}

//class Ghost extends Entity {
//
//	private Vector velocity;
//	private int countdown;
//	private double[][] path;
//	//public boolean leftRoom;
//	public boolean isDead;
//	public int moves;
//	private int previousDirection;
//	//private String color;
//
//	public Ghost(final float x, final float y, final float vx, final float vy, String color) {
//		
//		velocity = new Vector(vx, vy);
//		countdown = 0;
//		path = new double[25][25];
//		leftRoom = false;
//		isDead = false;
//		moves = 0;
//		previousDirection = 0;
//	}
//	
//	/*
//	 * Returns the distance between two points in a grid
//	 */
//	public double getDestinationDistance(int startRow, int startCol, int endRow, int endCol) {
//		if (startRow == endRow) {
//			return endCol - startCol;
//		}
//		
//		if (startCol == endCol) {
//			return endRow - startRow;
//		}
//		return Math.sqrt(Math.pow(endRow - startRow, 2) + Math.pow(endCol - startCol, 2));
//	}
//	
//	/*
//	 * Builds a grid of optimal adjacent moves using the A* algorithm
//	 * 
//	 */
//	public void buildPath(int[][] tiles, int destRow, int destCol) {
//		int row = getRow();
//		int col = getColumn();
//		
//		if (row > 0) {
//			this.path[row-1][col] = 10 + getDestinationDistance(row-1, col, destRow, destCol);
//			
//			// Set adjacent walls to poor pathfinding score
//			if (tiles[row-1][col] == 0) {
//				this.path[row-1][col] += 1000;
//			}
//		}
//		
//		if (row < tiles.length - 1) {
//			this.path[row+1][col] = 10 + getDestinationDistance(row+1, col, destRow, destCol);
//			
//			// Prevent ghosts from entering back into the room.
//			if (row+1 == 8 && col == 9) {
//				this.path[row+1][col] += 1000;
//			}
//			
//			// Set adjacent walls to poor pathfinding score
//			if (tiles[row+1][col] == 0) {
//				this.path[row+1][col] += 1000;
//			}
//		}
//		
//		if (col > 0) {
//			this.path[row][col-1] = 10 + getDestinationDistance(row, col-1, destRow, destCol);
//			
//			// Set adjacent walls to poor pathfinding score
//			if (tiles[row][col-1] == 0) {
//				this.path[row][col-1] += 1000;
//			}
//		}
//		
//		if (col < tiles[0].length) {
//			this.path[row][col+1] = 10 + getDestinationDistance(row, col+1, destRow, destCol);
//			
//			// Set adjacent walls to poor pathfinding score
//			if (tiles[row][col+1] == 0) {
//				this.path[row][col+1] += 1000;
//			}
//		}
//	}
//	
//	/*
//	 * Returns the an adjacent cell for the ghost to take.
//	 * 
//	 * 1 - Up
//	 * 2 - left
//	 * 3 - down
//	 * 4 - right
//	 */
//	public int getMinPath(int row, int col) {
//		
//		ArrayList<Integer> directions = new ArrayList<>();
//		int direction = 3;
//		double min = this.path[row+1][col];
//		
//		if (this.path[row+1][col] < 1000) {
//			directions.add(3);
//		}
//		
//		if (this.path[row-1][col] < min) {
//			directions.add(1);
//			min = this.path[row-1][col];
//			direction = 1;
//		}
//		if (this.path[row][col+1] < min) {
//			directions.add(4);
//			min = this.path[row][col+1];
//			direction = 4;
//		}
//		if (this.path[row][col-1] < min) {
//			directions.add(2);
//			min = this.path[row][col-1];
//			direction = 2;
//		}
//		
//		Random random = new Random();
//		int rand = random.nextInt(2) + 1;
//		if (rand < 2) {
//			return direction;
//		}
//		
//		return directions.get(random.nextInt(directions.size()));
//	}
//	
//	/*
//	 * Returns the an adjacent cell for the ghost to take.
//	 * 
//	 * 1 - Up
//	 * 2 - left
//	 * 3 - down
//	 * 4 - right
//	 */
//	public int getMaxPath(int row, int col) {
//		
//		ArrayList<Integer> directions = new ArrayList<>();
//		int direction = 3;
//		double max = this.path[row+1][col];
//		
//		if (this.path[row+1][col] < 1000) {
//			directions.add(3);
//		}
//		
//		if (this.path[row-1][col] > max && this.path[row-1][col] < 1000) {
//			directions.add(1);
//			max = this.path[row-1][col];
//			direction = 1;
//		}
//		if (this.path[row][col+1] < max && this.path[row][col+1] < 1000) {
//			directions.add(4);
//			max = this.path[row][col+1];
//			direction = 4;
//		}
//		if (this.path[row][col-1] < max && this.path[row][col-1] < 1000) {
//			directions.add(2);
//			max = this.path[row][col-1];
//			direction = 2;
//		}
//		
//		Random random = new Random();
//		int rand = random.nextInt(2) + 1;
//		if (rand < 2) {
//			return direction;
//		}
//		
//		return directions.get(random.nextInt(directions.size()));
//	}
//
//	/*
//	 * Moves the ghost along a path to intercept pacman
//	 */
//	public void moveGhost(BounceGame bg, int[][]tiles, int delta) {
//		int row = getRow();
//		int col = getColumn();
//		
//		// Leave the ghost chamber
//		if (row == 9 && col == 9 && this.getX() > 320 && this.getX() < 326 && this.leftRoom == false) {
//			this.setVelocity(new Vector(0f, -0.13f));
//			this.update(delta);
//		}
//
//		if (row == 8 && col == 9 && this.leftRoom == false) {
//			this.leftRoom = true;
//		}
//		
//		// Stopped moving
//		if (this.getVelocity().getX() == 0 && this.getVelocity().getY() == 0) {
//			buildPath(tiles, bg.pacman.getRow(), bg.pacman.getColumn());
//			int direction;
//			
//			direction = this.isDead == true ? getMaxPath(row, col) : getMinPath(row, col);
//	
//			if (this.isDead) {
//				this.moves += 1;
//			}
//			
//			if (direction == 2) {
//				this.setVelocity(new Vector(-0.12f, 0f));
//			}
//			if (direction == 3) {
//				this.setVelocity(new Vector(0f, 0.12f));
//			}
//			if (direction == 4) {
//				this.setVelocity(new Vector(0.12f, 0f));
//			}
//			if (direction == 1) {
//				this.setVelocity(new Vector(0f, -0.12f));
//			}
//			
//			this.update(delta);
//		}
//		
//		// Moving down
//		if (this.getVelocity().getY() > 0) {
//			row = (int)(this.getY() - 17)/34;
//			if (tiles[row+1][col] == 0) {
//				this.setVelocity(new Vector(0f, 0f));
//			}
//		}
//			
//		// Moving up
//		if (this.getVelocity().getY() < 0) {
//			row = (int)(this.getY() + 17)/34;
//			if (tiles[row-1][col] == 0) {
//				this.setVelocity(new Vector(0f, 0f));
//			}
//		}
//			
//		// Moving right
//		if (this.getVelocity().getX() > 0) {
//			col = (int)(this.getX() - 17)/34;
//			if (tiles[row][col+1] == 0) {
//				this.setVelocity(new Vector(0f, 0f));
//			}
//		}
//			
//		// Moving left
//		if (this.getVelocity().getX() < 0) {
//			col = (int)(this.getX()+ 17)/34;
//			if (tiles[row][col-1] == 0) {
//				this.setVelocity(new Vector(0f, 0f));
//			}
//		}
//	}
//	
//	public void turnGhostDead(BounceGame bg) {
//		this.isDead = true;
//		this.moves = 0;
//		if (this.color.equals("blue")) {
//			this.removeImage(ResourceManager.getImage(bg.BLUE_GHOST_RSC));
//		} else if (this.color.equals("orange")) {
//			this.removeImage(ResourceManager.getImage(bg.ORANGE_GHOST_RSC));
//		} else if (this.color.equals("red")) {
//			this.removeImage(ResourceManager.getImage(bg.RED_GHOST_RSC));
//		}
//		this.addImage(ResourceManager.getImage(bg.DEAD_GHOST_RSC));
//	}
//	
//	public void turnGhostNormal(BounceGame bg) {
//		this.isDead = false;
//		this.moves = 0;
//		if (this.color.equals("blue")) {
//			this.addImage(ResourceManager.getImage(bg.BLUE_GHOST_RSC));
//		} else if (this.color.equals("orange")) {
//			this.addImage(ResourceManager.getImage(bg.ORANGE_GHOST_RSC));
//		} else if (this.color.equals("red")) {
//			this.addImage(ResourceManager.getImage(bg.RED_GHOST_RSC));
//		}
//	}
//	
//	/**
//	 * Update the Ball based on how much time has passed...
//	 * 
//	 * @param delta
//	 *            the number of milliseconds since the last update
//	 */
//	public void update(final int delta) {
//		translate(velocity.scale(delta));
//	}
//}