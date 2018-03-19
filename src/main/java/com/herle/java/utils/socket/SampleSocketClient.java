package com.herle.java.utils.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.herle.java.utils.GenericUtil;

public class SampleSocketClient {
	
	private static final Logger myLogger = LoggerFactory.getLogger(SampleSocketClient.class);

	public static void main(String[] args) {

		String hostName = GenericUtil.readClientPropertyConfigFile("socket.hostName");
		int port = Integer.parseInt(GenericUtil.readClientPropertyConfigFile("socket.port"));

		try {
			myLogger.info("Connecting to " + hostName + " on port " + port);
			Socket client = new Socket(hostName, port);

			myLogger.info("Connected to " + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);

			out.writeUTF("Hi there!!!");

			client.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
