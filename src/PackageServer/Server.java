package PackageServer;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.swing.JOptionPane;

class Server {

	private int port;
	private ServerGUI servGUI;
	private ServerSocket servSocket;
	// Mảng chứa các tên đăng nhập vào
	private ArrayList<String> names;
	// Mảng chứa các luồng ghi
	private ArrayList<PrintWriter> writers;

	Connection conn = null;
	Statement stmt = null;

	String ip;

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
				// servGUI.appendEvent("Server đã đóng");
				servSocket.close();

			} catch (Exception ex) {
				servGUI.appendEvent("Không thể đóng server !");
			}
		}
	}

	public void start() throws IOException, SQLException {

		// Cho server kết nối CSDL

		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/chatnhom", "root", "admin");
			stmt = conn.createStatement();
			System.out.println("Connected !");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		// Thực hiện thêm thử
		// addUser(conn, "Thao Phan", "192.168.1.1");
		// Xoa thu
		// deleteUser(stmt, "12");
		// System.out.println("Them ok");
		// Thực hiện mở server
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
			servGUI.appendEvent("Server đã đóng !");
		}
	}

	// Thêm người dùng vào CSDL
	public void addUser(Connection conn, String name, String ip) {
		System.out.println("Them nguoi dung vao CSDL");
		// String query = "Insert into danhsach value ('" + name + "','" +
		// ip
		// + "');";
		String queryStr = "insert ignore into danhsach values(?,?);";
		try (PreparedStatement addStmt = conn.prepareStatement(queryStr)) {
			addStmt.setString(1, name);
			addStmt.setString(2, ip);
			addStmt.addBatch();
			addStmt.executeBatch();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	// Xóa người dùng khỏi CSDL
	public void deleteUser(Statement stmt, String name) throws SQLException {
		System.out.println("Xoa  nguoi dung ");
		String query = "delete from danhsach where tenTaiKhoan = '" + name
				+ "';";
		int rtUpdate = stmt.executeUpdate(query);
		System.out.println(rtUpdate);

	}

	// Thread để lắng nghe kết nối từ client
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
				ip = socket.getInetAddress().getHostAddress();
				System.out.println(ip);
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();
					System.out.println("Name login :" + name);
					if (name == null)
						return;
					// Đồng bộ tên lên server
					synchronized (names) {
						if (!names.contains(name)) {
							names.add(name);
							addUser(conn, name, ip);
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
						writer.println("MESSAGE" + name + "  :" + input);
						// System.out.println(name + ": " + input);
					}
				}

			} catch (Exception ex) {
				servGUI.appendEvent(name + " đã thoát !");
				names.remove(name);
				try {
					deleteUser(stmt, name);
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
}
