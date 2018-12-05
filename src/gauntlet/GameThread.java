package gauntlet;

import java.net.InetAddress;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class GameThread extends Thread {
	Server server;
	GameState gameState;
	GameContainer container;
	StateBasedGame game;
	Gauntlet gauntlet;
	int delta;
	InetAddress clientAddr;

	public GameThread(Server server, GameState gameState, GameContainer container, StateBasedGame game, int delta) {
		this.server = server;
		this.gameState = gameState;
		this.container = container;
		this.game = game;
		this.delta = delta;
		this.gauntlet = (Gauntlet)game;
	}

	public void run() {
		while (true) {
			GameState clientState = this.server.readClientState();
			this.gameState.setWarriorPosition(clientState.getWarriorX(), clientState.getWarriorY());
			int warriorRow = gameState.getWarriorRow();
			int warriorCol = gameState.getWarriorColumn();
			
			for (int i = 0; i < clientState.skeletons.size(); i++) {
				if (clientState.skeletons.get(i).isDead()) {
					this.gauntlet.skeletonList.get(i).setHealth(-1);
				}
			}
			
			// Client attempting to move down
			int tempCol = -1;
			int tempRow = -1;
			int commandTrue = 0;
			
			
			if (clientState.getWarriorDirection() == GameState.Direction.DOWN) {
				tempRow = (clientState.getWarriorY()-14)/32;
				if (this.gameState.getWarriorY() < (Gauntlet.maxRow * 32) && Gauntlet.map[tempRow+1][warriorCol] == 0) {
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.DOWN);
					commandTrue = 1;
				} 

				if (this.gameState.getWarriorY() < (Gauntlet.maxRow * 32) && warriorRow == Gauntlet.maxRow - 1){
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.DOWN);
					commandTrue = 1;
				}
				
				if (commandTrue == 0) {
					this.gameState.setWarriorDirection(GameState.Direction.DOWN);
					this.gameState.setWarriorMovement(false);
				}
			} 

			// Client attempting to move up
			else if (clientState.getWarriorDirection() == GameState.Direction.UP) {
				tempRow = (clientState.getWarriorY()+14)/32;
				if (this.gameState.getWarriorY() > 34 &&  Gauntlet.map[tempRow-1][warriorCol] == 0) {
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.UP);
					commandTrue = 1;
				}
				
				if (this.gameState.getWarriorY() > 44 && warriorRow == 1) {
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.UP);
					commandTrue = 1;
				}
				
				if(commandTrue == 0) {
					this.gameState.setWarriorDirection(GameState.Direction.UP);
					this.gameState.setWarriorMovement(false);
				}
			} 

			// Client attempting to move left
			else if (clientState.getWarriorDirection() == GameState.Direction.LEFT) {
				tempCol = (clientState.getWarriorX()+15)/32;
				if (this.gameState.getWarriorX() > 34 && Gauntlet.map[warriorRow][tempCol-1] == 0) {
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.LEFT);
					commandTrue = 1;
				}

				if (this.gameState.getWarriorX() > 44 && warriorRow == 1) {
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.LEFT);
					commandTrue = 1;
				}
				
				if(commandTrue == 0) {
					this.gameState.setWarriorDirection(GameState.Direction.LEFT);
					this.gameState.setWarriorMovement(false);
				}
			} 

			// Client attempting to move right
			else if (clientState.getWarriorDirection() == GameState.Direction.RIGHT) {
				tempCol = (clientState.getWarriorX()-15)/32;
				if (this.gameState.getWarriorX() < (Gauntlet.maxColumn * 32) && Gauntlet.map[warriorRow][tempCol+1] == 0) {
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.RIGHT);
					commandTrue = 1;
				}

				if (this.gameState.getWarriorX() < (Gauntlet.maxColumn * 32) && warriorRow == Gauntlet.maxColumn-1) {
					this.gameState.setWarriorMovement(true);
					this.gameState.setWarriorDirection(GameState.Direction.RIGHT);
					commandTrue = 1;
				}
				
				if (commandTrue == 0) {
					this.gameState.setWarriorDirection(GameState.Direction.RIGHT);
					this.gameState.setWarriorMovement(false);
				}
			} 
			
			else if (clientState.getWarriorDirection() == GameState.Direction.STOP) {
				this.gameState.setWarriorDirection(GameState.Direction.STOP);
				this.gameState.setWarriorMovement(false);
			}
			
			commandTrue = 0;
			
			// Update client's position and projectiles
			this.gameState.setWarriorPosition(clientState.getWarriorX(), clientState.getWarriorY());
			this.gameState.warriorProjectiles = clientState.warriorProjectiles;
			this.server.sendGameState(this.gameState);
		}
	}
}