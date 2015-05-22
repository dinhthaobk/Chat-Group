package PackageClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

class Client {
	private String name;
	private String host;

	private BufferedReader in;
	private PrintWriter out;
	private ClientGUI clientGui;
	private int port;
	Socket socket;

	public Client(String name, String host, int port, ClientGUI clientGUI) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.clientGui = clientGUI;
		// System.out.println("Name form GUI :" + name);
		// System.out.println("Host from GUI " + host);
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
			name = null;
			host = null;
		} catch (Exception ex) {
			System.out.println("Không dừng được server");
		}
	}
}