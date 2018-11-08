package gauntlet;

import java.awt.Font;
import java.io.IOException;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LobbyState extends BasicGameState{

	TextField tf;
	
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
	            System.out.println("DEBUG: "+tf.getText());
	            tf.setFocus(true);
	         }
		});
		tf.setBackgroundColor(Color.white);
		tf.setTextColor(Color.black);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Gauntlet bg = (Gauntlet)game;
		
		g.drawString("Press space to start a server", 250, 100);
		g.drawString("OR", 250, 140);
		g.drawString("Type in a server ip address and press enter", 250, 200);
		tf.render(container, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		Gauntlet bg = (Gauntlet) game;
		Input input = container.getInput();

		
		// Start server
		if (input.isKeyDown(input.KEY_SPACE)) {
			GameSocket socket = null;
			try {
				socket = new GameSocket();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			socket.createServer();
			socket.runServer();
			
		} 
		
		// Join a server
		if (input.isKeyDown(input.KEY_ENTER)) {
			
			System.out.println("IP address: " + tf.getText());
			GameSocket socket = null;
			try {
				socket = new GameSocket();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			socket.createClient(tf.getText());
		}
		
	}

	@Override
	public int getID() {
		return Gauntlet.LOBBYSTATE;
	}
}
