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
	int bufferSize;

	private InputStream is;
	private FileOutputStream fos;
	private BufferedOutputStream bos;

	// Mảng chứa các tên đăng nhập vào
	private ArrayList<String> names;
	// Mảng chứa các luồng ghi
	private ArrayList<PrintWriter> writers;

	private String listUser = "";

	// Biến phục vụ cho Database
	Connection conn = null;
	Statement stmt = null;
	String ip;

	// Hàm khởi tạo mặc định
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
				servGUI.appendEvent("Thông báo : Server đã được đóng trên cổng "
						+ port);
				servSocket.close();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null,
						"Không thể đóng server trên cổng " + port,
						"Cảnh báo !", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void start() throws IOException, SQLException {

		// Đoạn phục vụ cho Server kết nối databases
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/chatnhom", "root", "admin");
			stmt = conn.createStatement();
			System.out.println("Connected !");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		// Đoạn chương trình khởi động Socket
		try {
			servSocket = new ServerSocket(port);
			servGUI.appendEvent("Thông báo :Server đã được mở trên cổng :"
					+ port);
		} catch (Exception ex) {
			servGUI.appendEvent("Cảnh báo :Không tạo mới được server trên cổng :"
					+ port);
		}
		try {

			while (true) {
				Socket clientSocket = servSocket.accept();
				new ClientConnect(clientSocket).start();
			}

		} catch (Exception e) {
			servGUI.appendEvent("Cảnh báo : Server không thể lắng nghe trên cổng "
					+ port);
		}
	}

	// Thêm người dùng vào CSDL
	public void addUser(Connection conn, String name, String ip) {
		System.out.println("Them nguoi dung vao CSDL");
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
				ip = socket.getInetAddress().getHostAddress(); // Lấy địa chỉ IP
				// System.out.println(ip);
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
				// //
				// listUser = "";
				// for (String object : names)
				// listUser += "," + object;
				// //
				// Append to Event GUI
				servGUI.appendEvent(name + " đã đăng nhập vào phòng ");

				out.println("NAMEACCEPTED" + name);

				writers.add(out);
				// for (PrintWriter writer : writers) {
				// writer.println("LIST_ONLINE" + listUser);
				// }
				// for(String name : names){ listUser +=name.toString()+"\n";}
				// Thực hiện quá trình gửi lại cho các client
				while (true) {
					// send file
					// try {
					// is = socket.getInputStream();
					//
					// bufferSize = socket.getReceiveBufferSize();
					// System.out.println("Buffer size: " + bufferSize);
					// } catch (IOException ex) {
					// System.out.println("Can't get socket input stream. ");
					// }
					// try {
					// fos = new FileOutputStream("F:\\");
					// bos = new BufferedOutputStream(fos);
					//
					// } catch (FileNotFoundException ex) {
					// System.out.println("File not found. ");
					// }
					//
					// byte[] bytes = new byte[bufferSize];
					//
					// int count;
					//
					// while ((count = is.read(bytes)) > 0) {
					// bos.write(bytes, 0, count);
					// }
					//
					// bos.flush();
					// bos.close();
					// is.close();
					// // ket thuc send file
					String input = in.readLine();

					if (input == null) {
						return;
					}

					for (PrintWriter writer : writers) {
						writer.println("MESSAGE" + name + "  :" + input);
						System.out.println(name + ": " + input);
					}

				}

			} catch (Exception ex) {
				servGUI.appendEvent(name + " đã thoát !");
				names.remove(name);
				// //
				listUser = "";
				for (String object : names)
					listUser += "," + object;
				for (PrintWriter writer : writers) {
					writer.println("LIST_ONLINE" + listUser);
				}

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
