package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Gauntlet extends StateBasedGame {
	public static final int LOBBYSTATE = 0;
	public static final int GAMESTARTSTATE = 1;
	
	public static final String pathTile = "Gauntlet/resources/WalkingTile.png";
	public static final String wallTile = "Gauntlet/resources/WallTile.png";
	public static final String JOIN_GAME_RSC = "Gauntlet/resources/joinGame.png";
	public static final String HOST_GAME_RSC = "Gauntlet/resources/hostGame.png";

	public final int ScreenWidth;
	public final int ScreenHeight;
	
	public static AppGameContainer app;
	
	public GameSocket socket;
	
	int[][] map;
	MapMatrix[][] mapM;
	
	Server server;
	Client client;
	
	
	public Gauntlet(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		map = new int[75][75];
		mapM = new MapMatrix[75][75];
		
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new LobbyState());
		//addState(new GameStartUp());
		
		ResourceManager.loadImage(wallTile);
		ResourceManager.loadImage(pathTile);
		ResourceManager.loadImage(JOIN_GAME_RSC);
		ResourceManager.loadImage(HOST_GAME_RSC);
		
	}
	
	public static void main(String[] args) {
		try {
			app = new AppGameContainer(new Gauntlet("Gauntlet", 800, 800));		//(x,y)
			app.setDisplayMode(700, 700, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
