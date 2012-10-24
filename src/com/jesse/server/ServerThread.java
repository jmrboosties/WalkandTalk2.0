package com.jesse.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jesse.game.data.Command;
import com.jesse.game.data.MoveCommand;
import com.jesse.game.utils.Print;

public class ServerThread extends Thread {
	
	private static Gson gson = new Gson();
	private static JsonParser parser = new JsonParser();
	
	private Socket mSocket;
	private Server mServer;
	
	public ServerThread(Server server, Socket socket) {
		super("ServerThread");
		mServer = server;
		mSocket = socket;
		mServer.addClientSocket(socket);
	}
	
	public void run() {
		try {
			PrintWriter out = new PrintWriter(mSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			
			out.println("Hey you're connected");
			
			String commandJsonString;
			Command command;			
			while((commandJsonString = in.readLine()) != null) {
				JsonObject commandJson = (JsonObject) parser.parse(commandJsonString);
				commandJson = commandJson.getAsJsonObject("command");
				command = gson.fromJson(commandJson, MoveCommand.class);
				
				mServer.addCommand(command);
			}
			
			in.close();
			out.close();
			mSocket.close();
			
		} catch(SocketException e) {
			Print.log("Player has unexpectedly left.");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
