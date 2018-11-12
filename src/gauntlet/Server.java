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
	
	public static final String YES_CMD = "3yes\n";
	public static final String NO_CMD  = "2no\n";
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
				System.out.println("Listening for client");
				this.clientSocket = this.socket.accept();
				System.out.println("Client connected");
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
	 * readClientCommand
	 * 
	 * Reads a client command. All commands contain the length of the string command
	 * as the first byte, and the command as the rest of the bytes.
	 * 
	 * Example:
	 * 	"4down"
	 * 
	 */
	public String readClientCommand() {
		String cmd = null;
		try {
			cmd = this.clientStream.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cmd;
	}
	
	/*
	 * readClientPosition
	 * 
	 * Reads the client's updated x,y coordinates and returns them as an array.
	 * 
	 * [0] -> x coordinate
	 * [1] -> y coordinate
	 */
	public int[] readClientPosition() {

		int[] newPosition = new int[2];

		String cmd = null;
		try {
			cmd = this.clientStream.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		cmd = cmd.substring(1, cmd.charAt(0) - '0'+1);
		newPosition[0] = Integer.valueOf(cmd);
		System.out.println("Client x position is: " + newPosition[0]);

		// Read new client y coordinate
		cmd = null;
		try {
			cmd = this.clientStream.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		cmd = cmd.substring(1, cmd.charAt(0) - '0'+1);
		newPosition[1] = Integer.valueOf(cmd);
		System.out.println("Client y position is: " + newPosition[1]);
		return newPosition;
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