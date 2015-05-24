package PackageClient;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class ClientGUI extends JFrame implements ActionListener, WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Varible for GUI

	protected JTextArea txtMessage, txtListUser;
	protected JTextField txthost, txtUser, txtSend, txtPort;
	protected JButton btnConnect, btnExit, btnLogin, btnSend, sendFileBtn,
			openBtn, saveBtn;
	protected String filePath, fileSave;
	// Varible use;
	private Client client;

	public String getFileSave() {
		return fileSave;
	}

	public String getFilePath() {
		return filePath;
	}

	public ClientGUI(String title) {
		setTitle(title);
		setSize(600, 400);
		addComponent();
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addComponent() {
		JPanel pnBorder = new JPanel();
		pnBorder.setLayout(new BorderLayout());

		// Jpanel for Message and text send
		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(new BorderLayout());
		JPanel pnMessage = new JPanel(new BorderLayout());

		ImageIcon icon = new ImageIcon("background\\background_1.jpg");
		txtMessage = new JTextArea(10, 10) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		// icon = new ImageIcon("src\\PackageClient\\bg.jpg");

		txtMessage.setEditable(false);
		pnMessage.add(new JScrollPane(txtMessage));
		pnMessage.setBorder(BorderFactory.createTitledBorder("Message Chat"));
		pnCenter.add(pnMessage, BorderLayout.CENTER);
		JPanel pnTextSend = new JPanel(new FlowLayout());
		txtSend = new JTextField(25);
		// txtSend.setLineWrap(isResizable()); // Xuong dong khi het dong
		txtSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				client.send();
				// ////////////////////////////////////////

				if (e.getSource() == btnLogin) {
					client.checkLogin(txtUser.getName());
					txtListUser.setText(txtUser.getName());

				}

			}
		});
		// File chooser GUI
		JPanel pnFile = new JPanel();
		saveBtn = new JButton("Save");
		sendFileBtn = new JButton("Send");
		openBtn = new JButton("Open");
		pnFile.add(sendFileBtn, BorderLayout.WEST);
		pnFile.add(openBtn, BorderLayout.EAST);
		pnFile.add(saveBtn, BorderLayout.CENTER);
		pnBorder.add(pnFile, BorderLayout.SOUTH);
		pnFile.setVisible(true);
		// send file button
		sendFileBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					client.sendFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		openBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser openFile = new JFileChooser();

				// JFileChooser fileOpen = new JFileChooser();
				if (openFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File selectedFile = openFile.getSelectedFile();
					filePath = selectedFile.getPath();
					System.out.println(filePath);
				}

			}
		});
		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser saveFile = new JFileChooser();
				if (saveFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File selectedSaveFile = saveFile.getSelectedFile();
					fileSave = selectedSaveFile.getPath();
					System.out.println(fileSave);
				}

			}
		});

		// ket thuc File Chooser

		// ket thuc File Chooser
		pnTextSend.add(txtSend);
		btnSend = new JButton("Send");
		btnSend.setVisible(false);
		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				client.send();

			}
		});
		pnTextSend.add(btnSend);
		pnTextSend
				.setBorder(BorderFactory.createTitledBorder("Text to send :"));
		pnCenter.add(pnTextSend, BorderLayout.SOUTH);
		pnCenter.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JPanel pnEast = new JPanel();
		pnEast.setLayout(new BorderLayout());
		// JPanel for Login
		JPanel pnLogin = new JPanel(new GridLayout(4, 1, 1, 1));
		pnLogin.add(new JLabel("Server:"));
		txthost = new JTextField("localhost", 10);
		txthost.addActionListener(this);
		pnLogin.add(txthost);

		pnLogin.add(new JLabel("User name:"));
		txtUser = new JTextField(" ", 10);

		pnLogin.add(new JLabel("Cổng:"));
		txtPort = new JTextField("3333", 5);
		pnLogin.add(txtPort);
		pnLogin.add(new JLabel("Tài khoản:"));
		txtUser = new JTextField("Thao Phan", 10);

		txtUser.addActionListener(this);
		pnLogin.add(txtUser);
		btnLogin = new JButton("Đăng nhập");
		btnLogin.addActionListener(this);
		pnLogin.add(btnLogin);
		btnExit = new JButton("Thoát");
		btnExit.addActionListener(this);
		pnLogin.add(btnExit);
		pnLogin.setBorder(BorderFactory.createTitledBorder("Login"));

		// JPanel for List use
		JPanel pnList = new JPanel(new BorderLayout());
		txtListUser = new JTextArea();
		txtListUser.setEditable(false);
		pnList.add(new JScrollPane(txtListUser));

		pnList.setBorder(BorderFactory
				.createTitledBorder("Danh sách người trong phòng !"));

		pnEast.add(pnLogin, BorderLayout.NORTH);
		pnEast.add(pnList, BorderLayout.CENTER);

		pnBorder.add(pnCenter, BorderLayout.CENTER);
		pnBorder.add(pnEast, BorderLayout.EAST);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane();
		add(pnBorder);

	}

	public static void main(String[] args) {
		ClientGUI clientGui = new ClientGUI("Client");

	}

	public void appendMessage(String str) {
		txtMessage.append(str);
		txtMessage.append("\n");

	}

	public void appendListOnline(String str) {
		txtListUser.setText("");
		txtListUser.append(str);
		txtListUser.append("\n");
	}

	public String getMessage() {
		String txt = txtSend.getText();
		txtSend.setText(" ");
		return txt;
	}

	public String getHost() {
		return txthost.getText().trim();
	}

	public int getPort() {
		int tmp = 0;
		try {
			tmp = Integer.parseInt(txtPort.getText().trim());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Cổng nhập sai !");
		}
		return tmp;
	}

	public String getName() {
		return txtUser.getText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnExit) {
			int ref = JOptionPane.showConfirmDialog(null, "Thoát chương trình",
					"Thoát", JOptionPane.YES_NO_OPTION);
			if (ref == JOptionPane.YES_OPTION) {
				System.out.println("Click exit");
				System.exit(0);
			}
		}
		if (e.getSource() == btnLogin) {

			if (btnLogin.getText() == "Đăng nhập") {
				btnLogin.setText("Đăng xuất");
				String host = getHost();
				String name = getName();
				int port = getPort();
				if (host.length() == 0 || name.length() == 0 || port == 0) {
					// System.out.println("Nhap thieu thong tin");
					JOptionPane.showMessageDialog(this, "Nhập thiếu thông tin");
				} else {
					client = new Client(getName(), getHost(), port, this);
					new StartClient().start();
					client.checkLogin(getName());
				}
			} else {
				btnLogin.setText("Đăng nhập");
				txtMessage.setText("");
				client.stop();
				client = null;

			}
			// client.checkLogin(getName());
		}

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	class StartClient extends Thread {

		@Override
		public void run() {
			if (btnLogin.getText() == "Đăng xuất")
				client.start();
			else
				client.stop();
		}
	}
}
