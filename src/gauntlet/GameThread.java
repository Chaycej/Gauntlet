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

	InetAddress clientAddr;

	public GameThread(Server server, GameState gameState, GameContainer container, StateBasedGame game, int delta) {
		this.server = server;
		this.gameState = gameState;
		this.container = container;
		this.game = game;
		this.delta = delta;
	}

	public void sendValidMove(InetAddress clientAddr) {
		String msg = "yes";
		byte[] response = msg.getBytes();
		try {
			this.server.socket.send(new DatagramPacket(response, response.length, clientAddr, Server.PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readClientMove() {
		byte[] buf = new byte[256];
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		try {
			this.server.socket.receive(pack);
			this.clientAddr = pack.getAddress();
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
		return cmd;
	}

	/*
	 * readClientPosition
	 * 
	 * Reads the client's updated x,y coordinates
	 */
	public int[] readClientPosition() {

		int[] newPosition = new int[2];

		byte[] buf = new byte[256];
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		try {
			this.server.socket.receive(pack);
			this.clientAddr = pack.getAddress();
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

		newPosition[0] = Integer.valueOf(cmd);

		byte[] buf2 = new byte[256];
		pack = new DatagramPacket(buf2, buf2.length);
		try {
			this.server.socket.receive(pack);
			this.clientAddr = pack.getAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}

		cmd = null;
		try {
			cmd = new String(pack.getData(), "UTF-8");
			cmd = cmd.substring(1,  cmd.charAt(0) - '0'+1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		newPosition[1] = Integer.valueOf(cmd);
		return newPosition;
	}

	public void run() {
		while (true) {
			String cmd = this.readClientMove();

			// Client attempting to move down
			if (cmd.equals("down")) {
				if (gameState.getRow() < 24) {
					this.sendValidMove(this.clientAddr);
					int[] position = readClientPosition();
					this.gameState.updatePosition(position[0], position[1]);
				}
			} 

			// Client attempting to move up
			else if (cmd.equals("up")) {
				if (gameState.getRow() > 0) {
					this.sendValidMove(this.clientAddr);
					int[] position = readClientPosition();
					this.gameState.updatePosition(position[0], position[1]);
				}
			} 

			// Client attempting to move left
			else if (cmd.equals("left")) {
				if (gameState.getColumn() > 0) {
					this.sendValidMove(this.clientAddr);
					int[] position = readClientPosition();
					this.gameState.updatePosition(position[0], position[1]);
				}
			} 

			// Client attempting to move right
			else if (cmd.equals("right")) {
				if (gameState.getColumn() < 24) {
					this.sendValidMove(this.clientAddr);
					int[] position = readClientPosition();
					this.gameState.updatePosition(position[0], position[1]);
				}
			}

		}
	}
}