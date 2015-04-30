package Client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sun.org.apache.bcel.internal.classfile.PMGClass;

public class Client extends JFrame implements ActionListener {

	// Varible for GUI
	JTextField txtSend;
	JButton btnSend;
	JTextArea txtResult;
	JFrame myFrame;
	// Varible for Send and Recv
	private BufferedReader in;
	private PrintWriter out;

	public Client(String title) {
		// GUI
		setTitle(title);
	}

	public void doShow() {
		setSize(400, 400);
		setLocationRelativeTo(null);
		addControl();
		setResizable(false);
		setVisible(true);

	}

	private void addControl() {
		JPanel pnBorder = new JPanel();
		pnBorder.setLayout(new BorderLayout());

		JPanel pnNort = new JPanel();
		pnNort.add(new JLabel("Message"));
		pnBorder.add(pnNort, BorderLayout.NORTH);

		JPanel pnCenter = new JPanel();
		pnCenter.setLayout(new BoxLayout(pnCenter, BoxLayout.Y_AXIS));
		txtResult = new JTextArea(1, 1);
		txtResult.setSize(10, 10);
		txtResult.setEditable(false);

		pnCenter.add(new JScrollBar().add(txtResult));
		pnCenter.add(new JLabel("Text Send"));
		pnBorder.add(pnCenter, BorderLayout.CENTER);

		JPanel pnSouth = new JPanel();
		pnSouth.setLayout(new BoxLayout(pnSouth, BoxLayout.X_AXIS));
		txtSend = new JTextField();
		pnSouth.add(txtSend);
		btnSend = new JButton("Send");
		btnSend.addActionListener(this);
		pnSouth.add(btnSend);
		pnBorder.add(pnSouth, BorderLayout.SOUTH);

		getContentPane().add(pnBorder);

	}

	public static void main(String[] args) {
		Client client = new Client("Client Chat");
		client.doShow();

	}

	private String getAdrressHost()
	{
		return null;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		txtResult.append("Click");
	}
}
