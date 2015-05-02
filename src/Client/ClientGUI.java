package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.*;

public class ClientGUI extends JFrame implements ActionListener {

	// Varible for GUI
	private JTextArea txtMessage;
	private JTextField txtSend, txtName, txtServ;
	private JButton btnSend, btnConnect, btnExit;

	// Varible use;
	private String name;
	private String host;

	// Varible Client
	Client client;

	public ClientGUI(String title) {
		setTitle(title);
		// Component
		JPanel pnBorder = new JPanel();
		pnBorder.setLayout(new BorderLayout(2, 2));

		JPanel pnLogin = new JPanel();
		pnLogin.setLayout(new BoxLayout(pnLogin, BoxLayout.Y_AXIS));
		JPanel pnText = new JPanel();
		pnText.setLayout(new BoxLayout(pnText, BoxLayout.X_AXIS));
		pnText.add(new JLabel("Name user :"));

		txtName = new JTextField("" + host, 10);

		txtName.addActionListener(this);
		pnText.add(txtName);
		pnText.add(new JLabel("Server :"));
		txtServ = new JTextField("" + host, 10);
		txtServ.addActionListener(this);
		pnText.add(txtServ);

		JPanel pnButton = new JPanel();
		pnButton.setLayout(new FlowLayout());
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(this);
		pnButton.add(btnConnect);
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);

			}
		});
		pnButton.add(btnExit);

		pnLogin.add(pnText);
		pnLogin.add(pnButton);
		pnBorder.add(pnLogin, BorderLayout.NORTH);

		JPanel pnSend = new JPanel();
		pnSend.setLayout(new FlowLayout());
		pnSend.add(new JLabel("Text"));
		txtSend = new JTextField(20);
		pnSend.add(txtSend);
		btnSend = new JButton("Send");
		pnSend.add(btnSend);

		pnBorder.add(pnSend, BorderLayout.SOUTH);

		JPanel pnMessage = new JPanel();
		pnMessage.setLayout(new BoxLayout(pnMessage, BoxLayout.Y_AXIS));
		pnMessage.add(new JLabel("Message"));
		txtMessage = new JTextArea();
		pnMessage.add(txtMessage);
		pnBorder.add(pnMessage, BorderLayout.CENTER);

		Container con = getContentPane();
		con.add(pnBorder);

		// Khung chung
		setSize(350, 500);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args) {
		ClientGUI clientGui = new ClientGUI("Client");
	}

	public void appendMessage(String str) {
		txtMessage.append(str);
		txtMessage.append("\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnConnect) {
			{
				System.out.println("Click Connect");
				 name = txtName.getText();
				//name = "Test";
				// host = txtServ.getText();
				host = "localhost";
				System.out.println("Name input : " + name + "Host input :"
						+ host);

				client = new Client(name, host, this);
				client.connect();
				btnConnect.setEnabled(false);
			}

		}
	}
}
