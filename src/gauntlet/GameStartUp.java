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
		gg.warrior.setPosition(200,200);
		gg.ranger.setPosition(280, 200);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Gauntlet gg = (Gauntlet)game;
		
		int x = 16;
		int y = 16;
		for (int row=0; row<25; row++ ) {
			for (int col=0; col<25; col++) {
				if ( gg.map[row][col] == 48) {		//equals a 0 is a path
					gg.mapM[row][col]= new MapMatrix(x,y, 0f, 0f);
					gg.mapM[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.pathTile));
				} else {							//equals a 1 is a wall
					gg.mapM[row][col]= new MapMatrix(x,y, 0f, 0f);
					gg.mapM[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.wallTile));
				}
				x = x+32;
				gg.mapM[row][col].render(g);
			}
			y = y+32;
			x=16;
		}
		
		gg.warrior.render(g);
		gg.warrior.setVelocity(new Vector(0f,0f));
		gg.ranger.render(g);
		gg.ranger.setVelocity(new Vector(0f,0f));
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Input input = container.getInput();
		Gauntlet gg = (Gauntlet)game;
		
		// Client controls
		if (gg.client != null) {
			InetAddress addr = null;
			try {
				addr = InetAddress.getByName(gg.client.serverIP);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			if (input.isKeyDown(Input.KEY_UP)) {
				if (gg.warrior.getRow() > 0) {
					sendCommand("2up", gg, addr);
					String response = readResponse(gg);
					
					if (response.equals("yes")) {
						gg.warrior.checkNorth(game);
					}
				}
			}
			if (input.isKeyDown(Input.KEY_DOWN)) {
				if (gg.warrior.getRow() < 24) {
					sendCommand("4down", gg, addr);
					String response = readResponse(gg);
					
					if (response.equals("yes")) {
						gg.warrior.checkSouth(game);
					}
				}
			}
			if (input.isKeyDown(Input.KEY_RIGHT)) {
				if (gg.warrior.getColumn() < 24) {
					sendCommand("5right", gg, addr);
					String response = readResponse(gg);
					
					if (response.equals("yes")) {
						gg.warrior.checkEast(game);
					}
				}
			}
			if (input.isKeyDown(Input.KEY_LEFT)) {
				if (gg.warrior.getColumn() > 0) {
					sendCommand("4left", gg, addr);
					String response = readResponse(gg);
					
					if (response.equals("yes")) {
						gg.warrior.checkWest(game);
					}
				}
			}
		
			gg.warrior.update(delta);
		}
		
//		if (input.isKeyDown(Input.KEY_W)) {
//			gg.ranger.checkNorth(game);
//		
//		}
//		if (input.isKeyDown(Input.KEY_S)) {
//			gg.ranger.checkSouth(game);
//		}
//		if (input.isKeyDown(Input.KEY_D)) {
//			gg.ranger.checkEast(game);
//		}
//		if (input.isKeyDown(Input.KEY_A)) {
//			gg.ranger.checkWest(game);
//		}
//		gg.ranger.update(delta);

	}
	
	public void sendCommand(String command, Gauntlet gg, InetAddress serverAddr) {
		byte[] buf = command.getBytes();
		DatagramPacket joinPacket = new DatagramPacket(buf, buf.length, serverAddr, Client.PORT);
		
		try {
			gg.client.socket.send(joinPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String readResponse(Gauntlet gg) {
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		try {
			gg.client.socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String cmd = null;
		try {
			cmd = new String(packet.getData(), "UTF-8");
			cmd = cmd.substring(1, cmd.charAt(0) - '0'+1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return cmd;
	}

	@Override
	public int getID() {
		return Gauntlet.GAMESTARTSTATE;
	}
}