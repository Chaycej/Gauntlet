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
	public static final int LOSEGAME = 2;
	public static final int WINGAME = 3;

	public final static int maxRow = 75;
	public final static int maxColumn = 75;
	public final static int windowWidth = 800;
	public final static int windowHeight = 750;
	
	public final int  warriorSpawnX= 64;
	public final int  warriorSpawnY= 128;
	public final int  rangerSpawnX= 128;
	public final int  rangerSpawnY= 128;
	
	public final int  key1X= 2080;
	public final int  key1Y= 1346;
	
	public final int  key2X= 835;
	public final int  key2Y= 1410;
	
	public final int  key3X= 85;
	public final int  key3Y= 2360;
	
//	public final int  treasureX= 1980;
//	public final int  treasureY= 1980;
	public final int  treasureX= 210;
	public final int  treasureY= 210;
	
	public int lives = 1;
	
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
	
	public static final String KeyHDown = "gauntlet/resources/KeyHDown.png";
	
	public static final String winPic = "gauntlet/resources/Win.png";
	public static final String losePic = "gauntlet/resources/Lose.png";

	public static final String LowerHealthPotion = "gauntlet/resources/LowerHealthPotion.png";
	public static final String HealthPotion = "gauntlet/resources/HealthPotion.png";
	public static final String HigherHealthPotion = "gauntlet/resources/HigherHealthPotion.png";
	public static final String IncreaseHealth = "gauntlet/resources/IncreaseHealth.png";
	public static final String IncreaseFire = "gauntlet/resources/IncreaseFireRate.png";

	
	public static final String doorHRight = "gauntlet/resources/RightHDoor.png";
	public static final String doorHLeft = "gauntlet/resources/LeftHDoor.png";
	public static final String doorVTop = "gauntlet/resources/TopVDoor.png";
	public static final String doorVBottom = "gauntlet/resources/BottomVDoor.png";
	
	public static final String treasureChest = "gauntlet/resources/chest.png";


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
	
	Treasure treasure;
	
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
		addState(new LoseGame());
		addState(new WinGame());
		
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
	
		ResourceManager.loadImage(KeyHDown);
		ResourceManager.loadImage(winPic);
		ResourceManager.loadImage(losePic);
		
		ResourceManager.loadImage(HigherHealthPotion);
		ResourceManager.loadImage(HealthPotion);
		ResourceManager.loadImage(LowerHealthPotion);
		ResourceManager.loadImage(IncreaseHealth);
		ResourceManager.loadImage(IncreaseFire);
		
		ResourceManager.loadImage(doorHRight);
		ResourceManager.loadImage(doorHLeft);
		ResourceManager.loadImage(doorVTop);
		ResourceManager.loadImage(doorVBottom);
		
		ResourceManager.loadImage(treasureChest);
		
		warrior = new Warrior(warriorSpawnX, warriorSpawnY, 0f, 0f);
		ranger = new Ranger(rangerSpawnX, rangerSpawnY, 0f, 0f);
		
		key1 = new Keys(key1X, key1Y, 0f, 0f);
		key2 = new Keys(key2X, key2Y, 0f, 0f);
		key3 = new Keys(key3X, key3Y, 0f, 0f);
		
		treasure = new Treasure(treasureX,treasureY,0f,0f);
		
		warriorProjectiles = new Vector<>();
		rangerProjectiles = new Vector<>();
		
		//Cameras start centered so the characters are on the center of the viewing screen.
		warriorCamera = new Camera(ScreenWidth/2,ScreenHeight/2);
		rangerCamera = new Camera(ScreenWidth/2,ScreenHeight/2);
		
		skeletonList = new ArrayList<Skeleton>();
			
		skeletonList.add(new Skeleton(300, 300, 0f, 0f));
		skeletonList.add(new Skeleton(500, 500, 0f, 0f));
		skeletonList.add(new Skeleton(800, 500, 0f, 0f));
		skeletonList.add(new Skeleton(300, 800, 0f, 0f));
		skeletonList.add(new Skeleton(300, 2000, 0f, 0f));
		skeletonList.add(new Skeleton(600, 2000, 0f, 0f));
				
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
				if (map[row][col] == 53) {	
					map[row][col] = 5;			//equals a 4 is a door facing east  
				}
				if (map[row][col] == 54) {	
					map[row][col] = 6;			//equals a 4 is a door facing east  
				}
				if (map[row][col] == 55) {	
					map[row][col] = 7;			//equals a 4 is a door facing east  
				}
			}
		}
		
		potions = new ArrayList<Powerups>();
		potions.add(new Powerups(1056, 160, Powerups.PowerupType.lower));
		potions.add(new Powerups(2368, 64, Powerups.PowerupType.lower));
		potions.add(new Powerups(64, 672, Powerups.PowerupType.lower));
		
		potions.add(new Powerups(992, 928, Powerups.PowerupType.normal));
		potions.add(new Powerups(768, 2176, Powerups.PowerupType.normal));
		potions.add(new Powerups(1568, 2016, Powerups.PowerupType.max));

		potions.add(new Powerups(448, 1760, Powerups.PowerupType.maxPlus));
		potions.add(new Powerups(544, 1760, Powerups.PowerupType.maxPlus));
		potions.add(new Powerups(1300, 925, Powerups.PowerupType.fireRatePlus));
		potions.add(new Powerups(64, 2368, Powerups.PowerupType.fireRatePlus));

	}
	
	public static void main(String[] args) {
		try {
			app = new AppGameContainer(new Gauntlet("Gauntlet", windowWidth, windowHeight));		//(x,y)
			app.setDisplayMode(windowWidth, windowHeight, false);
			app.setClearEachFrame(false);
			app.setTargetFrameRate(45);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}