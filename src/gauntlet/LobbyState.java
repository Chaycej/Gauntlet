package gauntlet;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import jig.ResourceManager;

public class LobbyState extends BasicGameState{

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		container.setSoundOn(true);
		
		Gauntlet.app.setDisplayMode(500, 500, false);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Gauntlet bg = (Gauntlet)game;
		
		g.drawString("Gauntlet lobby", 290, 10);
		
		g.drawString("Press space to join a party", 250, 200);
		bg.joinButton.render(g);
		g.drawString("Press M to start a party", 250, 400);
		bg.serverButton.render(g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		Gauntlet bg = (Gauntlet) game;
		Input input = container.getInput();

		
		// Join a party
		if (input.isKeyDown(input.KEY_SPACE)) {
			
			
			
		} else if (input.isKeyDown(input.KEY_M)) { // Host a party
			
		}
		
	}

	@Override
	public int getID() {
		return Gauntlet.GAMESTARTSTATE;
	}
}