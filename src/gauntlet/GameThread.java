package gauntlet;

import java.io.IOException;
import java.net.DatagramPacket;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class GameThread extends Thread {
	
	Server server;
	GameContainer container;
	StateBasedGame game;
	int delta;
	
	public GameThread(Server server, GameContainer container, StateBasedGame game, int delta) {
		this.server = server;
		this.container = container;
		this.game = game;
		this.delta = delta;
	}
	
	public void run() {
		System.out.println("Created client thread");
		while (true) {
			
			byte[] buf = new byte[256];
			DatagramPacket pack = new DatagramPacket(buf, buf.length);
			try {
				this.server.socket.receive(pack);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Received message from client");
			System.out.println(pack.getData());
		}
	}
}
