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

	public Client(String name, String host) {
		this.name = name;
		this.host = host;
	}

	public Client(String name, String host, ClientGUI clientGUI) {
		this.name = name;
		this.host = host;
		this.clientGui = clientGUI;

	}

	public void send() {
		String message;
		message = clientGui.getMessage();
		if (message.length() == 0)
			return;
		out.println(message);
		out.flush();

	}

	@SuppressWarnings("resource")
	public void connect() {
		try {
			Socket clientSocket = new Socket("localhost", 3333);
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(
					clientSocket.getOutputStream()));

			// while (true) {
			// // String line = in.readLine();
			// // if (line.startsWith("nameUse")) {
			// // out.println(clientGui.getName());
			// // }
			// // if (line.startsWith("nameOK")) {
			// // // No thing
			// // }
			// // if (line.startsWith("message")) {
			// // clientGui.appendMessage(line.substring(8));
			// // }
			// String reply = in.readLine();
			// clientGui.appendMessage(reply);
			// }

		} catch (IOException e) {
			// No thing
			// e.printStackTrace();
			System.err.println("Ko connect dk !");
		}

	}

	public void sendMessage() {
		send();
		System.out.println("Send message" + clientGui.getMessage());

	}
}