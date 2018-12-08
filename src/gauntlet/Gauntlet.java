package gauntlet;

import jig.Entity;
import jig.ResourceManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Gauntlet extends StateBasedGame {
	public static final int LOBBYSTATE = 0;
	public static final int GAMESTARTSTATE = 1;

	public final static int maxRow = 75;
	public final static int maxColumn = 75;
	public final static int windowWidth = 800;
	public final static int windowHeight = 800;
	
	public final int  warriorSpawnX= 64;
	public final int  warriorSpawnY= 128;
	public final int  rangerSpawnX= 128;
	public final int  rangerSpawnY= 128;
	
	public final int  key1X= 2080;
	public final int  key1Y= 1346;
	
	public final int  key2X= 835;
	public final int  key2Y= 1410;
	
	public final int  key3X= 75;
	public final int  key3Y= 2360;
	
	public static final String pathTile = "gauntlet/resources/WalkingTile.png";
	public static final String wallTile = "gauntlet/resources/WallTile.png";
	public static final String JOIN_GAME_RSC = "gauntlet/resources/joinGame.png";
	public static final String HOST_GAME_RSC = "gauntlet/resources/hostGame.png";
	
	public static final String rangerN = "gauntlet/resources/ranger_n.png";
	public static final String rangerS = "gauntlet/resources/ranger_s.png";
	public static final String rangerE = "gauntlet/resources/ranger_e.png";
	public static final String rangerW = "gauntlet/resources/ranger_w.png";
	
	public static final String warriorN = "gauntlet/resources/warrior_n.png";
	public static final String warriorS = "gauntlet/resources/warrior_s.png";
	public static final String warriorE = "gauntlet/resources/warrior_e.png";
	public static final String warriorW = "gauntlet/resources/warrior_w.png";
	public static final String arrowN = "gauntlet/resources/Arrow_N.png";
	public static final String arrowE = "gauntlet/resources/Arrow_E.png";
	public static final String arrowS = "gauntlet/resources/Arrow_S.png";
	public static final String arrowW = "gauntlet/resources/Arrow_W.png";
	
	public static final String skeletonN = "gauntlet/resources/skeletonN.png";
	public static final String skeletonS = "gauntlet/resources/skeletonS.png";
	public static final String skeletonE = "gauntlet/resources/skeletonE.png";
	public static final String skeletonW = "gauntlet/resources/skeletonW.png";
	
	public static final String LobbyPic = "gauntlet/resources/LobbyPic.png";
	
	public static final String KeyHUp = "gauntlet/resources/KeyHUp.png";
	public static final String KeyHDown = "gauntlet/resources/KeyHDown.png";
	public static final String KeyVLeft = "gauntlet/resources/KeyVLeft.png";
	public static final String KeyVRight = "gauntlet/resources/KeyVRight.png";

	public static final String LowerHealthPotion = "gauntlet/resources/LowerHealthPotion.png";
	public static final String HealthPotion = "gauntlet/resources/HealthPotion.png";
	public static final String HigherHealthPotion = "gauntlet/resources/HigherHealthPotion.png";

	public static final String doorCNorth = "gauntlet/resources/doorCNorth.png";
	public static final String doorCSouth = "gauntlet/resources/doorCSouth.png";
	public static final String doorCEast = "gauntlet/resources/doorCEast.png";
	public static final String doorCWest = "gauntlet/resources/doorCWest.png";
	public static final String doorONorth = "gauntlet/resources/doorONorth.png";
	public static final String doorOSouth = "gauntlet/resources/doorOSouth.png";
	public static final String doorOEast = "gauntlet/resources/doorOEast.png";
	public static final String doorOWest = "gauntlet/resources/doorOWest.png";


	public final int ScreenWidth;
	public final int ScreenHeight; 
	
	
	public final float clientCamX;
	public final float clientCamY;
	
	public static AppGameContainer app;
	
	static int[][] map;
	MapMatrix[][] mapMatrix;
	Warrior warrior;
	Ranger ranger;
	Skeleton skeleton;
	Server server;
	Client client;
	GameThread clientThread;
	GameState gameState;
	Keys key1;
	Keys key2;
	Keys key3;
	
	Vector<Projectile> warriorProjectiles;
	Vector<Projectile> rangerProjectiles;
	ArrayList<Skeleton> skeletonList;
    ArrayList<Powerups> potions;

	
	//Camera class determines what the player is looking at.
	Camera warriorCamera;
    Camera rangerCamera;
	
	public Gauntlet(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		clientCamX = 0;
		clientCamY = 0;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		map = new int[maxRow][maxColumn];
		mapMatrix = new MapMatrix[maxRow][maxColumn];
		gameState = new GameState();
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new LobbyState());
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
		
		ResourceManager.loadImage(arrowN);
		ResourceManager.loadImage(arrowE);
		ResourceManager.loadImage(arrowS);
		ResourceManager.loadImage(arrowW);

		ResourceManager.loadImage(skeletonN);
		ResourceManager.loadImage(skeletonS);
		ResourceManager.loadImage(skeletonE);
		ResourceManager.loadImage(skeletonW);
		
		ResourceManager.loadImage(LobbyPic);
		ResourceManager.loadImage(KeyHUp);
		ResourceManager.loadImage(KeyHDown);
		ResourceManager.loadImage(KeyVRight);
		ResourceManager.loadImage(KeyVLeft);
		
		ResourceManager.loadImage(HigherHealthPotion);
		ResourceManager.loadImage(HealthPotion);
		ResourceManager.loadImage(LowerHealthPotion);
		
		ResourceManager.loadImage(doorCNorth);
		ResourceManager.loadImage(doorCSouth);
		ResourceManager.loadImage(doorCEast);
		ResourceManager.loadImage(doorCWest);
		ResourceManager.loadImage(doorONorth);
		ResourceManager.loadImage(doorOSouth);
		ResourceManager.loadImage(doorOEast);
		ResourceManager.loadImage(doorOWest);
		
		warrior = new Warrior(warriorSpawnX, warriorSpawnY, 0f, 0f);
		ranger = new Ranger(rangerSpawnX, rangerSpawnY, 0f, 0f);
		
		key1 = new Keys(key1X, key1Y, 0f, 0f);
		key2 = new Keys(key2X, key2Y, 0f, 0f);
		key3 = new Keys(key3X, key3Y, 0f, 0f);
		
		warriorProjectiles = new Vector<>();
		rangerProjectiles = new Vector<>();
		
		//Cameras start centered so the characters are on the center of the viewing screen.
		warriorCamera = new Camera(ScreenWidth/2,ScreenHeight/2);
		rangerCamera = new Camera(ScreenWidth/2,ScreenHeight/2);
		
		skeletonList = new ArrayList<Skeleton>();
		
		potions = new ArrayList<Powerups>();
	
		skeletonList.add(new Skeleton(300, 300, 0f, 0f));
		skeletonList.add(new Skeleton(500, 500, 0f, 0f));
		
		int rowB = 0;
        int colB = 0;
        try {
            FileInputStream inputStream = new FileInputStream("../Gauntlet/src/gauntlet/map2.txt");
            while (inputStream.available() > 0) {
                int numRead = inputStream.read();
                if (!Character.isDigit(numRead)){
                    continue;
                }
                map[rowB][colB] = numRead;
                colB++;
                if (colB == maxColumn) {
                    colB = 0;
                    rowB++;
                }
            }
            inputStream.close();
        } catch (IOException ioe) {
            System.out.println("Trouble reading from the file: " + ioe.getMessage());
        }
		for (int row=0; row<maxRow; row++ ) {
			for (int col=0; col<maxColumn; col++) {
				if (map[row][col] == 48) {		//equals a 0 is a path
					map[row][col] = 0;
				}
				if (map[row][col] == 49) {		//equals a 1 is a wall
					map[row][col] = 1;
				}
				if (map[row][col] == 50) {	
					map[row][col] = 2;			//equals a 2 is a door facing south  
				}
				if (map[row][col] == 51) {	
					map[row][col] = 3;			//equals a 3 is a door facing west  
				}
				if (map[row][col] == 52) {	
					map[row][col] = 4;			//equals a 4 is a door facing east  
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			app = new AppGameContainer(new Gauntlet("Gauntlet", windowWidth, windowHeight));		//(x,y)
			app.setDisplayMode(1500, 1500, false);
			app.setClearEachFrame(false);
			app.setTargetFrameRate(35);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}