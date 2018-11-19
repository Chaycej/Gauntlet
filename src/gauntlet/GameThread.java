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
			
			this.server.readClientState(this.gameState);
			GameState.Direction direction = this.gameState.getWarriorDirection();
			
			// Client attempting to move down
			if (direction == GameState.Direction.DOWN) {
				if (this.gameState.getWarriorY() < 24 * 32 - 29) {
					this.gameState.setWarriorMovement(true);
				} else {
					this.gameState.setWarriorMovement(false);
				}
			} 

			// Client attempting to move up
			else if (direction == GameState.Direction.UP) {
				if (this.gameState.getWarriorY() > 40) {
					this.gameState.setWarriorMovement(true);
				} else {
					this.gameState.setWarriorMovement(false);
				}
			} 

			// Client attempting to move left
			else if (direction == GameState.Direction.LEFT) {
				if (this.gameState.getWarriorX() > 40) {
					this.gameState.setWarriorMovement(true);
				} else {
					this.gameState.setWarriorMovement(false);
				}
			} 

			// Client attempting to move right
			else if (direction == GameState.Direction.RIGHT) {
				if (this.gameState.getWarriorX() < 24 * 32 - 29) {
					this.gameState.setWarriorMovement(true);
				} else {
					this.gameState.setWarriorMovement(false);
				}
			}
		}
	}
}