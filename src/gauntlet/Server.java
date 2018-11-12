package gauntlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
	
	public static final String YES_CMD = "yes";
	public static final String NO_CMD  = "no";
	public static final int PORT = 3303;
	
	public DatagramSocket socket;
	InetAddress clientAddr;
	
	/*
	 * Server
	 * 
	 * Initializes a server object and opens a UDP socket on port 3303.
	 */
	public Server() {
		try {
			this.socket = new DatagramSocket(Server.PORT);
		} catch (SocketException e) {
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
		
		byte[] buf = new byte[256];
		
		// Listen for client connection
		while (true) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				this.socket.receive(packet);
				System.out.println("Client joined the party: " + packet.getAddress().toString() + ": " + packet.getPort());
				break;
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
		String msg = YES_CMD;
		byte[] response = msg.getBytes();
		try {
			this.socket.send(new DatagramPacket(response, response.length, this.clientAddr, PORT));
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
		String msg = NO_CMD;
		byte[] response = msg.getBytes();
		try {
			this.socket.send(new DatagramPacket(response, response.length, clientAddr, PORT));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * readClientMove
	 * 
	 * Reads the next move the client is attempting to take and returns it.
	 * Valid moves are: (up, left, down, right).
	 * 
	 */
	public String readClientMove() {
		byte[] buf = new byte[256];
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		try {
			this.socket.receive(pack);
			this.clientAddr = pack.getAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String cmd = null;
		try {
			cmd = new String(pack.getData(), "UTF-8");
			cmd = cmd.substring(1,  cmd.charAt(0) - '0'+1);
		} catch (UnsupportedEncodingException e) {
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

		// Read new client x coordinate
		byte[] buf = new byte[256];
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		try {
			this.socket.receive(pack);
			this.clientAddr = pack.getAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String cmd = null;
		try {
			cmd = new String(pack.getData(), "UTF-8");
			cmd = cmd.substring(1,  cmd.charAt(0) - '0'+1);
			System.out.println("X packet is " + cmd);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		newPosition[0] = Integer.valueOf(cmd);
		System.out.println("Client x position is: " + newPosition[0]);

		// Read new client y coordinate
		byte[] buf2 = new byte[256];
		pack = new DatagramPacket(buf2, buf2.length);
		try {
			this.socket.receive(pack);
			this.clientAddr = pack.getAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}

		cmd = null;
		try {
			cmd = new String(pack.getData(), "UTF-8");
			cmd = cmd.substring(1,  cmd.charAt(0) - '0'+1);
			System.out.println("Y packet is " + cmd);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

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
		this.socket.close();
	}
}