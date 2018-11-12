package gauntlet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	
	public static final int PORT = 3303;
	public InetAddress serverAddress;
	public DatagramSocket socket;
	
	/*
	 * Client
	 * 
	 * Creates a client by opening a UDP socket on port 3303.
	 */
	public Client(String serverIP) {
		
		try {
			this.serverAddress = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		try {
			this.socket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * joinServer
	 * 
	 * Attempts to join the server using the server's address.
	 */
	public void joinServer() {
		String msg = "Join";
		byte[] buf = msg.getBytes();
		DatagramPacket joinPacket = new DatagramPacket(buf, buf.length, this.serverAddress, PORT);
		
		try {
			this.socket.send(joinPacket);
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
		byte[] xbuf = sb.toString().getBytes();
		DatagramPacket xPos = new DatagramPacket(xbuf, xbuf.length, this.serverAddress, PORT);
		try {
			this.socket.send(xPos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Send y coordinate
		int yLength = (int)Math.log10(y) + 1;
		sb = new StringBuilder();
		sb.append(yLength);
		sb.append(y);
		byte[] ybuf = sb.toString().getBytes();
		DatagramPacket yPos = new DatagramPacket(ybuf, ybuf.length, this.serverAddress, PORT);
		try {
			this.socket.send(yPos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * sendCommand
	 * 
	 * Sends a command the server asking to move/attack.
	 */
	public void sendCommand(String command, Gauntlet gg, InetAddress serverAddr) {
		byte[] buf = command.getBytes();
		DatagramPacket joinPacket = new DatagramPacket(buf, buf.length, serverAddr, PORT);

		try {
			this.socket.send(joinPacket);
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
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		try {
			gg.client.socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String cmd = null;
		try {
			cmd = new String(packet.getData(), "UTF-8");
			cmd = cmd.substring(1, cmd.charAt(0) - '0'+1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return cmd;
	}
	
	/*
	 * closeClient
	 * 
	 * Kills the client by closing the UDP socket.
	 */
	public void closeClient() {
		this.socket.close();
	}
}