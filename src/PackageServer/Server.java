package PackageServer;

import java.io.*;
import java.net.*;
import java.util.*;

class Server {

	private int port;
	private ServerGUI servGUI;
	private ServerSocket servSocket;
	// Mảng chứa các tên đăng nhập vào
	private ArrayList<String> names;
	// Mảng chứa các luồng ghi
	private ArrayList<PrintWriter> writers;

	// Constructer
	public Server(int Port, ServerGUI serverGUI) {
		this.port = Port;
		this.servGUI = serverGUI;

		// Khoi tao chua cac phuong thuc cua cac client
		writers = new ArrayList<PrintWriter>();
		names = new ArrayList<String>();

	}

	public void stop() {
		if (servSocket != null) {
			try {
				servGUI.appendEvent("Server đã đóng");
				servSocket.close();

			} catch (Exception ex) {
				// No thing
			}
		}
	}

	public void start() throws IOException {
		try {
			servSocket = new ServerSocket(port);
			servGUI.appendEvent("Server đã được mở trên cổng :" + port);
		} catch (Exception ex) {
			servGUI.appendEvent("Không tạo mới được server trên cổng :" + port);
		}
		try {

			while (true) {
				Socket clientSocket = servSocket.accept();
				new ClientConnect(clientSocket).start();
			}

		} catch (Exception e) {
			servGUI.appendEvent("Không kết nối được client");
		}
	}

	public class ClientConnect extends Thread {

		private Socket socket;
		private String name;
		private String message;

		private BufferedReader in;
		private PrintWriter out;

		public ClientConnect(Socket clientSocket) {
			this.socket = clientSocket;
		}

		@Override
		public void run() {
			try {
				servGUI.appendEvent("Server is running");
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();
					System.out.println("Name login :" + name);
					if (name == null)
						return;
					synchronized (names) {
						if (!names.contains(name)) {
							names.add(name);
							break;
						}
					}
				}

				// Append to Event GUI
				servGUI.appendEvent(name + " đã đăng nhập vào phòng ");

				out.println("NAMEACCEPTED");

				writers.add(out);
				// Thực hiện quá trình gửi lại cho các client
				while (true) {
					String input = in.readLine();
					if (input == null) {
						return;
					}
					for (PrintWriter writer : writers) {
						writer.println("MESSAGE" + name + " send :" + input);
						System.out.println(name + ": " + input);
					}
				}

			} catch (Exception ex) {
				servGUI.appendEvent(name + " đã thoát !");
				names.remove(name);

			}
		}
	}
}