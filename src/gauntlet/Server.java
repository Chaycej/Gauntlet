package gauntlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.SocketException;

/*
 *  Server
 * 
 * The server communicates to clients through a UDP socket on port 3303. The first person
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
 * - Sending acknowledgement commands to the client for whether
 * 	 they are allowed to move or attack.
 * 
 */
public class Server {
	
	public static final String YES_CMD = "1y\n";
	public static final String NO_CMD  = "1n\n";
	public static final int PORT = 3303;
	
	public ServerSocket socket;
	public Socket clientSocket;
	public BufferedReader clientStream;
	public DataOutputStream serverStream;
	InetAddress clientAddr;
	
	/*
	 * Server
	 * 
	 * Initializes a server object and opens a UDP socket on port 3303.
	 */
	public Server() {
		try {
			this.socket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("DEBUG: Started server");
	}
	
	/*
	 * run
	 * 
	 * Starts the server and listens for the client connection. Once a client connects
	 * the server stops listening for new clients.
	 */
	public void run() {
		
		// Listen for client connection
		while (true) {
			try {
				this.clientSocket = this.socket.accept();
				this.clientStream = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
				this.serverStream = new DataOutputStream(this.clientSocket.getOutputStream());
				
				String clientMsg = clientStream.readLine();
				System.out.println(clientMsg);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	/*
	 * sendValidMove
	 * 
	 * Acknowledges the client's next move and sends a command allowing the client to move.
	 */
	public void sendValidMove() {
		try {
			this.serverStream.writeBytes(YES_CMD);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * sendInvalidMove
	 * 
	 * Sends an invalid move command to the client.
	 */
	public void sendInvalidMove(InetAddress clientAddr) {
		try {
			this.serverStream.writeBytes(NO_CMD);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * readClientState
	 * 
	 * Reads a client state. Client packets follow the format
	 * "1p<x pos length><x pos><y pos length><y pos><direciton command length><direction command>		
	 * 
	 * Example:
	 * 	"1p320032002up"
	 * 
	 */
	public GameState readClientState() {
		
		String cmd = null;
		try {
			cmd = this.clientStream.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(cmd);
		
		// Read x,y position
		int index = cmd.charAt(0) - '0' + 1;
		int xLength = cmd.charAt(index) - '0';
		int xPos = Integer.parseInt(cmd.substring(index+1, index + xLength + 1));
		index += xLength + 1;
		System.out.println("X position is " + xPos);
		int yLength = cmd.charAt(index) - '0';
		System.out.println("y length is " + yLength);
		int yPos = Integer.parseInt(cmd.substring(index+1, index + yLength + 1));
		index += yLength + 1;
		
		String direction = cmd.substring(index+1, index + cmd.charAt(index) - '0' + 1);
		
		return new GameState(direction, xPos, yPos);
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