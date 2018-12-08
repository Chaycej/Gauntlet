package gauntlet;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import jig.ResourceManager;
import jig.Vector;

public class GameStartUp extends BasicGameState{

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		container.setSoundOn(true);

		Gauntlet gauntlet = (Gauntlet)game;
		
		for (int i = 0; i < 3; i++) {
		    gauntlet.potions.add(new Powerups(32.0f+32.0f*i,32.0f+32.0f*i, i));
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Gauntlet gauntlet = (Gauntlet)game;

		// Clears all background noise/ pictures Comment out you'll understand.
		g.clear();
		
		// Moves the map in accordance to the character both are needed.
		g.translate(gauntlet.ScreenWidth/2-gauntlet.warriorCamera.getXoffset(), 
				gauntlet.ScreenHeight/2-gauntlet.warriorCamera.getYoffset());
		g.translate(gauntlet.ScreenWidth/2-gauntlet.rangerCamera.getXoffset(), 
				gauntlet.ScreenHeight/2-gauntlet.rangerCamera.getYoffset());

		renderMap(container, game, g);
		
		if (gauntlet.client != null) {
			g.drawString("Warrior health: " + String.valueOf(gauntlet.warrior.getHealth()),
					gauntlet.warriorCamera.getXoffset() - 100, gauntlet.warriorCamera.getYoffset() - 400);
			g.drawString("Ranger health: " + String.valueOf(gauntlet.ranger.getHealth()),
					gauntlet.warriorCamera.getXoffset() - 300, gauntlet.warriorCamera.getYoffset() - 400);
		} else {
			g.drawString("Warrior health: " + String.valueOf(gauntlet.warrior.getHealth()),
					gauntlet.rangerCamera.getXoffset() - 100, gauntlet.rangerCamera.getYoffset() - 400);
			g.drawString("Ranger health: " + String.valueOf(gauntlet.ranger.getHealth()),
					gauntlet.rangerCamera.getXoffset() - 300, gauntlet.rangerCamera.getYoffset() - 400);
		}
		
		// Don't render a dead guy
		if(!gauntlet.warrior.isDead()) {
		    gauntlet.warrior.render(g);
		}
		
		// Don't render a dead guy
		if(!gauntlet.ranger.isDead()) {
		    gauntlet.ranger.render(g);
		}

		for (Skeleton s : gauntlet.skeletonList) {
			if (!s.isDead()) {
				s.render(g);
			}
		}
		
		for (Projectile projectile : gauntlet.warriorProjectiles) {
			projectile.render(g);
		}

		for (Projectile projectile : gauntlet.rangerProjectiles) {
			projectile.render(g);
		}

		
		for (Powerups potions : gauntlet.potions) {
			potions.render(g);
		}
		
		gauntlet.key1.render(g);
		gauntlet.key2.render(g);
		gauntlet.key3.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Gauntlet gauntlet = (Gauntlet)game;

		if (gauntlet.client != null) {
			handleClient(container, game, delta);
		} else {
			handleServer(container, game, delta);
		}
		
		for (int i = 0; i < gauntlet.potions.size(); i++) {
			if(gauntlet.warrior.collides(gauntlet.potions.get(i)) != null) {
				gauntlet.warrior.potion(gauntlet.potions.get(i).getType());
				gauntlet.potions.remove(i);
			}
			else if(gauntlet.ranger.collides(gauntlet.potions.get(i)) != null) {
				gauntlet.ranger.potion(gauntlet.potions.get(i).getType());
				gauntlet.potions.remove(i);
			}
		}
	}

