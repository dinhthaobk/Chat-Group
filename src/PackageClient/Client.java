package PackageClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

class Client {
	private String name;
	private String host;

	private BufferedReader in;
	private PrintWriter out;
	private ClientGUI clientGui;
	private int port;
	Socket socket;

	Connection conn = null;
	Statement stmt = null;

	public Client(String name, String host, int port, ClientGUI clientGUI) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.clientGui = clientGUI;
		// System.out.println("Name form GUI :" + name);
		// System.out.println("Host from GUI " + host);
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/chatnhom", "root", "admin");
			stmt = conn.createStatement();
			System.out.println("Connected !");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}


	}

	public void getUser(Statement stmt)
	{
		
	}
	
	public void send() {
		String message;
		message = clientGui.getMessage();
		if (message.length() == 0)
			return;
		out.println(message);
		out.flush();

	}

	public void start() {
		try {
			socket = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				String line = in.readLine();
				if (line.startsWith("SUBMITNAME")) {
					System.out.println(name);
					out.println(name);
				}
				if (line.startsWith("NAMEACCEPTED")) {
					// System.out.println("success");
				}

				if (line.startsWith("MESSAGE")) {
					clientGui.appendMessage(line.substring(7));
				}
				
				if (line.startsWith("LIST_ONLINE")) {
					clientGui.appendListOnline(line.substring(12));
					
				}

				// String reply = in.readLine();
				// clientGui.appendMessage(reply);
			}

		} catch (IOException e) {
			// No thing
			// e.printStackTrace();
			System.err.println("Không kết nối được !");
		}

	}

	public void sendMessage() {
		send();
		System.out.println("Send message" + clientGui.getMessage());

	}

	public void stop() {
		try {
			socket.close();
			socket = null;
			name = null;
			host = null;
		} catch (Exception ex) {
			System.out.println("Không dừng được server");
		}
	}
	//////////////////////////////////////////////////
	public boolean checkLogin(String nick){
		
		if(nick.compareTo("")==0)
			return false;
		else{
			//  send(nick);
			return true;
			/*int sst = Integer.parseInt(getMSG());
			if(sst==0)
				 return false;
			else return true;*/
		}
	}
	public void send(String data) {
	// 	out.println(data);
		out.flush();
		System.out.println("Send message" + data);

	}
	//////////////////////////////////////////////////////////
}
