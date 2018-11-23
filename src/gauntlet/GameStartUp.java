package gauntlet;

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
		gauntlet.warrior.setPosition(gauntlet.warriorX, gauntlet.warriorY);
		gauntlet.ranger.setPosition(gauntlet.rangerX, gauntlet.warriorY);
		gauntlet.skeletonList.get(0).setPosition(gauntlet.skeletonX, gauntlet.skeletonY);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Gauntlet gauntlet = (Gauntlet)game;
		renderMap(container, game, g);
		gauntlet.warrior.render(g);
		gauntlet.ranger.render(g);
		gauntlet.skeleton.render(g);
		
		for (int i = 0; i < gauntlet.wProjectiles.size(); i++) {
			gauntlet.wProjectiles.get(i).render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Gauntlet gauntlet = (Gauntlet)game;

		if (gauntlet.client != null) {
			handleClient(container, game, delta);
		} else {
			handleServer(container, game, delta);
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
		
		GameState clientState = new GameState();
		clientState.setWarriorPosition((int)gauntlet.warrior.getX(), (int)gauntlet.warrior.getY());
		
		
		// Up movement
		if (input.isKeyDown(Input.KEY_UP)) {
			if (gauntlet.warrior.getRow() > 0) {
				gauntlet.warrior.setDirection(GameState.Direction.UP);
				clientState.setWarriorDirection(GameState.Direction.UP);
			}
		}
		
		// Down movement
		else if (input.isKeyDown(Input.KEY_DOWN)) {
			if (gauntlet.warrior.getRow() < Gauntlet.maxRow-1) {
				gauntlet.warrior.setDirection(GameState.Direction.DOWN);
				clientState.setWarriorDirection(GameState.Direction.DOWN);
			}
		}
		
		// Right movement
		else if (input.isKeyDown(Input.KEY_RIGHT)) {
			if (gauntlet.warrior.getColumn() < Gauntlet.maxColumn-1) {
				gauntlet.warrior.setDirection(GameState.Direction.RIGHT);
				clientState.setWarriorDirection(GameState.Direction.RIGHT);
			}
		}
		
		// Left movement
		else if (input.isKeyDown(Input.KEY_LEFT)) {
			if (gauntlet.warrior.getColumn() > 0) {
				gauntlet.warrior.setDirection(GameState.Direction.LEFT);
				clientState.setWarriorDirection(GameState.Direction.LEFT);
			}
		}
		
		// Projectile
		else if (input.isKeyPressed(Input.KEY_SPACE)) {
				gauntlet.wProjectiles.add(new Projectiles(gauntlet.warrior.getPosition().getX(),
						gauntlet.warrior.getPosition().getY(), gauntlet.warrior.getDirection()));
			clientState.setWarriorDirection(GameState.Direction.STOP);
		}
			
		// Not moving
		else {
			clientState.setWarriorDirection(GameState.Direction.STOP);
		}
		
		gauntlet.client.sendGameState(clientState);
		
		
		for (int i = 0; i < gauntlet.wProjectiles.size(); i++) {
			gauntlet.wProjectiles.get(i).update(delta);
			if(gauntlet.wProjectiles.get(i).getColumn() > Gauntlet.maxColumn 
					|| gauntlet.wProjectiles.get(i).getRow() > Gauntlet.maxRow 
					|| gauntlet.wProjectiles.get(i).getColumn() < 0
					|| gauntlet.wProjectiles.get(i).getRow() < 0) {
				gauntlet.wProjectiles.remove(i);
			}
		}
		
		// Update new game state
		GameState newGameState = gauntlet.client.readGameState();
		if (newGameState != null) {
			
			// Update warrior
			gauntlet.warrior.updateWarriorState(newGameState.getWarriorDirection(), newGameState.warriorIsMoving());
			
			// Update ranger
			gauntlet.ranger.setPosition(newGameState.getRangerX(), newGameState.getRangerY());
			gauntlet.ranger.updateAnimation(newGameState.getRangerDirection());
			
			gauntlet.skeleton.setPosition(newGameState.skeletonList.get(0).getXPos(), newGameState.skeletonList.get(0).getYPos());
		}
		
		gauntlet.skeleton.update(delta);
		gauntlet.ranger.update(delta);
		gauntlet.warrior.update(delta);
	}
	
	/*
	 *  handleServer
	 * 
	 *  Updates the ranger state based on the server's key presses and updates the rest of the 
	 *  game state once the new game state has been sent to the client.
	 */
	public void handleServer(GameContainer container, StateBasedGame game, int delta) {
		
		Input input = container.getInput();
		Gauntlet gauntlet = (Gauntlet)game;
		
		// Move skeletons
		for (Skeleton skeleton : gauntlet.skeletonList) {
			skeleton.moveGhost(gauntlet, delta);
			skeleton.setXPos((int)skeleton.getX());
			skeleton.setYPos((int)skeleton.getY());
			skeleton.update(delta);
		}
		
		gauntlet.gameState.skeletonList = gauntlet.skeletonList;
		
		// Up movement
		if (input.isKeyDown(Input.KEY_W)) {
			if (gauntlet.ranger.getRow() > 0) {
				gauntlet.ranger.northAnimation();
				gauntlet.gameState.setRangerDirection(GameState.Direction.UP);
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(0, -0.1f));
			} 
		}
		
		// Down movement
		else if (input.isKeyDown(Input.KEY_S)) {
			if (gauntlet.ranger.getRow() < Gauntlet.maxRow-1) {
				gauntlet.ranger.southAnimation();
				gauntlet.gameState.setRangerDirection(GameState.Direction.DOWN);
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(0, 0.1f));
			}
		}
		
		// Right movement
		else if (input.isKeyDown(Input.KEY_D)) {
			if (gauntlet.ranger.getColumn() < Gauntlet.maxColumn) {
				gauntlet.ranger.eastAnimation();
				gauntlet.server.
				gauntlet.gameState.setRangerDirection(GameState.Direction.RIGHT);
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(0.1f, 0));
			}
		}
		
		// Left movement
		else if (input.isKeyDown(Input.KEY_A)) {
			if (gauntlet.ranger.getColumn() > 0) {
				gauntlet.ranger.westAnimation();
				gauntlet.gameState.setRangerDirection(GameState.Direction.LEFT);
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(-0.1f, 0));
			}
		} else {
			gauntlet.gameState.setRangerMovement(false);
			gauntlet.ranger.setVelocity(new Vector(0f, 0f));
		}
		
		gauntlet.gameState.setRangerPosition((int)gauntlet.ranger.getX(), (int)gauntlet.ranger.getY());
		
		
		gauntlet.warrior.setPosition(gauntlet.gameState.getWarriorX(), gauntlet.gameState.getWarriorY());
		gauntlet.warrior.updateAnimation();
		
		gauntlet.ranger.update(delta);
	}
	
	/*
	 *  renderMap
	 * 
	 *  Renders the game map each update.
	 */
	public void renderMap(GameContainer container, StateBasedGame game, Graphics g) {
		Gauntlet gauntlet = (Gauntlet)game;
		int x = 16;
		int y = 16;
		for (int row = 0; row < Gauntlet.maxRow; row++ ) {
			for (int col = 0; col < Gauntlet.maxColumn; col++) {
				if ( gauntlet.map[row][col] == 0) {		//equals a 0 is a path
					gauntlet.mapMatrix[row][col]= new MapMatrix(x,y, 0f, 0f);
					gauntlet.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.pathTile));
				} else {							//equals a 1 is a wall
					gauntlet.mapMatrix[row][col]= new MapMatrix(x,y, 0f, 0f);
					gauntlet.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.wallTile));
				}
				x = x + 32;
				gauntlet.mapMatrix[row][col].render(g);
			}
			y = y + 32;
			x = 16;
		}
	}

	@Override
	public int getID() {
		return Gauntlet.GAMESTARTSTATE;
	}
}