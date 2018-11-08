package gauntlet;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.ServerSocket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.io.IOException;


public class GameSocket extends Thread {
	
	public int PORT = 3303;
	public DatagramSocket serverSocket;
	public DatagramSocket clientSocket;
	public boolean isServer = true;
	
	public byte[] buf = new byte[256];
	
	public GameSocket() throws IOException {}
	
	public void createServer() {
		try {
			serverSocket = new DatagramSocket(PORT);
		} catch (IOException e) {
			
		}
	}
	
	public void runServer() {
		
		while (true) {
			System.out.println("started server");
			try {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				serverSocket.receive(packet);
				InetAddress address = packet.getAddress();
	            int port = packet.getPort();
	            
	            System.out.println("Received packet from: " + address + ": " +port);
	            break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void createClient(String serverIP) {
		try {
			this.clientSocket = new DatagramSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String msg = "Hello";
		byte[] buf = msg.getBytes();
		InetAddress server = null;
		
		try {
			server = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, server, PORT);
		try {
			clientSocket.send(packet);
			System.out.println("Sent packet");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Connected to server!");
	}

}
