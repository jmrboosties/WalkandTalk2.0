package com.jesse.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.jesse.game.data.Command;
import com.jesse.game.data.GameSnapshot;
import com.jesse.game.data.MessageCommand;
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
		
		GameSnapshot currentState = mServer.getState();
		GameSnapshot newState = currentState.next();
		
		boolean commandsRun = false;
		PlayerHolder holder = null;
		for (Command command : mServer.getCommandQueue()) {
			Print.log(command.toString());
			switch(command.getCommandType()) {
			case Command.COMMAND_JOIN :
				holder = new PlayerHolder(command.getPlayerId());
				newState.addPlayer(holder);
				break;
			case Command.COMMAND_MOVE :
				holder = newState.getPlayers().get(command.getPlayerId());
				break;
			case Command.COMMAND_MESSAGE :
				MessageCommand msgCommand = (MessageCommand)command;
				mServer.addMessageToQueue(msgCommand.getPlayerId(), msgCommand.getMessage());
				holder = null;
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
	
	private void publishStateToClients(GameSnapshot newState, GameSnapshot oldState) throws IOException {

		JsonObject stateJson = null;
		
		if(!newState.equals(oldState)) {
			stateJson = new JsonObject();
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
				player.setState(State.IDLE);
			}
			
			stateJson.add("mPlayers", playersJson);
		}
		
		PrintWriter out;
		JsonObject jContainer = new JsonObject();
		
		jContainer.add("messages", mServer.gson.toJsonTree(mServer.getMessageQueue(), HashMap.class));
		mServer.clearMessageQueue();
		
		if(stateJson != null) {
			String json = stateJson.toString();
			jContainer.add("snapshot", mServer.parser.parse(json));
		}

		Print.log("sending to clients: " + jContainer.toString());
		for (Socket socket : mServer.getClientSockets()) {
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println(jContainer.toString());
		}
		
		if(mServer.getNewClientSockets().size() > 0) {
			String newJson = mServer.gson.toJson(newState);
			JsonObject newPlayersContainer = new JsonObject();
			newPlayersContainer.addProperty("joined", true);
			newPlayersContainer.add("snapshot", mServer.parser.parse(newJson));
//			newJson = "{\"joined\":true,".concat(newJson.substring(1));
			Print.log("sending to new clients: " + newPlayersContainer.toString());
			for (Socket socket : mServer.getNewClientSockets()) {
				out = new PrintWriter(socket.getOutputStream(), true);
				
				out.println(newPlayersContainer.toString());
			}

			mServer.dumpNewClientsIntoRegular();
		}
		
	}

}