	/*
	 * handleClient
	 * 
	 * Handles client by sending the server client state based on the client's next move.
	 * The general client protocol is as follows:
	 *	
	 */
	public void handleClient(GameContainer container, StateBasedGame game, int delta) {
		Input input = container.getInput();
		Gauntlet gauntlet = (Gauntlet)game;

		gauntlet.gameState.setWarriorPosition((int)gauntlet.warrior.getX(), (int)gauntlet.warrior.getY());
		
		// Up movement
		if (input.isKeyDown(Input.KEY_UP)) {
			if (gauntlet.warrior.getRow() > 0) {
				gauntlet.warrior.setDirection(GameState.Direction.UP);
				gauntlet.gameState.setWarriorDirection(GameState.Direction.UP);
			}
		}

		// Down movement
		else if (input.isKeyDown(Input.KEY_DOWN)) {
			if ( gauntlet.warrior.getRow() < Gauntlet.maxRow-1) {
				gauntlet.warrior.setDirection(GameState.Direction.DOWN);
				gauntlet.gameState.setWarriorDirection(GameState.Direction.DOWN);
			}
		}

		// Right movement
		else if (input.isKeyDown(Input.KEY_RIGHT)) {
			if (gauntlet.warrior.getColumn() < Gauntlet.maxColumn-1) {
				gauntlet.warrior.setDirection(GameState.Direction.RIGHT);
				gauntlet.gameState.setWarriorDirection(GameState.Direction.RIGHT);
			}
		}

		// Left movement
		else if (input.isKeyDown(Input.KEY_LEFT)) {
			if (gauntlet.warrior.getColumn() > 0) {
				gauntlet.warrior.setDirection(GameState.Direction.LEFT);
				gauntlet.gameState.setWarriorDirection(GameState.Direction.LEFT);
			}
		}

		// Projectile
		else if (input.isKeyPressed(Input.KEY_M)) {

			Projectile projectile = new Projectile(gauntlet.warrior.getPosition().getX(),
					gauntlet.warrior.getPosition().getY(), gauntlet.warrior.getDirection());
			gauntlet.warriorProjectiles.add(projectile);
			gauntlet.gameState.setWarriorDirection(GameState.Direction.STOP);
		}

		// Not moving
		else {
			gauntlet.gameState.setWarriorDirection(GameState.Direction.STOP);
		}

		updateProjectiles(gauntlet.skeletonList, gauntlet.warriorProjectiles, delta);
		gauntlet.gameState.warriorProjectiles = gauntlet.warriorProjectiles;
		gauntlet.gameState.skeletons = gauntlet.skeletonList;

		gauntlet.client.sendGameState(gauntlet.gameState);

		// Update new game state
		GameState newGameState = gauntlet.client.readGameState();
		if (newGameState != null) {
			
			gauntlet.warrior.setPosition(newGameState.getWarriorX(), newGameState.getWarriorY());
			
			// Update warrior and ranger health
			gauntlet.warrior.setHealth(newGameState.getWarriorHealth());
			gauntlet.ranger.setHealth(newGameState.getRangerHealth());
			
			// Update warrior
			gauntlet.warrior.updateWarriorState(newGameState.getWarriorDirection(), newGameState.warriorIsMoving());

			// Update ranger
			gauntlet.ranger.setPosition(newGameState.getRangerX(), newGameState.getRangerY());
			gauntlet.ranger.updateAnimation(newGameState.getRangerDirection());

			// Update skeletons
			for (int i = 0; i < newGameState.skeletons.size(); i++) {
				gauntlet.skeletonList.get(i).setPosition(newGameState.skeletons.get(i).getXPos(),
						newGameState.skeletons.get(i).getYPos());
				gauntlet.skeletonList.get(i).setXPos(newGameState.skeletons.get(i).getXPos());
				gauntlet.skeletonList.get(i).setYPos(newGameState.skeletons.get(i).getYPos());
				gauntlet.skeletonList.get(i).setHealth(newGameState.skeletons.get(i).getHealth());
			}

			// Update teammates projectiles
			gauntlet.rangerProjectiles = newGameState.rangerProjectiles;
			for (Projectile projectile : gauntlet.rangerProjectiles) {
				projectile.addImage();
				projectile.setPosition(projectile.getXPos(), projectile.getYPos());
			}
		}
		
		for (Skeleton s : gauntlet.skeletonList) {
			s.update(delta);
		}
		
		gauntlet.ranger.update(delta);
		gauntlet.warrior.update(delta);
		
		//updates the camera as Warrior moves
		gauntlet.warriorCamera.update(gauntlet.warrior.getPosition().getX(), gauntlet.warrior.getPosition().getY());
	}

