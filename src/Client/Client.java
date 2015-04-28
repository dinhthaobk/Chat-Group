package Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		Socket clientSocket = new Socket("localhost", 8080);
		System.out.println("C");
	}
}
