package com.jesse.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TimerTask;

import com.google.gson.JsonObject;
import com.jesse.data.GameSnapshot;
import com.jesse.data.PlayerHolder;
import com.jesse.data.commands.Command;
import com.jesse.data.commands.MessageCommand;
import com.jesse.game.utils.Constants;
import com.jesse.game.utils.Constants.State;
import com.jesse.game.utils.Print;

public class MainServerLoop extends TimerTask {
	
	private Server mServer;
	private long mLoopCount = 0;
	private boolean[] mCommandsRun;
	
	public MainServerLoop(Server server) {
		mServer = server;
		mCommandsRun = new boolean[Constants.MAPS.size()];
	}
	
	@Override
	public void run() {
		mLoopCount++;
		
		HashMap<Integer, GameSnapshot> currentStates = mServer.getSnapshots();
		HashMap<Integer, GameSnapshot> newStates = new HashMap<Integer, GameSnapshot>();
		
		for (Entry<Integer, GameSnapshot> entry : currentStates.entrySet())
			newStates.put(entry.getKey(), entry.getValue().next());
		
		for (int i = 0; i < mCommandsRun.length; i++)
			mCommandsRun[i] = false;
			
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
				mServer.addMessageToQueue(msgCommand.getMapId(), msgCommand.getPlayerId(), msgCommand.getMessage());
				holder = null;
				break;
			case Command.COMMAND_LEAVE :
				newStates.get(command.getMapId()).getPlayers().put(command.getPlayerId(), null);
				mServer.addLeavingPlayerId(command.getPlayerId(), command.getMapId());
				holder = null;
				break;
			}
			
			if(holder != null)
				command.execute(holder);
			
			if(command.getCommandType() == Command.COMMAND_JOIN)
				mServer.transferPlayerFromNew(holder);
			
			if(command.getCommandType() == Command.COMMAND_WARP) {
				
			}
			
			mCommandsRun[command.getMapId()] = true;
		}
		
		mServer.clearCommandQueue();
		
		//TODO: first send all new data to existing clients
		//Then, send full data packet to new clients
		//Separate the warped and joined clients, in packet header
		
		try {
			for (Entry<Integer, GameSnapshot> entry : currentStates.entrySet()) {
				if(mCommandsRun[entry.getKey()]) {
					publishStateToClients(entry.getKey(), newStates.get(entry.getKey()), entry.getValue());
					sendToNewClients(entry.getKey(), newStates.get(entry.getKey()));
					mServer.setState(newStates.get(entry.getKey()), entry.getKey());
				}
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
				
				if(player != null) {
					if(!oldState.getPlayers().containsKey(key))
						jObj = player.getGson();
					else if(!player.equals(oldState.getPlayers().get(key)) && 
						!player.coordinates.equals(oldState.getPlayers().get(key).coordinates))
							jObj = player.getGson(true, false, true, true, true);

					player.setState(State.IDLE);
				}
				
				if(jObj != null)
					playersJson.add(String.valueOf(key), jObj);
				
				jObj = null;
			}
			
			for (Integer id : mServer.getLeavingPlayersFromMap(mapId))
				playersJson.add(String.valueOf(id), null);
			
			mServer.clearLeavingPlayersFromMap(mapId);
			
			stateJson.add("mPlayers", playersJson);
		}
		
		PrintWriter out;
		JsonObject jContainer = new JsonObject();
		
		//To already joined clients
		jContainer.add("messages", mServer.gson.toJsonTree(mServer.getMessageQueue(mapId), HashMap.class));
		mServer.clearMessageQueue(mapId);
		
		if(stateJson != null) {
			String json = stateJson.toString();
			jContainer.add("snapshot", mServer.parser.parse(json));
		}
		
		Print.log("sending to clients of " + Constants.MAPS.get(mapId) + ": " + jContainer.toString());
		
		for (Socket socket : mServer.getClientSockets(mapId)) {
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println(jContainer.toString());
		}
		
		//FIXME old stuff used to be here
	}
	
	private void sendToNewClients(int mapId, GameSnapshot newState) throws IOException {
		//People new as of this tick
		if(mServer.getNewClientSockets(mapId).size() > 0) {
			Print.log("AELRT: " + mServer.getNewClientSockets(mapId).toString());
			
			String newJson = mServer.gson.toJson(newState);
			JsonObject newPlayersContainer = new JsonObject();
			
			newPlayersContainer.addProperty("joined", true);
			newPlayersContainer.add("snapshot", mServer.parser.parse(newJson));
			
			Print.log("sending to new clients of + " + Constants.MAPS.get(mapId) + ": " + newPlayersContainer.toString());
			
			PrintWriter out;
			for (Socket socket : mServer.getNewClientSockets(mapId)) {
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(newPlayersContainer.toString());
			}

			mServer.dumpNewClientsIntoRegular(mapId);
		}
	}

}
