package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;

import com.sun.imageio.plugins.common.InputStreamAdapter;

class Server {

	private int port;
	private ServerGUI serverGui;
	private ServerSocket servSocket;
	private Socket socketClientConnect;
	private HashSet<Socket> connSocket;

	private boolean isConnect = true;

	public Server(int Port, ServerGUI serverGUI) {
		this.port = Port;
		this.serverGui = serverGUI;
	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void start() throws IOException {

		serverGui.appendEvent("Start is click ");
		System.out.println("Port is :  " + port);
		servSocket = new ServerSocket(port);
		try {
			while (true) {
				socketClientConnect = servSocket.accept();
				serverGui
						.appendEvent("Accept client :"
								+ socketClientConnect.getInetAddress()
										.getHostAddress());
				try (BufferedReader in = new BufferedReader(
						new InputStreamReader(
								socketClientConnect.getInputStream()));
						PrintWriter out = new PrintWriter(
								socketClientConnect.getOutputStream())) {
					serverGui.appendRoom("Recever form client :"
							+ in.readLine());
				} catch (Exception ex) {
					// No thing
				}
			}
		} catch (Exception e) {
			// / No thing
		}

	}

	public class Handle extends Thread {

	}

	public class ListenClient extends Thread {
		public void run() {

		}
	}

}