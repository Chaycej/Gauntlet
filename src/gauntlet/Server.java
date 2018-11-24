package gauntlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/*
 *  Server
 * 
 * The server communicates to clients through a TCP socket on port 3303. The first person
 * that boots up the game is considered the server, and any player to boot up the game after
 * the server has been configured is considered a client where they send commands asking the server
 * to move, attack, etc. 
 * 
 * When a client client connects to the server, the server spawns a new thread. The newly created thread's
 * purpose is only for handling clients, while the main thread handles the server's own game moves/attacks.
 * 
 * Server responsibilities include:
 * 
 * - Connecting a client to the game.
 * - Listening for new moves.
 * - Listening for a client's attack.
 * - Listening for client's position.
 * - Sending updated game state to the client 
 * 
 */
public class Server {
	
	public static final int PORT = 3303;
	
	public ServerSocket socket;
	public Socket clientSocket;
	public BufferedReader clientStream;
	public DataOutputStream serverStream;
	public Gauntlet gauntlet;
	InetAddress clientAddr;
	
	/*
	 * Server
	 * 
	 * Initializes a server object and opens a UDP socket on port 3303.
	 */
	public Server(Gauntlet gauntlet) {
		try {
			this.socket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.gauntlet = gauntlet;
		System.out.println("DEBUG: Started server");
	}
	
	/*
	 * run
	 * 
	 * Starts the server and listens for the client connection. Once a client connects
	 * the server stops listening for new clients.
	 */
	public void run(GameContainer container, StateBasedGame game, int delta) {
		
		// Listen for client connection
		while (true) {
			try {
				this.clientSocket = this.socket.accept();
				this.clientStream = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
				this.serverStream = new DataOutputStream(this.clientSocket.getOutputStream());
				System.out.println("Client joined game");
				gauntlet.clientThread = new GameThread(this, gauntlet.gameState, container, game, delta);
				gauntlet.clientThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	public void sendGameState(GameState gameState) {
		try {
			ObjectOutputStream output = new ObjectOutputStream(this.clientSocket.getOutputStream());
			output.writeObject(gameState);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * readClientState
	 * 
	 *  Reads a client's state and returns it.
	 */
	public GameState readClientState() {
		try {
			ObjectInputStream in = new ObjectInputStream(this.clientSocket.getInputStream());
			GameState clientState = (GameState)in.readObject();
			return clientState;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * closeServer
	 * 
	 * Kills the server and closes the connection between the client.
	 */
	public void closeServer() {
		try {
			this.clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}