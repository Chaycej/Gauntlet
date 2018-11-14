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
			GameState state = this.server.readClientState();
			String direction = state.getDirection();
			System.out.println("Direction is " + direction);
			System.out.println("x: " + state.getX() + " y: " + state.getY());
			// Client attempting to move down
			if (direction.equals("do")) {
				if (gameState.getRow() < 24) {
					this.server.sendValidMove();
				}
			} 

			// Client attempting to move up
			else if (direction.equals("up")) {
				if (gameState.getRow() > 0) {
					this.server.sendValidMove();
					System.out.println("Sent valid move!");
				}
			} 

			// Client attempting to move left
			else if (direction.equals("le")) {
				if (gameState.getColumn() > 0) {
					this.server.sendValidMove();
					System.out.println("Sent valid move!");
				}
			} 

			// Client attempting to move right
			else if (direction.equals("ri")) {
				if (gameState.getColumn() < 24) {
					this.server.sendValidMove();
					System.out.println("Sent valid move!");
				}
			}

		}
	}
}