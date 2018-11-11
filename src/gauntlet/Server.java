package gauntlet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
	
	public static final int PORT = 3303;
	
	public DatagramSocket socket;
	
	// Opens a server UDP socket
	public Server() {
		try {
			this.socket = new DatagramSocket(Server.PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		System.out.println("DEBUG: Started server");
	}
	
	// Starts the server and begins listening for a client connection
	public void run() {
		
		byte[] buf = new byte[256];
		
		// Listen for client connection
		while (true) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				this.socket.receive(packet);
				System.out.println("Received join message from: " + packet.getAddress().toString() + ": " + packet.getPort());
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	public void closeServer() {
		this.socket.close();
	}
}