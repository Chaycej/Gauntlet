package gauntlet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Client {
	
	private static final int PORT = 3303;
	public String serverIP;
	public DatagramSocket socket;
	
	
	// Creates a client socket
	public Client(String serverIP) {
		this.serverIP = serverIP;
		
		try {
			this.socket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	// Sends a join message to the server
	public void joinServer() {
		
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(this.serverIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		String msg = "Join";
		byte[] buf = msg.getBytes();
		DatagramPacket joinPacket = new DatagramPacket(buf, buf.length, addr, this.PORT);
		
		try {
			this.socket.send(joinPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
