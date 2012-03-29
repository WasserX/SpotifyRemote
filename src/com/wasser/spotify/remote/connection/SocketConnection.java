package com.wasser.spotify.remote.connection;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import com.wasser.spotify.remote.player.Commands;
import com.wasser.spotify.remote.player.PlayerProperties;

public class SocketConnection {
	static final int ACCEPTED_LENGTH = 5;
	private static SocketConnection client = null;

	private java.net.Socket socket = null;
	private String address = "192.168.0.100";
	private int port = 15000;
	private InputStreamReader in;
	private PrintWriter out;

	public static SocketConnection getClient() {
		if (client == null)
			client = new SocketConnection();
		return client;
	}

	private SocketConnection() {
		super();
	}

	/**
	 * This method is called automatically. Only call this method if one of the
	 * actions fails and you want to reconnect (maybe using new parameters.)
	 */
	public void connect() {
		try {
			socket = new java.net.Socket(address, port);
			in = new InputStreamReader(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream());
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + e);
			socket = null;
		} catch (IOException e) {
			System.out.println("There was an error creating the Socket: " + e);
			socket = null;
		}
	}

	/**
	 * Sends a command to the server.
	 * 
	 * @param command
	 * @return true if the data was correctly sent. false if there was a socket
	 *         error
	 */
	public boolean sendCommand(Commands command) {
		if (socket == null)
			return false;
		String json = DataParser.getJSONCommand(command);

		String acceptedLength = String.valueOf(json.length());
		while (acceptedLength.length() < ACCEPTED_LENGTH)
			acceptedLength = acceptedLength.concat(" ");

		out.print(acceptedLength);
		out.flush();
		out.print(json);
		out.flush();
		return true;
	}

	/**
	 * Receives a new state as sent by the server.
	 * 
	 * @return a new State or null if there was an error with the socket.
	 */
	public PlayerProperties receiveState() {
		PlayerProperties receivedState = null;
		try {
			if (socket == null || socket.isClosed()) {
				socket = null;
				return null;
			}

			// Read length of JSON Object
			char lengthBuffer[] = new char[ACCEPTED_LENGTH];
			if (in.read(lengthBuffer, 0, ACCEPTED_LENGTH) < ACCEPTED_LENGTH)
				return null; // Broken Pipe
			int length = Integer.parseInt(String.valueOf(lengthBuffer).trim());

			// Read JSON Object of <jsonLength>
			char json[] = new char[length];
			if (in.read(json, 0, length) < length)
				return null; // Broken Pipe
			receivedState = DataParser.getStateFromJSON(String.valueOf(json));
		} catch (Exception e) {
			// Broken Pipe
			System.out.println("Broken Pipe: " + e);
			socket = null;
		}
		return receivedState;
	}

	public boolean isClosed() {
		return (socket == null || socket.isClosed());
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
