package com.jesse.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.TimerTask;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.jesse.game.data.Command;
import com.jesse.game.data.GameState;
import com.jesse.game.data.PlayerHolder;
import com.jesse.game.utils.Print;
import com.jesse.game.utils.Constants.State;

public class MainServerLoop extends TimerTask {
	
	private Server mServer;
	private long mLoopCount = 0;
	
	public MainServerLoop(Server server) {
		mServer = server;
	}
	
	@Override
	public void run() {
		mLoopCount++;
		
		GameState currentState = mServer.getState();
		GameState newState = currentState.next();
		
		boolean commandsRun = false;
		PlayerHolder holder = null;
		for (Command command : mServer.getCommandQueue()) {
			switch(command.getCommandType()) {
			case Command.COMMAND_JOIN :
				Print.log(command.toString());
				holder = new PlayerHolder(command.getPlayerId());
				newState.addPlayer(holder);
				break;
			case Command.COMMAND_MOVE :
				Print.log(command.toString());
				holder = newState.getPlayers().get(command.getPlayerId());
				break;
			}
			if(holder != null)
				command.execute(holder);
			commandsRun = true;
			
		}
		
		mServer.clearCommandQueue();
		
		if(commandsRun)
			try {
				publishStateToClients(newState, currentState);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		mServer.setState(newState);
		if(mServer.debugMode)
			Print.log("Loop: " + mLoopCount);
	}
	
	private void publishStateToClients(GameState newState, GameState oldState) throws IOException {
		if(newState.equals(oldState))
			return;
		
		JsonObject stateJson = new JsonObject();
		JsonObject playersJson = new JsonObject();
		
		JsonObject jObj = null;
		PlayerHolder player;
		int key;
		for (Entry<Integer, PlayerHolder> entry : newState.getPlayers().entrySet()) {
			key = entry.getKey();
			player = entry.getValue();
			if(!oldState.getPlayers().containsKey(key)) {
				jObj = player.getGson();
			}
			else if(!player.equals(oldState.getPlayers().get(key))) {
				if(!player.coordinates.equals(oldState.getPlayers().get(key).coordinates)) {
					jObj = player.getGson(true, false, true, false);
				}
			}
			
			if(jObj != null)
				playersJson.add(String.valueOf(key), jObj);
			
			jObj = null;
			player.setState(State.IDLE); //TODO trial
		}
		
		stateJson.add("mPlayers", playersJson);
		
		PrintWriter out;
		for (Socket socket : mServer.getClientSockets()) {
			out = new PrintWriter(socket.getOutputStream(), true);
			String json = stateJson.toString();
			Print.log("sending to clients: "+ json);
			out.println(json);
		}
		
		for (Socket socket : mServer.getNewClientSockets()) {
			out = new PrintWriter(socket.getOutputStream(), true);
			String json = mServer.gson.toJson(newState);
			Print.log("sending to new clients: " + json);
			out.println(json);
		}
		
		mServer.dumpNewClientsIntoRegular();
		
	}

}
