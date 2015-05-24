package PackageClient;

import java.awt.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

class Client extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	}

	// Thực hiện kết nối Client với CSDL

	public int connectDatabase() {
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/chatnhom", "root", "admin");
			stmt = conn.createStatement();
			System.out.println("Connected !");

			return 1;

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return 0;
		}
	}

	public void showTableData() throws SQLException {
		Frame framResult = new JFrame("List online in ChatRoom");
		framResult.setLayout(new BorderLayout());
		framResult.add(new JLabel("Danh sách thành viên trong phòng "),
				BorderLayout.NORTH);
		String[] columns = { "Tên tài khoản", "IP" };
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		JTable table = new JTable();
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFillsViewportHeight(true);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		String query = "Select * from danhsach";
		ResultSet rs = stmt.executeQuery(query);
		if (!rs.first())
			System.out.println("Have no record");

		else {
			// display result if not empty
			do {
				String tenTaiKhoan = rs.getString("tenTaiKhoan");
				String ipTaiKhoan = rs.getString("ipTaiKhoan");
				model.addRow(new Object[] { tenTaiKhoan, ipTaiKhoan });
				System.out.println(tenTaiKhoan + ", " + ipTaiKhoan);
			} while (rs.next());
		}

		framResult.add(scroll);
		framResult.add(table);
		framResult.setSize(250, 300);
		framResult.setVisible(true);
	}

	public void send() {
		String message;
		message = clientGui.getMessage();
		if (message.length() == 0)
			return;
		out.println(message);
		out.flush();

	}

	// // send file
	// public void sendFile() throws IOException {
	// String filePath = clientGui.getFilePath();
	// File file = new File(filePath);
	// long fileLength = file.length();
	// byte[] bytes = new byte[(int) fileLength];
	// FileInputStream fis = new FileInputStream(file);
	// BufferedInputStream bis = new BufferedInputStream(fis);
	// BufferedOutputStream out = new BufferedOutputStream(
	// socket.getOutputStream());
	//
	// int count;
	//
	// while ((count = bis.read(bytes)) > 0) {
	// out.write(bytes, 0, count);
	// }
	// out.flush();
	// out.close();
	// fis.close();
	// bis.close();
	// }

	// ket thuc
	public void start() {
		try {
			socket = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				String line = in.readLine();
				if (line.startsWith("SUBMITNAME")) {
					// System.out.println(name);
					out.println(name);
				}
				if (line.startsWith("CANCELNAME")) {
					// System.out.println(line);
					JOptionPane.showMessageDialog(null,
							"Tên đăng nhập đã trùng !", "Chú ý! ",
							JOptionPane.WARNING_MESSAGE);
					System.exit(1);
				}
				if (line.startsWith("NAMEACCEPTED")) {
					// System.out.println(line);
					JOptionPane.showMessageDialog(null,
							"Đăng nhập thành công !", "Thông báo ! ",
							JOptionPane.INFORMATION_MESSAGE);
				}

				if (line.startsWith("LIST_ONLINE")) {
					String lineList = line.substring(12).replace(',', '\n');
					clientGui.appendListOnline(lineList);
					System.out.println(line);

				}
				if (line.startsWith("MESSAGE")) {
					// String messge = line.substring(7);
					// if (!messge.equals("EXIT")) {
					// clientGui.appendMessage(messge);
					// }
					clientGui.appendMessage(line.substring(7));
				}
				if (line.startsWith("LIST_ONLINE")) {
					String lineList = line.substring(12).replace(',', '\n');
					clientGui.appendListOnline(lineList);
					System.out.println(line);

				}

			}

		} catch (IOException e) {
			// System.err.println("Không kết nối được !");
			JOptionPane.showMessageDialog(null,
					"Không kết nối được với Server !", "Cảnh báo !",
					JOptionPane.WARNING_MESSAGE);
		}

	}

	public void sendMessage() {
		send();
		System.out.println("Send message" + clientGui.getMessage());

	}

	public void stop() {
		try {
			out.println("EXIT");
			socket.close();
			in.close();
			out.close();
			socket = null;
			name = null;
			host = null;
		} catch (Exception ex) {
			System.out.println("Không dừng được server");
		}
	}

	// ////////////////////////////////////////////////
	public boolean checkLogin(String nick) {

		if (nick.compareTo("") == 0)
			return false;
		else {
			// send(nick);
			return true;
			/*
			 * int sst = Integer.parseInt(getMSG()); if(sst==0) return false;
			 * else return true;
			 */
		}
	}

	public void send(String data) {
		// out.println(data);
		out.flush();
		System.out.println("Send message" + data);

	}
	// ////////////////////////////////////////////////////////
}
