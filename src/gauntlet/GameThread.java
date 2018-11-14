package gauntlet;

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

	public void run() {
		while (true) {
			
			
			System.out.println("Waiting for client state");
			this.gameState = this.server.readClientState();
			String direction = this.gameState.getDirection();
			System.out.println("Direction is " + direction);
			System.out.println("x: " + this.gameState.getX() + " y: " + this.gameState.getY());
			// Client attempting to move down
			if (direction.equals("do")) {
				if (this.gameState.getRow() < 24) {
					this.server.sendValidMove();
					System.out.println("Send valid move!");
				}
			} 

			// Client attempting to move up
			else if (direction.equals("up")) {
				if (this.gameState.getRow() > 0) {
					this.server.sendValidMove();
					System.out.println("Sent valid move!");
				}
			} 

			// Client attempting to move left
			else if (direction.equals("le")) {
				if (this.gameState.getColumn() > 0) {
					this.server.sendValidMove();
					System.out.println("Sent valid move!");
				}
			} 

			// Client attempting to move right
			else if (direction.equals("ri")) {
				if (this.gameState.getColumn() < 24) {
					this.server.sendValidMove();
					System.out.println("Sent valid move!");
				}
			}

		}
	}
}