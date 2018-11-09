package gauntlet;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import jig.ResourceManager;

public class GameStartUp extends BasicGameState{

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		container.setSoundOn(true);
		
		Gauntlet gg = (Gauntlet)game;
		gg.warrior.setPosition(200,200);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Gauntlet bg = (Gauntlet)game;
		
		int x = 16;
		int y = 16;
		for (int row=0; row<25; row++ ) {
			for (int col=0; col<25; col++) {
				if ( bg.map[row][col] == 48) {		//equals a 0 is a path
					bg.mapM[row][col]= new MapMatrix(x,y, 0f, 0f);
					bg.mapM[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.pathTile));
				} else {							//equals a 1 is a wall
					bg.mapM[row][col]= new MapMatrix(x,y, 0f, 0f);
					bg.mapM[row][col].addImageWithBoundingBox(ResourceManager.getImage(Gauntlet.wallTile));
				}
				x = x+32;
				bg.mapM[row][col].render(g);
			}
			y = y+32;
			x=16;
		}
		bg.warrior.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public int getID() {
		return Gauntlet.GAMESTARTSTATE;
	}
}