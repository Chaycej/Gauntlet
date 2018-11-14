package gauntlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;

public class Client {
	
	/*
	 *  Client commands
	 */
	public static final String POS_CMD = "1p";
	public static final String UP_CMD = "2up\n";
	public static final String DOWN_CMD = "2do\n";
	public static final String RIGHT_CMD = "2ri\n";
	public static final String LEFT_CMD = "2le\n";
	
	public static final int PORT = 3303;
	public InetAddress serverAddress;
	public Socket socket;
	public BufferedReader fromServer;
	public DataOutputStream toServer;
	
	/*
	 * Client
	 * 
	 * Creates a client by opening a UDP socket on port 3303.
	 */
	public Client(String serverIP) {
		try {
			this.socket = new Socket(serverIP, PORT);
			this.fromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.toServer = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * joinServer
	 * 
	 * Attempts to join the server using the server's address.
	 */
	public void joinServer() {
		String msg = "Join\n";
		
		try {
			this.toServer.writeBytes(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * sendCommand
	 * 
	 * Sends a command the server asking to move/attack.
	 */
	public void sendCommand(String command) {
		try {
			this.toServer.writeBytes(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * readServerResponse
	 * 
	 * Reads a server response after the client attempts to make a move, and
	 * return the response.
	 */
	public String readServerResponse(Gauntlet gg) {
		String cmd = null;
		try {
			cmd = this.fromServer.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		cmd = cmd.substring(1, cmd.charAt(0) - '0'+1);
		return cmd;
	}
	
	public void sendMovement(String cmd, Gauntlet gauntlet) {
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append(this.POS_CMD);
		int xPos = (int)gauntlet.warrior.getX();
		int xLength = (int)Math.log10(xPos) + 1;
		cmdBuilder.append(xLength);
		cmdBuilder.append(xPos);
		int yPos = (int)gauntlet.warrior.getY();
		int yLength = (int)Math.log10(yPos) + 1;
		cmdBuilder.append(yLength);
		cmdBuilder.append(yPos);
		cmdBuilder.append(cmd);
		gauntlet.client.sendCommand(cmdBuilder.toString());
	}
	
	/*
	 * closeClient
	 * 
	 * Kills the client by closing the UDP socket.
	 */
	public void closeClient() {
		try {
			this.toServer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}