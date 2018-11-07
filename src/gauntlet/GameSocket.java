package gauntlet;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.net.Socket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;


public class GameSocket extends Thread {
	
	public int PORT = 3303;
	public ServerSocket serverSocket;
	public Socket clientSocket;
	public boolean isServer = true;
	
	public GameSocket() throws IOException {
	}
	
	public void createServer() {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			
		}
	}
	
	public void runServer(int guests) {
		
		while (guests-1 > 0) {
			try {
				serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			guests--;
			
			System.out.println("Client connected");
		}
	}
	
	public void createClient(String serverIP) {
		try {
			this.clientSocket = new Socket(serverIP, PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Connected to server!");
	}

}