	/*
	 *  handleServer
	 * 
	 *  Updates the ranger state based on the server's key presses and updates the rest of the 
	 *  game state once the new game state has been sent to the client.
	 *  
	 *  Note: Server is also handling client state on a seperate thread.
	 */
	public void handleServer(GameContainer container, StateBasedGame game, int delta) {

		Input input = container.getInput();
		Gauntlet gauntlet = (Gauntlet)game;

		// Move skeletons
		for (Skeleton skeleton : gauntlet.skeletonList) {
			skeleton.moveGhost(gauntlet, delta);
			skeleton.update(delta);
		}
		
		gauntlet.gameState.skeletons = gauntlet.skeletonList;
		int row = gauntlet.ranger.getRow();
		int col = gauntlet.ranger.getColumn();
		int tempCol = 0;
		int tempRow = 0;
		
		if (gauntlet.ranger.isDead()) {
			gauntlet.gameState.setRangerDirection(GameState.Direction.STOP);
			gauntlet.ranger.setPosition(gauntlet.rangerSpawnX, gauntlet.rangerSpawnY);
			gauntlet.ranger.setHealth(100);
		}
		
		if (gauntlet.warrior.isDead()) {
			gauntlet.gameState.setWarriorDirection(GameState.Direction.STOP);
			gauntlet.warrior.setPosition(gauntlet.warriorSpawnX, gauntlet.warriorSpawnY);
			gauntlet.gameState.setWarriorPosition(gauntlet.warriorSpawnX, gauntlet.warriorSpawnY);
			//gauntlet.warrior.setHealth(100);
		}
		
		// Up movement
		else if (input.isKeyDown(Input.KEY_UP)) {
			gauntlet.ranger.northAnimation();
			gauntlet.ranger.setDirection(GameState.Direction.UP);
			gauntlet.gameState.setRangerDirection(GameState.Direction.UP);
			tempRow = (((int) gauntlet.ranger.getY())-20)/32;
			if (Gauntlet.map[tempRow][col] == 1) {
				gauntlet.ranger.setVelocity(new Vector(0f, 0f));
			}
			
			else if (row > 0 && Gauntlet.map[tempRow-1][col] == 0) {
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(0, -0.5f));
			} 
		}

