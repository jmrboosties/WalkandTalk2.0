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
import com.jesse.game.data.JoinCommand;
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
			Command command = null;
			while((commandJsonString = in.readLine()) != null) {
				long time = System.currentTimeMillis();
				JsonObject json = (JsonObject) parser.parse(commandJsonString);
				JsonObject commandJson = json.getAsJsonObject("command");
				
				switch(json.getAsJsonPrimitive("command_type").getAsInt()) {
				case Command.COMMAND_JOIN :
					command = gson.fromJson(commandJson, JoinCommand.class);
					break;
				case Command.COMMAND_MOVE :
					command = gson.fromJson(commandJson, MoveCommand.class);
					break;
				}
				
				if(command != null)
					mServer.addCommand(command);
				
				Print.log(System.currentTimeMillis() - time + "");
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
