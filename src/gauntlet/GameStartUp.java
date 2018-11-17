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
		Gauntlet gg = (Gauntlet)game;
		renderMap(container, game, g);
		gg.warrior.render(g);
		gg.warrior.setVelocity(new Vector(0f,0f));
		gg.ranger.render(g);
		gg.ranger.setVelocity(new Vector(0f,0f));
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		Gauntlet gg = (Gauntlet)game;

		if (gg.client != null) {
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
		Gauntlet gg = (Gauntlet)game;
		
//		try {
//			ObjectInputStream in = new ObjectInputStream(gg.client.socket.getInputStream());
//			GameState gameState = (GameState)in.readObject();
//			System.out.println("Got server game state");
//			System.out.println("Server position is " + gameState.getRangerX() + " " + gameState.getRangerY());
//		} catch (IOException | ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		
		//checks up movement
		if (input.isKeyDown(Input.KEY_UP)) {
			if (gg.warrior.getRow() > 0) {
				gg.client.sendMovement(Client.UP_CMD, gg);
				String response = gg.client.readServerResponse(gg);

				if (response.equals("y")) {
					gg.warrior.northAnimation();
					gg.warrior.setVelocity(new Vector(0, -0.1f));
				}
			} else {
				gg.warrior.setVelocity(new Vector(0, 0f));
			}
			gg.warrior.update(delta);
		}
		
		//checks down movement
		else if (input.isKeyDown(Input.KEY_DOWN)) {
			if (gg.warrior.getRow() < gg.maxRow-1) {
				gg.client.sendMovement(Client.DOWN_CMD, gg);
				String response = gg.client.readServerResponse(gg);
				if (response.equals("y")) {
					gg.warrior.southAnimation();
					gg.warrior.setVelocity(new Vector(0, 0.1f));
				}
			} else {
				gg.warrior.setVelocity(new Vector(0, 0f));
			}
			gg.warrior.update(delta);
		}
		
		//checks right movement
		else if (input.isKeyDown(Input.KEY_RIGHT)) {
			if (gg.warrior.getColumn() < gg.maxColumn-1) {
				gg.client.sendMovement(Client.RIGHT_CMD, gg);
				String response = gg.client.readServerResponse(gg);
				if (response.equals("y")) {
					gg.warrior.eastAnimation();
					gg.warrior.setVelocity(new Vector(0.1f, 0));
				}
			} else {
				gg.warrior.setVelocity(new Vector(0, 0f));
			}
			gg.warrior.update(delta);
		}
		
		//checks left movement
		else if (input.isKeyDown(Input.KEY_LEFT)) {
			if (gg.warrior.getColumn() > 0) {
				gg.client.sendMovement(Client.LEFT_CMD, gg);
				String response = gg.client.readServerResponse(gg);
				if (response.equals("y")) {
					gg.warrior.westAnimation();
					gg.warrior.setVelocity(new Vector(-0.1f, 0));
				} 
			} else {
				gg.warrior.setVelocity(new Vector(0, 0f));
			}
			gg.warrior.update(delta);
		}
	}
	
	public void handleServer(GameContainer container, StateBasedGame game, int delta) {
		
		Input input = container.getInput();
		Gauntlet gg = (Gauntlet)game;
		
		String direction = null;
		
		//checks up movement
		if (input.isKeyDown(Input.KEY_W)) {
			if (gg.ranger.getRow() > 0) {
				gg.ranger.northAnimation();
				direction = "up";
				gg.ranger.setVelocity(new Vector(0, -0.1f));
			} 
		}
		
		//checks down movement
		else if (input.isKeyDown(Input.KEY_S)) {
			if (gg.ranger.getRow() < gg.maxRow-1) {
				gg.ranger.southAnimation();
				direction = "do";
				gg.ranger.setVelocity(new Vector(0, 0.1f));
			}
		}
		
		//checks right movement
		else if (input.isKeyDown(Input.KEY_D)) {
			if (gg.ranger.getColumn() < gg.maxColumn) {
				gg.ranger.eastAnimation();
				direction = "ri";
				gg.ranger.setVelocity(new Vector(0.1f, 0));
			}
		}
		
		//checks left movement
		else if (input.isKeyDown(Input.KEY_A)) {
			if (gg.ranger.getColumn() > 0) {
				gg.ranger.westAnimation();
				direction = "le";
				gg.ranger.setVelocity(new Vector(-0.1f, 0));
			}
		}
		
		// Update warrior position and direction
		gg.warrior.setPosition((float)gg.gameState.getWarriorX(), (float)gg.gameState.getWarriorY());
		switch(gg.gameState.getWarriorDirection()) {
		case "up":
			gg.warrior.northAnimation();
		case "le":
			gg.warrior.westAnimation();
		case "ri":
			gg.warrior.eastAnimation();
		case "do":
			gg.warrior.southAnimation();
		default:
			System.out.println("Unrecognized direction");
		}
		
		gg.gameState.setRangerDirection(direction);
		gg.gameState.setRangerPosition((int)gg.ranger.getX(), (int)gg.ranger.getY());
		//gg.server.sendGameState(gg.gameState);
		gg.ranger.update(delta);
	}

	@Override
	public int getID() {
		return Gauntlet.GAMESTARTSTATE;
	}
}