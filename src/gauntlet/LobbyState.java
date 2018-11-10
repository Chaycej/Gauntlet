package gauntlet;

import java.awt.Font;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LobbyState extends BasicGameState{
	TextField tf;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		container.setSoundOn(true);
		
		Font font = new Font("Arial", Font.PLAIN, 20);
		UnicodeFont uc = new UnicodeFont(font);
		uc.addAsciiGlyphs();
	    uc.addGlyphs(400,600);
	    uc.getEffects().add(new ColorEffect(java.awt.Color.white));
	    	try {
	    		uc.loadGlyphs();
	    } catch (SlickException e) {};
		tf = new TextField(container, uc, 275, 300, 175, 50, new ComponentListener() {
			public void componentActivated(AbstractComponent source) {
	            tf.setFocus(true);
	         }
		});
		tf.setBackgroundColor(Color.white);
		tf.setTextColor(Color.black);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//Gauntlet gg = (Gauntlet)game;
		g.drawString("Press space to start a server", 250, 100);
		g.drawString("OR", 250, 140);
		g.drawString("Type in a server ip address and press enter", 250, 200);
		tf.render(container, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		Gauntlet gg = (Gauntlet) game;
		Input input = container.getInput();

		// Start server
		if (input.isKeyDown(Input.KEY_SPACE)) {
			if (gg.server == null) {
				gg.server = new Server();
				gg.server.run();
				gg.clientThread = new GameThread(gg.server,container, game, delta);
				gg.clientThread.start();
				gg.enterState(Gauntlet.GAMESTARTSTATE);
			}
		} 
		
		// Join a server
		if (input.isKeyDown(Input.KEY_ENTER)) {
			if (gg.client == null) {
				gg.client = new Client(tf.getText());
				gg.client.joinServer();
				gg.enterState(Gauntlet.GAMESTARTSTATE);
			}
		}
	}

	@Override
	public int getID() {
		return Gauntlet.LOBBYSTATE;
	}
}

