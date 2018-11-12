package gauntlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;

public class Client {
	
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
	 * sendPosition
	 * 
	 * Sends the client's current position.
	 */
	public void sendPosition(int x, int y) {
		
		// Send x coordinate
		int xLength = (int)Math.log10(x) + 1;
		StringBuilder sb = new StringBuilder();
		sb.append(xLength);
		sb.append(x);
		try {
			this.toServer.writeBytes(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Send y coordinate
		int yLength = (int)Math.log10(y) + 1;
		sb = new StringBuilder();
		sb.append(yLength);
		sb.append(y);
		try {
			this.toServer.writeBytes(sb.toString());
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