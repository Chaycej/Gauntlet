package gauntlet;

import java.io.IOException;
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
		
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(gg.client.serverIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		if (input.isKeyDown(Input.KEY_UP)) {
			gg.warrior.checkNorth(game);
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			gg.warrior.checkSouth(game);
			
			String msg = "Down";
			byte[] buf = msg.getBytes();
			DatagramPacket joinPacket = new DatagramPacket(buf, buf.length, addr, Client.PORT);
			
			try {
				gg.client.socket.send(joinPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			gg.warrior.checkEast(game);
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			gg.warrior.checkWest(game);
		}
	
		if (input.isKeyDown(Input.KEY_W)) {
			gg.ranger.checkNorth(game);
		
		}
		if (input.isKeyDown(Input.KEY_S)) {
			gg.ranger.checkSouth(game);
		}
		if (input.isKeyDown(Input.KEY_D)) {
			gg.ranger.checkEast(game);
		}
		if (input.isKeyDown(Input.KEY_A)) {
			gg.ranger.checkWest(game);
		}
		gg.warrior.update(delta);
		gg.ranger.update(delta);
	}

	@Override
	public int getID() {
		return Gauntlet.GAMESTARTSTATE;
	}
}