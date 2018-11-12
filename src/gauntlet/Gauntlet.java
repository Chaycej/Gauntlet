package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import java.io.FileInputStream;
import java.io.IOException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Gauntlet extends StateBasedGame {
	public static final int LOBBYSTATE = 0;
	public static final int GAMESTARTSTATE = 1;

	public final static int row = 25;
	public final static int col = 25;
	public final static int windowWidth = 800;
	public final static int windowHeight = 800;
	
	public final int  warriorX= 200;
	public final int  warriorY= 200;
	public final int  rangerX= 280;
	public final int  rangerY= 200;
	public final int  skeletonX= 500;
	public final int  skeletonY= 300;
	
	public static final String pathTile = "Gauntlet/resources/WalkingTile.png";
	public static final String wallTile = "Gauntlet/resources/WallTile.png";
	public static final String JOIN_GAME_RSC = "Gauntlet/resources/joinGame.png";
	public static final String HOST_GAME_RSC = "Gauntlet/resources/hostGame.png";
	
	public static final String rangerN = "Gauntlet/resources/ranger_n.png";
	public static final String rangerS = "Gauntlet/resources/ranger_s.png";
	public static final String rangerE = "Gauntlet/resources/ranger_e.png";
	public static final String rangerW = "Gauntlet/resources/ranger_w.png";
	
	public static final String warriorN = "Gauntlet/resources/warrior_n.png";
	public static final String warriorS = "Gauntlet/resources/warrior_s.png";
	public static final String warriorE = "Gauntlet/resources/warrior_e.png";
	public static final String warriorW = "Gauntlet/resources/warrior_w.png";
	
	public static final String skeletonN = "Gauntlet/resources/skeletonN.png";
	public static final String skeletonS = "Gauntlet/resources/skeletonS.png";
	public static final String skeletonE = "Gauntlet/resources/skeletonE.png";
	public static final String skeletonW = "Gauntlet/resources/skeletonW.png";

	public final int ScreenWidth;
	public final int ScreenHeight;
	
	public static AppGameContainer app;
	
	public GameSocket socket;
	
	int[][] map;
	MapMatrix[][] mapMatrix;
	Warrior warrior;
	Ranger ranger;
	Skeleton skeleton;
	Server server;
	Client client;
	GameThread clientThread;
	
	public Gauntlet(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		map = new int[row][col];
		mapMatrix = new MapMatrix[row][col];
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		//addState(new LobbyState());
		addState(new GameStartUp());
		
		ResourceManager.loadImage(wallTile);
		ResourceManager.loadImage(pathTile);
		ResourceManager.loadImage(JOIN_GAME_RSC);
		ResourceManager.loadImage(HOST_GAME_RSC);
		
		ResourceManager.loadImage(rangerN);
		ResourceManager.loadImage(rangerS);
		ResourceManager.loadImage(rangerE);
		ResourceManager.loadImage(rangerW);
		
		ResourceManager.loadImage(warriorN);
		ResourceManager.loadImage(warriorS);
		ResourceManager.loadImage(warriorE);
		ResourceManager.loadImage(warriorW);
		
		ResourceManager.loadImage(skeletonN);
		ResourceManager.loadImage(skeletonS);
		ResourceManager.loadImage(skeletonE);
		ResourceManager.loadImage(skeletonW);
		
		warrior = new Warrior(warriorX, warriorY, 0f, 0f);
		ranger = new Ranger(rangerX, rangerY, 0f, 0f);
		skeleton = new Skeleton(skeletonX, skeletonY, 0f, 0f);
		
		int rowB = 0;
        int colB = 0;
        try {
            FileInputStream inputStream = new FileInputStream("../Gauntlet/src/gauntlet/map.txt");
            while (inputStream.available() > 0) {
                int numRead = inputStream.read();
                if (!Character.isDigit(numRead)){
                    continue;
                }
                map[rowB][colB] = numRead;
                colB++;
                if (colB == col) {
                    colB = 0;
                    rowB++;
                }
            }
            inputStream.close();
        } catch (IOException ioe) {
            System.out.println("Trouble reading from the file: " + ioe.getMessage());
        }
	}
	
	public static void main(String[] args) {
		try {
			app = new AppGameContainer(new Gauntlet("Gauntlet", windowWidth, windowHeight));		//(x,y)
			app.setDisplayMode(windowWidth, windowHeight, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}