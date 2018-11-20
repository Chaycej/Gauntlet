package gauntlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;

public class Client {
	
	public static final String FIRE_CMD = "1fi\n";
	
	public static final int PORT = 3303;
	public InetAddress serverAddress;
	public Socket socket;
	public BufferedReader fromServer;
	public DataOutputStream toServer;
	
	/*
	 * Client
	 * 
	 * Creates a client by opening a TCP socket on port 3303.
	 */
	public Client(String serverIP) {
		try {
			this.socket = new Socket(serverIP, PORT);
			this.fromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.toServer = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Could not connect");
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
	
	public GameState readGameState() {
		try {
			ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());
			GameState gameState = (GameState)in.readObject();
			return gameState;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void sendGameState(GameState gameState) {
		try {
			ObjectOutputStream output = new ObjectOutputStream(this.socket.getOutputStream());
			output.writeObject(gameState);
		} catch (IOException e) {
			e.printStackTrace();
		}
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