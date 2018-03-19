package com.herle.java.utils.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.herle.java.utils.GenericUtil;

public class SampleSocketServer extends Thread {

	private static final Logger myLogger = LoggerFactory.getLogger(SampleSocketServer.class);
	private ServerSocket serverSocket;
	private static Socket server;

	public SampleSocketServer() throws IOException {

		int port = Integer.parseInt(GenericUtil.readServerPropertyConfigFile("socket.port"));

		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(0);
	}

	public void run() {
		while (true) {
			try {
				server = serverSocket.accept();

				DataInputStream in = new DataInputStream(server.getInputStream());

				System.out.println(
						"Client Ip Address : " + server.getRemoteSocketAddress() + "    Client data : " + in.readUTF());

			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				myLogger.info(
						"\n LocalizedMessage : " + e.getLocalizedMessage() + "\n  		 Message :: " + e.getMessage()
								+ "\n toString :: " + e.toString() + "\n:		 StackTrace :: " + e.getStackTrace());
				break;
			}
		}
	}

	public static void closeSocketServer() throws IOException {
		if (server.isClosed()) {
			myLogger.info("Socket Server is already closed");
		} else {
			myLogger.info("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");
			server.close();
		}
	}

	public static void startSocketServer() {

		try {
			Thread t = new SampleSocketServer();
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {
		startSocketServer();
	}
}