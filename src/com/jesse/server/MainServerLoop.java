package com.jesse.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TimerTask;

import com.google.gson.JsonObject;
import com.jesse.game.data.GameSnapshot;
import com.jesse.game.data.PlayerHolder;
import com.jesse.game.data.commands.Command;
import com.jesse.game.data.commands.MessageCommand;
import com.jesse.game.utils.Constants;
import com.jesse.game.utils.Constants.State;
import com.jesse.game.utils.Print;

public class MainServerLoop extends TimerTask {
	
	private Server mServer;
	private long mLoopCount = 0;
	
	public MainServerLoop(Server server) {
		mServer = server;
	}
	
	@Override
	public void run() {
		mLoopCount++;
		
//		GameSnapshot currentState = mServer.getState();
//		GameSnapshot newState = currentState.next();
		
		HashMap<Integer, GameSnapshot> currentStates = mServer.getSnapshots();
		HashMap<Integer, GameSnapshot> newStates = new HashMap<Integer, GameSnapshot>();
		
		for (Entry<Integer, GameSnapshot> entry : currentStates.entrySet())
			newStates.put(entry.getKey(), entry.getValue().next());
		
		boolean commandsRun = false;
		PlayerHolder holder = null;
		for (Command command : mServer.getCommandQueue()) {
			Print.log(command.toString());
			switch(command.getCommandType()) {
			case Command.COMMAND_JOIN :
				holder = new PlayerHolder(command.getPlayerId());
				newStates.get(command.getMapId()).addPlayer(holder);
				break;
			case Command.COMMAND_MOVE :
			case Command.COMMAND_WARP :
				holder = newStates.get(command.getMapId()).getPlayers().get(command.getPlayerId());
				break;
			case Command.COMMAND_MESSAGE :
				MessageCommand msgCommand = (MessageCommand)command;
				mServer.addMessageToQueue(msgCommand.getPlayerId(), msgCommand.getMessage());
				holder = null;
				break;
			case Command.COMMAND_LEAVE :
				newStates.get(command.getMapId()).getPlayers().remove(command.getPlayerId());
				mServer.addLeavingPlayerId(command.getPlayerId());
				holder = null;
				break;
			}
			
			if(holder != null)
				command.execute(holder);
			
			if(command.getCommandType() == Command.COMMAND_JOIN)
				mServer.transferPlayerFromNew(holder);
			
			if(command.getCommandType() == Command.COMMAND_WARP) {
				
			}
			
			commandsRun = true;
		}
		
		mServer.clearCommandQueue();
		
		if(commandsRun)
			try {
				for (Entry<Integer, GameSnapshot> entry : currentStates.entrySet()) {
					publishStateToClients(entry.getKey(), newStates.get(entry.getKey()), entry.getValue());
					mServer.setState(newStates.get(entry.getKey()), entry.getKey());
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		if(mServer.debugMode)
			Print.log("Loop: " + mLoopCount);
	}

	private void publishStateToClients(int mapId, GameSnapshot newState, GameSnapshot oldState) throws IOException {

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
						jObj = player.getGson(true, false, true, false, true);
					}
				}
				
				if(jObj != null)
					playersJson.add(String.valueOf(key), jObj);
				
				jObj = null;
				player.setState(State.IDLE);
			}
			
			for (Integer id : mServer.getLeavingPlayers())
				playersJson.add(String.valueOf(id), null);
			
			mServer.clearLeavingPlayers();
			
			stateJson.add("mPlayers", playersJson);
		}
		
		PrintWriter out;
		JsonObject jContainer = new JsonObject();
		
		//To already joined clients
		jContainer.add("messages", mServer.gson.toJsonTree(mServer.getMessageQueue(), HashMap.class));
		mServer.clearMessageQueue();
		
		if(stateJson != null) {
			String json = stateJson.toString();
			jContainer.add("snapshot", mServer.parser.parse(json));
		}
		
		Print.log("sending to clients of + " + Constants.MAPS.get(mapId) + ": " + jContainer.toString());
		
		for (Socket socket : mServer.getClientSockets()) {
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println(jContainer.toString());
		}
		
		//People new as of this tick
		if(mServer.getNewClientSockets().size() > 0) {
			String newJson = mServer.gson.toJson(newState);
			JsonObject newPlayersContainer = new JsonObject();
			
			newPlayersContainer.addProperty("joined", true);
			newPlayersContainer.add("snapshot", mServer.parser.parse(newJson));
			
			Print.log("sending to new clients of + " + Constants.MAPS.get(mapId) + ": " + newPlayersContainer.toString());
			
			for (Socket socket : mServer.getNewClientSockets()) {
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(newPlayersContainer.toString());
			}

			mServer.dumpNewClientsIntoRegular();
		}
		
	}

}
