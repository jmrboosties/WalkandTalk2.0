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
import com.jesse.game.data.MessageCommand;
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
		mServer.addNewClientSocket(mSocket);
	}
	
	public void run() {
		try {
			PrintWriter out = new PrintWriter(mSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			
			String commandJsonString;
			Command command = null;
			int type;
			JsonObject json;
			JsonObject commandJson;
			while((commandJsonString = in.readLine()) != null) {
				json = (JsonObject) parser.parse(commandJsonString);
				commandJson = json.getAsJsonObject("command");
				
				type = json.getAsJsonPrimitive("command_type").getAsInt();
				
				switch(type) {
				case Command.COMMAND_JOIN :
					command = gson.fromJson(commandJson, JoinCommand.class);
					Print.log("socket: " + mSocket.toString());
					mServer.addToNewPlayerMap(json.getAsJsonPrimitive("mPlayerId").getAsInt(), mSocket);
					break;
				case Command.COMMAND_MOVE :
					command = gson.fromJson(commandJson, MoveCommand.class);
					break;
				case Command.COMMAND_MESSAGE :
					command = gson.fromJson(commandJson, MessageCommand.class);
					break;
				}
				
				command.setCommandType(type);
				command.setPlayerId(json.getAsJsonPrimitive("mPlayerId").getAsInt());
				
				if(command != null)
					mServer.addCommand(command);
				
			}
			
			in.close();
			out.close();
			mSocket.close();
			
		} catch(SocketException e) {
			mServer.dropPlayer(mSocket);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
