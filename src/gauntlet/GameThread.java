package gauntlet;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class GameThread extends Thread {
	
	public GameThread() {}
	
	public void run(GameContainer container, StateBasedGame game, int delta) {
		System.out.println("Started a new thread for client!");
		
		Gauntlet bg = (Gauntlet)game;
		
		Graphics g = container.getGraphics();
		bg.ranger.render(g);
	}
}
