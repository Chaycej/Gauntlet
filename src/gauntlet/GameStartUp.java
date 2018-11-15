package gauntlet;

import java.io.IOException;
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
		
		for (int i = 0; i < gg.wProjectiles.size(); i++) {
			gg.wProjectiles.get(i).render(g);
		}
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
		
		//checks if projectile fired 
		else if (input.isKeyPressed(Input.KEY_SPACE)) {
			//float dirX = 0, dirY = 0;	
			//gg.client.sendMovement(Client.POS_CMD, gg);
			System.out.println("p launched");
			gg.wProjectiles.add(new Projectiles(gg.warrior.getPosition().getX(),gg.warrior.getPosition().getY(),gg.warrior.getDirectionFacing()));
		}
		//gg.warrior.update(delta);
		for (int i = 0; i < gg.wProjectiles.size(); i++) {
			
			gg.wProjectiles.get(i).update(delta);
			if(gg.wProjectiles.get(i).getColumn() > gg.maxColumn 
					|| gg.wProjectiles.get(i).getRow() > gg.maxRow 
					|| gg.wProjectiles.get(i).getColumn() < 0
					|| gg.wProjectiles.get(i).getRow() < 0) {
				System.out.println("pro removed");
				gg.wProjectiles.remove(i);
			}
		}
	}
	
	public void handleServer(GameContainer container, StateBasedGame game, int delta) {
		
		Input input = container.getInput();
		Gauntlet gg = (Gauntlet)game;
		
//		//checks up movement
//		if (input.isKeyDown(Input.KEY_W)) {
//			gg.ranger.northAnimation();
//			if (gg.ranger.getRow() > 0) {
//				sendCommand("2up", gg, addr);
//				String response = gg.server.readResponse(gg);
//
//				if (response.equals("yes")) {
//					gg.ranger.setVelocity(new Vector(0, -0.1f));
//				} 
//				if (response.equals("no")) {
//					gg.ranger.setVelocity(new Vector(0, 0f));
//				}
//			} else {
//				gg.ranger.setVelocity(new Vector(0, 0f));
//			}
//		}
//		//checks down movement
//		if (input.isKeyDown(Input.KEY_S)) {
//			gg.ranger.southAnimation();
//			if (gg.ranger.getRow() < gg.row-1) {
//				sendCommand("4down", gg, addr);
//				String response = readResponse(gg);
//				if (response.equals("yes")) {
//					gg.ranger.setVelocity(new Vector(0, 0.1f));
//				} 
//				if (response.equals("no")) {
//					gg.ranger.setVelocity(new Vector(0, 0f));
//				}
//			} else {
//				gg.ranger.setVelocity(new Vector(0, 0f));
//			}
//		}
//		//checks right movement
//		if (input.isKeyDown(Input.KEY_D)) {
//			gg.ranger.eastAnimation();
//			if (gg.ranger.getColumn() < gg.col-1) {
//				sendCommand("5right", gg, addr);
//				String response = readResponse(gg);
//				if (response.equals("yes")) {
//					gg.ranger.setVelocity(new Vector(0.1f, 0));
//				} 
//				if (response.equals("no")) {
//					gg.ranger.setVelocity(new Vector(0, 0f));
//				}
//			} else {
//				gg.ranger.setVelocity(new Vector(0, 0f));
//			}
//		}
//		//checks left movement
//		if (input.isKeyDown(Input.KEY_A)) {
//			gg.ranger.westAnimation();
//			if (gg.ranger.getColumn() > 0) {
//				sendCommand("4left", gg, addr);
//				String response = readResponse(gg);
//				if (response.equals("yes")) {
//					gg.ranger.setVelocity(new Vector(-0.1f, 0));
//				} 
//				if (response.equals("no")) {
//					gg.ranger.setVelocity(new Vector(0, 0f));
//				}
//			} else {
//				gg.ranger.setVelocity(new Vector(0, 0f));
//			}
//		}
//		gg.ranger.update(delta);

	}

	@Override
	public int getID() {
		return Gauntlet.GAMESTARTSTATE;
	}
}