		// Down movement
		else if (input.isKeyDown(Input.KEY_DOWN)) {
			gauntlet.ranger.southAnimation();
			gauntlet.ranger.setDirection(GameState.Direction.DOWN);
			gauntlet.gameState.setRangerDirection(GameState.Direction.DOWN);
			tempRow = (((int) gauntlet.ranger.getY())+20)/32;
			
			if (Gauntlet.map[tempRow][col] == 1) {
				gauntlet.ranger.setVelocity(new Vector(0f, 0f));
			}
			
			else if (row < Gauntlet.maxRow-1 && Gauntlet.map[tempRow+1][col] == 0) {
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(0, 0.5f));
			}
		}

		// Right movement
		else if (input.isKeyDown(Input.KEY_RIGHT)) {
			gauntlet.ranger.eastAnimation();
			gauntlet.ranger.setDirection(GameState.Direction.RIGHT);
			gauntlet.gameState.setRangerDirection(GameState.Direction.RIGHT);
			tempCol = (((int) gauntlet.ranger.getX())+20)/32;
			
			if (Gauntlet.map[row][tempCol] == 1) {
				gauntlet.ranger.setVelocity(new Vector(0f, 0f));
			}
			
			else if (col < Gauntlet.maxColumn && Gauntlet.map[row][tempCol+1] == 0) {
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(0.5f, 0));
			}
		}

		// Left movement
		else if (input.isKeyDown(Input.KEY_LEFT)) {
			gauntlet.ranger.westAnimation();
			gauntlet.ranger.setDirection(GameState.Direction.LEFT);
			gauntlet.gameState.setRangerDirection(GameState.Direction.LEFT);
			tempCol = (((int) gauntlet.ranger.getX())-20)/32;
			
			if (Gauntlet.map[row][tempCol] == 1) {
				gauntlet.ranger.setVelocity(new Vector(0f, 0f));
			}
			
			else if (col > 0 && Gauntlet.map[row][tempCol-1] == 0) {
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(-0.5f, 0));
			}
		} 

		// Projectile
		else if (input.isKeyPressed(Input.KEY_M)) {
			Projectile projectile = new Projectile(gauntlet.ranger.getPosition().getX(),
					gauntlet.ranger.getPosition().getY(), gauntlet.ranger.getDirection());
			gauntlet.rangerProjectiles.add(projectile);
			gauntlet.gameState.setRangerDirection(GameState.Direction.STOP);
		}

		// If no input stop the ranger
		else {
			gauntlet.gameState.setRangerMovement(false);
			gauntlet.ranger.setVelocity(new Vector(0f, 0f));
		}
		
		// Check for character-enemy collisions
		for (Skeleton s : gauntlet.skeletonList) {
			if (s.collides(gauntlet.warrior) != null && !s.isDead()) {
				gauntlet.warrior.takeHit();
			}
			if (s.collides(gauntlet.ranger) != null && !s.isDead()) {
				gauntlet.ranger.takeHit();
			}
		}
		
		if (gauntlet.warrior.collides(gauntlet.key1) != null || gauntlet.ranger.collides(gauntlet.key1) != null) {
			gauntlet.key1.keyUsed = true;
			System.out.println("collided with key 1");
		}
		if (gauntlet.warrior.collides(gauntlet.key2) != null || gauntlet.ranger.collides(gauntlet.key2) != null) {
			gauntlet.key2.keyUsed = true;
		}
		if (gauntlet.warrior.collides(gauntlet.key3) != null  || gauntlet.ranger.collides(gauntlet.key3) != null) {
			gauntlet.key3.keyUsed = true;
		}
		
		// Update server's game state before sending to client
		gauntlet.gameState.setRangerPosition((int)gauntlet.ranger.getX(), (int)gauntlet.ranger.getY());
		updateProjectiles(gauntlet.skeletonList, gauntlet.rangerProjectiles, delta);
		gauntlet.gameState.rangerProjectiles = gauntlet.rangerProjectiles;
		gauntlet.gameState.skeletons = gauntlet.skeletonList;

		// Update teammate's position
		gauntlet.warrior.setPosition(gauntlet.gameState.getWarriorX(), gauntlet.gameState.getWarriorY());
		gauntlet.warrior.updateAnimation();

		// Update teammate projectiles
		gauntlet.warriorProjectiles = gauntlet.gameState.warriorProjectiles;
		for (Projectile projectile : gauntlet.warriorProjectiles) {
			projectile.addImage();
			projectile.setPosition(projectile.getXPos(), projectile.getYPos());
		}
		gauntlet.ranger.update(delta);
		
		//updates the camera as Ranger moves.
		gauntlet.rangerCamera.update(gauntlet.ranger.getPosition().getX(), gauntlet.ranger.getPosition().getY());
	}

	/*
	 *  renderMap
	 * 
	 *  Renders the game map each update.
	 */
	public void renderMap(GameContainer container, StateBasedGame game, Graphics g) {
		Gauntlet gauntlet = (Gauntlet)game;
		int x = (int) (16);// + gauntlet.warriorCamera.getXoffset()*32);
		int y = (int) (16);// + gauntlet.warriorCamera.getYoffset()*32);
		for (int row = 0; row < Gauntlet.maxRow; row++ ) {
			for (int col = 0; col < Gauntlet.maxColumn; col++) {
				if ( Gauntlet.map[row][col] == 0) {		//equals a 0 is a path
					gauntlet.mapMatrix[row][col]= new MapMatrix(x,y, 0f, 0f);
					gauntlet.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.pathTile));
				} 
				if (Gauntlet.map[row][col] == 1){							//equals a 1 is a wall
					gauntlet.mapMatrix[row][col]= new MapMatrix(x,y, 0f, 0f);
					gauntlet.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.wallTile));
				} 
				if (Gauntlet.map[row][col] == 2){
					gauntlet.mapMatrix[row][col]= new MapMatrix(x,y, 0f, 0f);
					if (gauntlet.key2.keyUsed == true) {
						gauntlet.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.doorOSouth));
					} else {
						gauntlet.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.doorCSouth));
					}
				}
				if (Gauntlet.map[row][col] == 3){
					gauntlet.mapMatrix[row][col]= new MapMatrix(x,y, 0f, 0f);
					if (gauntlet.key1.keyUsed == true) {
						gauntlet.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.doorOWest));
					} else {
						gauntlet.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.doorCWest));
					}
				}
				if (Gauntlet.map[row][col] == 4){
					gauntlet.mapMatrix[row][col]= new MapMatrix(x,y, 0f, 0f);
					if (gauntlet.key3.keyUsed == true) {
						gauntlet.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.doorOEast));
					} else {
						gauntlet.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.doorCEast));
					}
				}
				x = x + 32;
				gauntlet.mapMatrix[row][col].render(g);
			}
			y = y + 32;
			x = 16;
		}
		if (gauntlet.key1.keyUsed == false) {
			gauntlet.key1.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.KeyHDown));
		} else {
			gauntlet.key1.removeImage(ResourceManager.getImage(Gauntlet.KeyHDown));
		}
		if (gauntlet.key2.keyUsed == false) {
			gauntlet.key2.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.KeyHDown));
		} else {
			gauntlet.key2.removeImage(ResourceManager.getImage(Gauntlet.KeyHDown));
		}
		if (gauntlet.key3.keyUsed == false) {
			gauntlet.key3.addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.KeyHDown));
		} else {
			gauntlet.key3.removeImage(ResourceManager.getImage(Gauntlet.KeyHDown));
		}
	}

	/*
	 *  updateProjectiles
	 * 
	 *  updates projectile locations.
	 */
	public void updateProjectiles(ArrayList<Skeleton> skeletonList, java.util.Vector<Projectile> projectiles, int delta) {

		ArrayList<Integer> removeList = new ArrayList<>();
		

		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update(delta);
			projectiles.get(i).setXPos((int) projectiles.get(i).getX());
			projectiles.get(i).setYPos((int) projectiles.get(i).getY());
			
			int row = projectiles.get(i).getRow();
			int col = projectiles.get(i).getColumn();
			
			if(col >= Gauntlet.maxColumn || row >= Gauntlet.maxRow || col < 0 || row < 0) {
				removeList.add(i);
				continue;
			}
			
			if (Gauntlet.map[row][col] == 1) {
				removeList.add(i);
				continue;
			}
			
			// Check if a projectile hits an enemy
			for (Skeleton s : skeletonList) {
				if (row == s.getRow() && col == s.getColumn()) {
					removeList.add(i);
					s.kill();
				}
			}
		}
		
		int offSet = 0;
		for (int i : removeList) {
			projectiles.remove(i - offSet);
			offSet += 1;
		}
	}
	
	public void flushEnemies(ArrayList<Skeleton> skeletonList) {
		ArrayList<Integer> removeList = new ArrayList<>();
		int lastRemoved = 0;
		for (int i = skeletonList.size(); i > lastRemoved; i--) {
			if (skeletonList.get(i).isDead()) {
				removeList.add(i);
				lastRemoved = i;
			}
		}
		
		int offSet = 0;
		for (int i : removeList) {			
			skeletonList.remove(i - offSet);
			offSet += 1;
		}
	}


	@Override
	public int getID() {
		return Gauntlet.GAMESTARTSTATE;
	}
}