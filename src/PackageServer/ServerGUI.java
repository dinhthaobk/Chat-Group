package PackageServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ServerGUI extends JFrame implements ActionListener, WindowListener {
	private static final long serialVersionUID = 1L;
	private JTextField txtPort;
	private JButton btnStart, btnStop, btnExit, btnClear;
	private JTextArea txtEvent;
	private Server server;
	private int port;
	public Hashtable<String, Server> listUser;

	// Hàm khởi tạo mặc định
	public ServerGUI(String title) {
		setTitle(title);
		this.server = null;
		this.port = 0;
	}

	// Hàm thực thực hiện Frame
	public void doShow() {
		setSize(380, 300);
		setLocationRelativeTo(null);
		addControl();
		setResizable(true);
		setVisible(true);
	}

	// Thêm các thành phần giao diện
	private void addControl() {
		JPanel pnBorder = new JPanel();
		pnBorder.setLayout(new BorderLayout());
		JPanel pnNort = new JPanel();
		pnNort.add(new JLabel("Port : "));
		txtPort = new JTextField("3333", 5);
		txtPort.setEditable(true);
		pnNort.add(txtPort);
		btnStart = new JButton("Start");
		btnStart.addActionListener(this);
		pnNort.add(btnStart);
		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.addActionListener(this);
		pnNort.add(btnStop);
		btnExit = new JButton("Exit");
		btnExit.addActionListener(this);
		pnNort.add(btnExit);
		btnClear = new JButton("Clear");
		btnClear.addActionListener(this);
		pnNort.add(btnClear);
		// Include Area Event
		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(new BorderLayout());

		// Thêm ảnh cho phần thêm sự kiện trong server
		ImageIcon icon = new ImageIcon("background\\background_4.jpg");
		txtEvent = new JTextArea(10, 10) {

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
		txtEvent.setEditable(false);
		appendEvent("Log event.");
		pnCenter.add(new JScrollPane(txtEvent));
		pnBorder.add(pnNort, BorderLayout.NORTH);
		pnBorder.add(pnCenter, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane();
		add(pnBorder);
	}

	// / Thêm sự kiện vào mục event
	public void appendEvent(String str) {
		txtEvent.append(str + "\n");
	}

	// Hàm thực hiện chính
	public static void main(String[] args) {
		ServerGUI servGUI = new ServerGUI("Server");
		servGUI.doShow();
	}

	// Action
	@Override
	public void actionPerformed(ActionEvent e) {
		// Sự kiện nhấn nút thoát
		if (e.getSource() == btnExit) {
			int ref = JOptionPane.showConfirmDialog(null, "Exit", "Exit",
					JOptionPane.YES_NO_OPTION);
			if (ref == JOptionPane.YES_OPTION) {
				if (server != null)
					try {
						server.stop();
						server = null;
						System.exit(0);
					} catch (Exception ex) {
						appendEvent("Can't exit ! Server can't stop ");
					}

				else
					System.exit(0);
			}
		}

		// Sự kiện nhấn nút bắt đầu
		if (e.getSource() == btnStart) {
			// Lấy số hiệu cổng
			try {
				port = Integer.parseInt(txtPort.getText());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null,
						"Địa chỉ cổng không hợp lệ !");
				return;
			}

			if (server != null) {
				server.stop();
				server = null;
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				txtPort.setEditable(true);
			} else {
				server = new Server(port, this);
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				txtPort.setEditable(false);
				new ServerRuning().start();
			}
		}
		if (e.getSource() == btnStop) {
			try {
				server.stop();
				server = null;
				btnStop.setEnabled(false);
				btnStart.setEnabled(true);
				txtPort.setEditable(true);
				txtEvent.setText("");
			} catch (Exception ex) {
				appendEvent("Không thể dừng server !");
			}
		}
		if (e.getSource() == btnClear) {
			txtEvent.setText(null);
		}
	}

	class ServerRuning extends Thread {
		public void run() {
			try {
				server.start();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Server không thể khởi động");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
}
