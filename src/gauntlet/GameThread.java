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
			
			GameState clientState = this.server.readClientState();
			
			this.gameState.setWarriorPosition(clientState.getWarriorX(), clientState.getWarriorY());
			
			// Client attempting to move down
			if (clientState.getWarriorDirection() == GameState.Direction.DOWN) {
				if (this.gameState.getWarriorY() < 24 * 32 - 29) {
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.DOWN);
				} else {
					this.gameState.setWarriorMovement(false);
				}
			} 

			// Client attempting to move up
			else if (clientState.getWarriorDirection() == GameState.Direction.UP) {
				if (this.gameState.getWarriorY() > 40) {
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.UP);
				} else {
					this.gameState.setWarriorMovement(false);
				}
			} 

			// Client attempting to move left
			else if (clientState.getWarriorDirection() == GameState.Direction.LEFT) {
				if (this.gameState.getWarriorX() > 40) {
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.LEFT);
				} else {
					this.gameState.setWarriorMovement(false);
				}
			} 

			// Client attempting to move right
			else if (clientState.getWarriorDirection() == GameState.Direction.RIGHT) {
				if (this.gameState.getWarriorX() < 24 * 32 - 29) {
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.RIGHT);
				} else {
					this.gameState.setWarriorMovement(false);
				}
			}
			
			else if (clientState.getWarriorDirection() == GameState.Direction.STOP) {
				this.gameState.setWarriorDirection(GameState.Direction.STOP);
				this.gameState.setWarriorMovement(false);
			}
			
			// Update client's position
			this.gameState.setWarriorPosition(clientState.getWarriorX(), clientState.getWarriorY());
			
			this.server.sendGameState(this.gameState);
		}
	}
}