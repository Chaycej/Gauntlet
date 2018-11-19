package gauntlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
		Gauntlet gg = (Gauntlet)game;
		gg.warrior.setPosition(gg.warriorX,gg.warriorY);
		gg.ranger.setPosition(gg.rangerX, gg.warriorY);
	}

	public void renderMap(GameContainer container, StateBasedGame game, Graphics g) {
		Gauntlet gg = (Gauntlet)game;
		int x = 16;
		int y = 16;
		for (int row=0; row<gg.maxRow; row++ ) {
			for (int col=0; col<gg.maxColumn; col++) {
				if ( gg.map[row][col] == 48) {		//equals a 0 is a path
					gg.mapMatrix[row][col]= new MapMatrix(x,y, 0f, 0f);
					gg.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.pathTile));
				} else {							//equals a 1 is a wall
					gg.mapMatrix[row][col]= new MapMatrix(x,y, 0f, 0f);
					gg.mapMatrix[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.wallTile));
				}
				x = x+32;
				gg.mapMatrix[row][col].render(g);
			}
			y = y+32;
			x=16;
		}
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Gauntlet gauntlet = (Gauntlet)game;
		renderMap(container, game, g);
		gauntlet.warrior.render(g);
		gauntlet.warrior.setVelocity(new Vector(0f,0f));
		gauntlet.ranger.render(g);
		gauntlet.ranger.setVelocity(new Vector(0f,0f));
		
		for (int i = 0; i < gauntlet.wProjectiles.size(); i++) {
			gauntlet.wProjectiles.get(i).render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
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
	 * Handles client by sending the server client commands based on the client's next move.
	 * The general client protocol is as follows:
	 *		
	 *		client movement:
	 *			1. Send current (x,y) position to server.
	 *			2. Send direction command to server. 
	 *			3. Wait for server's response.
	 *			4. Move client to requested direction.
	 */
	public void handleClient(GameContainer container, StateBasedGame game, int delta) {
		Input input = container.getInput();
		Gauntlet gauntlet = (Gauntlet)game;
		
		//checks up movement
		if (input.isKeyDown(Input.KEY_UP)) {
			if (gauntlet.warrior.getRow() > 0) {
				gauntlet.client.sendMovement(Client.UP_CMD, gauntlet);
			}
		}
		
		//checks down movement
		else if (input.isKeyDown(Input.KEY_DOWN)) {
			if (gauntlet.warrior.getRow() < gauntlet.maxRow-1) {
				gauntlet.client.sendMovement(Client.DOWN_CMD, gauntlet);
			}
		}
		
		//checks right movement
		else if (input.isKeyDown(Input.KEY_RIGHT)) {
			if (gauntlet.warrior.getColumn() < gauntlet.maxColumn-1) {
				gauntlet.client.sendMovement(Client.RIGHT_CMD, gauntlet);
			}
		}
		
		//checks left movement
		else if (input.isKeyDown(Input.KEY_LEFT)) {
			if (gauntlet.warrior.getColumn() > 0) {
				gauntlet.client.sendMovement(Client.LEFT_CMD, gauntlet);
			}
		}
		
		// Not moving
		else {
			gauntlet.client.sendMovement("2no\n", gauntlet);
		}
		
		// Projectile
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			gauntlet.wProjectiles.add(new Projectiles(gauntlet.warrior.getPosition().getX(),
					gauntlet.warrior.getPosition().getY(), gauntlet.warrior.getDirectionFacing()));
		}
		
		for (int i = 0; i < gauntlet.wProjectiles.size(); i++) {
			gauntlet.wProjectiles.get(i).update(delta);
			if(gauntlet.wProjectiles.get(i).getColumn() > gauntlet.maxColumn 
					|| gauntlet.wProjectiles.get(i).getRow() > gauntlet.maxRow 
					|| gauntlet.wProjectiles.get(i).getColumn() < 0
					|| gauntlet.wProjectiles.get(i).getRow() < 0) {
				gauntlet.wProjectiles.remove(i);
			}
		}
		
		// Render new game state
		GameState newGameState = gauntlet.client.readGameState();
		if (newGameState != null) {
			
			if (!newGameState.warriorIsMoving()) {
				gauntlet.warrior.setVelocity(new Vector(0f, 0f));
			}
			
			if (newGameState.getWarriorDirection() == GameState.Direction.UP) {
				gauntlet.warrior.northAnimation();
				if (newGameState.warriorIsMoving()) {
					gauntlet.warrior.setVelocity(new Vector(0f, -0.1f));
				}
			} else if (newGameState.getWarriorDirection() == GameState.Direction.DOWN) {
				gauntlet.warrior.southAnimation();
				if (newGameState.warriorIsMoving()) {
					gauntlet.warrior.setVelocity(new Vector(0f, 0.1f));
				}
			} else if (newGameState.getWarriorDirection() == GameState.Direction.LEFT) {
				gauntlet.warrior.westAnimation();
				if (newGameState.warriorIsMoving()) {
					gauntlet.warrior.setVelocity(new Vector(-0.1f, 0f));
				}
			} else if (newGameState.getWarriorDirection() == GameState.Direction.RIGHT) {
				gauntlet.warrior.eastAnimation();
				if (newGameState.warriorIsMoving()) {
					gauntlet.warrior.setVelocity(new Vector(0.1f, 0f));
				}
			
			}
		}
		
		gauntlet.warrior.update(delta);
	}
	
	public void handleServer(GameContainer container, StateBasedGame game, int delta) {
		
		Input input = container.getInput();
		Gauntlet gauntlet = (Gauntlet)game;
		
		//checks up movement
		if (input.isKeyDown(Input.KEY_W)) {
			if (gauntlet.ranger.getRow() > 0) {
				gauntlet.ranger.northAnimation();
				gauntlet.gameState.setRangerDirection(GameState.Direction.UP);
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(0, -0.1f));
			} 
		}
		
		//checks down movement
		else if (input.isKeyDown(Input.KEY_S)) {
			if (gauntlet.ranger.getRow() < gauntlet.maxRow-1) {
				gauntlet.ranger.southAnimation();
				gauntlet.gameState.setRangerDirection(GameState.Direction.DOWN);
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(0, 0.1f));
			}
		}
		
		//checks right movement
		else if (input.isKeyDown(Input.KEY_D)) {
			if (gauntlet.ranger.getColumn() < gauntlet.maxColumn) {
				gauntlet.ranger.eastAnimation();
				gauntlet.server.
				gauntlet.gameState.setRangerDirection(GameState.Direction.RIGHT);
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(0.1f, 0));
			}
		}
		
		//checks left movement
		else if (input.isKeyDown(Input.KEY_A)) {
			if (gauntlet.ranger.getColumn() > 0) {
				gauntlet.ranger.westAnimation();
				gauntlet.gameState.setRangerDirection(GameState.Direction.LEFT);
				gauntlet.gameState.setRangerMovement(true);
				gauntlet.ranger.setVelocity(new Vector(-0.1f, 0));
			}
		}
		
//		// Update warrior position and direction
//		gg.warrior.setPosition((float)gg.gameState.getWarriorX(), (float)gg.gameState.getWarriorY());
//		switch(gg.gameState.getWarriorDirection()) {
//		case "up":
//			gg.warrior.northAnimation();
//		case "le":
//			gg.warrior.westAnimation();
//		case "ri":
//			gg.warrior.eastAnimation();
//		case "do":
//			gg.warrior.southAnimation();
//		default:
//			System.out.println("Unrecognized direction");
//		}
		
		gauntlet.gameState.setRangerPosition((int)gauntlet.ranger.getX(), (int)gauntlet.ranger.getY());
		gauntlet.server.sendGameState(gauntlet.gameState);
		gauntlet.ranger.update(delta);
	}

	@Override
	public int getID() {
		return Gauntlet.GAMESTARTSTATE;
	}
}