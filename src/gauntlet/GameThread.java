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
			
			
			String cmd = this.server.readClientCommand();
			System.out.println("Cmd is " + cmd);
			
			if (cmd.equals("pos")) {
				int[] position = this.server.readClientPosition();
				this.gameState.updatePosition(position[0], position[1]);
			}

			// Client attempting to move down
			if (cmd.equals("down")) {
				if (gameState.getRow() < 24) {
					this.server.sendValidMove();
				}
			} 

			// Client attempting to move up
			else if (cmd.equals("up")) {
				if (gameState.getRow() > 0) {
					this.server.sendValidMove();
				}
			} 

			// Client attempting to move left
			else if (cmd.equals("left")) {
				if (gameState.getColumn() > 0) {
					this.server.sendValidMove();
				}
			} 

			// Client attempting to move right
			else if (cmd.equals("right")) {
				if (gameState.getColumn() < 24) {
					this.server.sendValidMove();
					System.out.println("Send valid move!");
				}
			}

		}
	}
}