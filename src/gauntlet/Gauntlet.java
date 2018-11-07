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
	
	public static final String pathTile = "Gauntlet/resources/WalkingTile.png";
	public static final String wallTile = "Gauntlet/resources/WallTile.png";
	public static final String JOIN_GAME_RSC = "Gauntlet/resources/joinGame.png";
	public static final String HOST_GAME_RSC = "Gauntlet/resources/hostGame.png";

	public final int ScreenWidth;
	public final int ScreenHeight;
	
	public static AppGameContainer app;
	
	public static boolean isServer;
	public static int partySize;
	public static String serverIP;
	
	int[][] map;
	MapMatrix[][] mapM;
	
	JoinButton joinButton;
	ServerButton serverButton;
	
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
		addState(new GameStartUp());
		
		ResourceManager.loadImage(wallTile);
		ResourceManager.loadImage(pathTile);
		ResourceManager.loadImage(JOIN_GAME_RSC);
		ResourceManager.loadImage(HOST_GAME_RSC);
		
		/* To read one character at a time from map.txt this code came from 
		https://stackoverflow.com/questions/811851/how-do-i-read-input-character-by-character-in-java */
		int rowB = 0;
		int colB = 0;
		try {
			FileInputStream inputStream = new FileInputStream("../GauntletGame/src/gauntlet/map.txt");
			while (inputStream.available() > 0) {
	            int numRead = inputStream.read();
	            if (!Character.isDigit(numRead)){
	            	continue;
	            }
	            //System.out.println("number is " + (char) numRead);
	            //System.out.println("col " + colB);
	            //System.out.println("row " + rowB);
	            map[rowB][colB] = numRead;
	            colB++;
	            if (colB == 75) {
	            	colB = 0;
	            	rowB++;
	            }
	        }
	        inputStream.close();
	    } catch (IOException ioe) {
	        System.out.println("Trouble reading from the file: " + ioe.getMessage());
	    }
		
		joinButton = new JoinButton(350, 300);
		serverButton = new ServerButton(350, 480);
	}
	
	public static void main(String[] args) {
		try {
			app = new AppGameContainer(new Gauntlet("Gauntlet", 2400, 2600));		//(x,y)
			app.setDisplayMode(700, 700, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
