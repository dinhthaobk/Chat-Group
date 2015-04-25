package Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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

public class ServerGUI extends JFrame {

	private JTextField txtPort;
	private JButton btnStart, btnStop, btnExit;
	private JTextArea txtChat, txtEvent;
	private Server server;
	private int port;

	public ServerGUI(String title, int port) {
		setTitle(title);
		this.server = null;
		this.port = port;
	}

	public void doShow() {
		setSize(800, 400);
		setLocationRelativeTo(null);
		addControl();
		setResizable(false);
		setVisible(true);
	}

	private void addControl() {

		JPanel pnBorder = new JPanel();
		pnBorder.setLayout(new BorderLayout());

		JPanel pnNort = new JPanel();
		pnNort.add(new JLabel("Port : "));
		txtPort = new JTextField("" + port, 10);
		txtPort.setEditable(true);
		pnNort.add(txtPort);
		btnStart = new JButton("Start");
		pnNort.add(btnStart);
		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		pnNort.add(btnStop);
		btnExit = new JButton("Exit");
		pnNort.add(btnExit);

		// Include Area Text Chat and Event
		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(new GridLayout(1, 2));
		txtChat = new JTextArea(20, 30);
		txtChat.setEditable(false);
		appendRoom("Chat room.");
		pnCenter.add(new JScrollPane(txtChat));
		txtEvent = new JTextArea(20, 10);
		txtEvent.setEditable(false);
		appendEvent("Log event.");
		pnCenter.add(new JScrollPane(txtEvent));

		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (server != null) {
					server.stop();
					server = null;
					txtPort.setEditable(true);
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
				}
				try {
					port = Integer.parseInt(txtPort.getText().trim());
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null,
							"Nhập cổng Port không hợp lê ");
				}

				
				btnStop.setEnabled(true);
				btnStart.setEnabled(false);
				txtPort.setEditable(false);
			}
		});

		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// server.stop();
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				txtPort.setEditable(true);
				server = null;

			}
		});

		btnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ref = JOptionPane.showConfirmDialog(null,
						"Thoát chương trình", "Thoát",
						JOptionPane.YES_NO_OPTION);
				if (ref == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});

		pnBorder.add(pnNort, BorderLayout.NORTH);
		pnBorder.add(pnCenter, BorderLayout.CENTER);
		getContentPane();
		add(pnBorder);

	}

	public void appendEvent(String str) {
		txtEvent.append(str + "\n");
	}

	public void appendRoom(String str) {
		txtChat.append(str + "\n");

	}

	public static void main(String[] args) {
		ServerGUI servGUI = new ServerGUI("Server", 8080);
		servGUI.doShow();
		
	}
}
