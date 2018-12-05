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

	public final static int maxRow = 25;
	public final static int maxColumn = 25;
	public final static int windowWidth = 800;
	public final static int windowHeight = 800;
	
	public final int  warriorX= 100;
	public final int  warriorY= 750;
	public final int  rangerX= 150;
	public final int  rangerY= 750;
	public final int  skeletonX= 300;
	public final int  skeletonY= 300;
	
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
	
	public static final String DoorOpen = "gauntlet/resources/DoorOpen.png";
	public static final String DoorClosed = "gauntlet/resources/DoorClosed.png";
	public static final String DoorODown = "gauntlet/resources/DoorOpenDown.png";
	public static final String DoorCDown = "gauntlet/resources/DoorClosedDown.png";
	public static final String DoorORight = "gauntlet/resources/DoorOpenRight.png";
	public static final String DoorCRight = "gauntlet/resources/DoorClosedRight.png";
	public static final String DoorCLeft = "gauntlet/resources/DoorClosedLeft.png";
	public static final String DoorOLeft = "gauntlet/resources/DoorOpenLeft.png";

	public final int ScreenWidth;
	public final int ScreenHeight;
	
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
	Vector<Projectile> warriorProjectiles;
	Vector<Projectile> rangerProjectiles;
	ArrayList<Skeleton> skeletonList;

	
	public Gauntlet(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

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
		
		ResourceManager.loadImage(DoorOpen);
		ResourceManager.loadImage(DoorClosed);
		ResourceManager.loadImage(DoorORight);
		ResourceManager.loadImage(DoorCRight);
		ResourceManager.loadImage(DoorOLeft);
		ResourceManager.loadImage(DoorCLeft);
		ResourceManager.loadImage(DoorODown);
		ResourceManager.loadImage(DoorCDown);
		
		warrior = new Warrior(warriorX, warriorY, 0f, 0f);
		ranger = new Ranger(rangerX, rangerY, 0f, 0f);
		skeleton = new Skeleton(skeletonX, skeletonY, 0f, 0f);
		
		warriorProjectiles = new Vector<>();
		rangerProjectiles = new Vector<>();
		skeletonList = new ArrayList<Skeleton>();
		skeletonList.add(skeleton);
		
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
				if ( map[row][col] == 48) {		//equals a 0 is a path
					map[row][col] = 0;
				} else {
					map[row][col] = 1;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			app = new AppGameContainer(new Gauntlet("Gauntlet", windowWidth, windowHeight));		//(x,y)
			app.setDisplayMode(windowWidth, windowHeight, false);
			app.setClearEachFrame(false);
			app.setTargetFrameRate(35);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}