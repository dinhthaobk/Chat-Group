package Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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

public class ServerGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
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
		setSize(400, 400);
		setLocationRelativeTo(null);
		addControl();
		setResizable(true);
		setVisible(true);
	}

	private void addControl() {
		// UI
		JPanel pnBorder = new JPanel();
		pnBorder.setLayout(new BorderLayout());
		JPanel pnNort = new JPanel();
		pnNort.add(new JLabel("Port : "));
		txtPort = new JTextField("" + port, 10);
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
		// Include Area Text Chat and Event
		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(new GridLayout(1, 2));
		txtChat = new JTextArea(9, 9);
		txtChat.setEditable(false);
		appendRoom("Chat room.");
		pnCenter.add(new JScrollPane(txtChat));
		txtEvent = new JTextArea(9, 9);
		txtEvent.setEditable(false);
		appendEvent("Log event.");
		pnCenter.add(new JScrollPane(txtEvent));
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
		ServerGUI servGUI = new ServerGUI("Server", 5000);
		servGUI.doShow();
	}

	// Action
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnExit) {
			int ref = JOptionPane.showConfirmDialog(null, "Exit", "Exit",
					JOptionPane.YES_NO_OPTION);
			if (ref == JOptionPane.YES_OPTION) {
				if (server != null)
					try {
						server.stop();
						server = null;
					} catch (Exception ex) {
						appendEvent("Can't exit ! Server can't stop ");
					}

				else
					System.exit(0);
			}
		}
		if (e.getSource() == btnStart) {
			if (server != null) {
				server.stop();
				server = null;
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				txtPort.setEditable(true);
			} else
				server = new Server(port, this);
			// appendEvent("Start sever !");
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);
			txtPort.setEditable(false);
			new ServerRuning().start();

		}
		if (e.getSource() == btnStop) {
			try {
				server.stop();
				server = null;
				btnStop.setEnabled(false);
				btnStart.setEnabled(true);
				txtPort.setEditable(true);
			} catch (Exception ex) {
				appendEvent("ko the dung server");
			}
		}
	}

	class ServerRuning extends Thread {
		public void run() {
			try {
				server.start();
				appendEvent("Server is running");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
