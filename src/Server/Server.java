package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class Server {

	private int port;
	private ServerGUI serverGui;
	private ServerSocket servSocket;
	private Socket socketClientConnect;
	private ArrayList<Socket> listClientConnect;

	private boolean isConnect = true;

	public Server(int Port, ServerGUI serverGUI) {
		this.port = port;
		this.serverGui = serverGUI;
	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void start() throws IOException {
		serverGui.appendEvent("OK");
		servSocket = new ServerSocket(port);
		while (isConnect) {
			socketClientConnect = servSocket.accept();
			serverGui.appendEvent("Accept listen :"
					+ socketClientConnect.getInetAddress().getHostAddress());		
		}
	}

	public class ListenClient extends Thread {
		public void run() {

		}
	}

}