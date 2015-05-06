package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;

import com.sun.corba.se.impl.ior.WireObjectKeyTemplate;
import com.sun.imageio.plugins.common.InputStreamAdapter;

class Server {

	private int port;
	private ServerGUI serverGui;
	private ServerSocket servSocket;

	private ArrayList<String> nameUsers;

	private ArrayList<PrintWriter> printWriter;
	private boolean isConnect = true;

	public Server(int Port, ServerGUI serverGUI) {
		this.port = Port;
		this.serverGui = serverGUI;

		// Khoi tao chua cac phuong thuc cua cac client
		printWriter = new ArrayList<PrintWriter>();
		nameUsers = new ArrayList<String>();

	}

	public void stop() {
		if (servSocket != null) {
			try {
				serverGui.appendEvent("Close");
				servSocket.close();

			} catch (Exception ex) {
				// No thing
			}
		}
	}

	public void start() throws IOException {

		serverGui.appendEvent(" Server is start ! ");
		serverGui.appendEvent("Port is :  " + port);
		servSocket = new ServerSocket(port);
		try {
			while (true) {
				new Handle(servSocket.accept()).start();
			}

		} catch (Exception e) {
			// / No thing

		} finally {
			servSocket.close();
		}
	}

	public class Handle extends Thread {

		private Socket clienSocket;
		private String nameUse;
		private String messageUse;

		private BufferedReader in;
		private PrintWriter out;

		public Handle(Socket clientSocket) {
			this.clienSocket = clientSocket;
		}

		@Override
		public void run() {
			try {
				serverGui.appendEvent(clienSocket.getInetAddress().toString()
						+ "has connect");
				in = new BufferedReader(new InputStreamReader(
						clienSocket.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(
						clienSocket.getOutputStream()));

				// while (true) {
				// out.print("nameUse");
				// nameUse = in.readLine();
				// if (nameUse == null)
				// return;
				// else
				// synchronized (nameUsers) {
				// if (!nameUsers.contains(nameUse))
				// nameUsers.add(nameUse);
				// else
				// break;
				// }
				// }
				// serverGui.appendEvent(nameUse + "has connect");
				// out.println("nameOK");
				// printWriter.add(out);
				//
				// while (true) {
				// String input = in.readLine();
				// if (input == null)
				// return;
				// else
				// for (PrintWriter fwriter : printWriter) {
				// out.println("Message " + input);
				// }
				//
				// }
			} catch (Exception ex) {
				// No thing
			} finally {
				if (nameUse != null)
					nameUsers.remove(nameUse);
				if (out != null)
					printWriter.remove(out);
				try {
					clienSocket.close();
				} catch (IOException e) {
					// no thing
				}
			}
		}
	}
}
