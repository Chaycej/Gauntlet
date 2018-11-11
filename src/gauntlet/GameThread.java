package gauntlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class GameThread extends Thread {
	
	Server server;
	GameState gameState;
	GameContainer container;
	StateBasedGame game;
	int delta;
	
	public GameThread(Server server, GameState gameState, GameContainer container, StateBasedGame game, int delta) {
		this.server = server;
		this.gameState = gameState;
		this.container = container;
		this.game = game;
		this.delta = delta;
	}
	
	public void sendValidMove(InetAddress clientAddr) {
		String msg = "3yes";
		byte[] response = msg.getBytes();
		try {
			this.server.socket.send(new DatagramPacket(response, response.length, clientAddr, Server.PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			
			byte[] buf = new byte[256];
			InetAddress clientAddr = null;
			DatagramPacket pack = new DatagramPacket(buf, buf.length);
			try {
				this.server.socket.receive(pack);
				clientAddr = pack.getAddress();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String cmd = null;
			try {
				cmd = new String(pack.getData(), "UTF-8");
				cmd = cmd.substring(1,  cmd.charAt(0) - '0'+1);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			// Client attempting to move down
			if (cmd.equals("down")) {
				if (gameState.getRow() < 25) {
					gameState.moveDown();
					this.sendValidMove(clientAddr);
				}
			} 
			
			// Client attempting to move up
			else if (cmd.equals("up")) {
				if (gameState.getRow() > 0) {
					gameState.moveUp();
					this.sendValidMove(clientAddr);
				}
			} 
			
			// Client attempting to move left
			else if (cmd.equals("left")) {
				if (gameState.getColumn() > 0) {
					gameState.moveLeft();
					this.sendValidMove(clientAddr);
				}
			} 
			
			// Client attempting to move right
			else if (cmd.equals("right")) {
				if (gameState.getColumn() < 25) {
					gameState.moveRight();
					this.sendValidMove(clientAddr);
				}
			}
		}
	}
}