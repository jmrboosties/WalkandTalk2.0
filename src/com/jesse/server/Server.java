package com.jesse.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.jesse.data.GameSnapshot;
import com.jesse.data.PlayerHolder;
import com.jesse.data.commands.Command;
import com.jesse.data.commands.LeaveCommand;
import com.jesse.game.utils.Constants;
import com.jesse.game.utils.Print;

public class Server {
	
	public Gson gson;
	public JsonParser parser;
	
	private HashMap<Integer, GameSnapshot> mSnapshots;
//	private HashMap<Integer, String> mMessageQueue;
	private HashMap<Integer, HashMap<Integer, String>> mMessageQueues;
	private ArrayList<Command> mCommandList;
	private HashMap<Integer, ArrayList<Socket>> mClientMap;
	private HashMap<Integer, Socket> mNewPlayers;
	private HashBiMap<Socket, PlayerHolder> mPlayerMap;
	private HashMap<Integer, ArrayList<Socket>> mNewClientMap;
	private HashMap<Integer, ArrayList<Integer>> mPlayersLeavingMap;
	public boolean debugMode = false;
	
	public Server() {
		mSnapshots = new HashMap<Integer, GameSnapshot>();
		mCommandList = new ArrayList<Command>();
		mClientMap = new HashMap<Integer, ArrayList<Socket>>();
		mNewClientMap = new HashMap<Integer, ArrayList<Socket>>();
//		mMessageQueue = new HashMap<Integer, String>();
		mMessageQueues = new HashMap<Integer, HashMap<Integer,String>>();
		mNewPlayers = new HashMap<Integer, Socket>();
		mPlayerMap = HashBiMap.<Socket, PlayerHolder>create();
		mPlayersLeavingMap = new HashMap<Integer, ArrayList<Integer>>();
		
		initMapExtras();
		
		parser = new JsonParser();
		gson = new Gson();
	}
	
	private void initMapExtras() {
		for (Integer mapId : Constants.MAPS.keySet()) {
			mSnapshots.put(mapId, new GameSnapshot(mapId));
			mClientMap.put(mapId, new ArrayList<Socket>());
			mNewClientMap.put(mapId, new ArrayList<Socket>());
			mPlayersLeavingMap.put(mapId, new ArrayList<Integer>());
			mMessageQueues.put(mapId, new HashMap<Integer, String>());
		}
		
//		mMessageQueues.put(-1, value)
	}

	public void start() throws IOException {
		new Thread(new InputHandler()).start();
		
		ServerSocket socket = new ServerSocket(7377);
		boolean listening = true;

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MainServerLoop(this), 0, 50);
		
		while(listening)
			new ServerThread(this, socket.accept()).start();
		
		socket.close();
	}
	
	public GameSnapshot getState(int mapId) {
		return mSnapshots.get(mapId);
	}
	
	public void setState(GameSnapshot state, int mapId) {
		mSnapshots.get(mapId).update(state, this);
	}
	
	public void addCommand(Command command) {
		mCommandList.add(command);
	}
	
	public ArrayList<Command> getCommandQueue() {
		return mCommandList;
	}
	
	public void clearCommandQueue() {
		mCommandList.clear();
	}
	
	public ArrayList<Socket> getClientSockets(int mapId) {
		return mClientMap.get(mapId);
	}
	
	public void addNewClientSocket(Socket socket, int mapId) {
		mNewClientMap.get(mapId).add(socket);
	}
	
	public ArrayList<Socket> getNewClientSockets(int mapId) {
		return mNewClientMap.get(mapId);
	}
	
	public void dumpNewClientsIntoRegular(int mapId) {
		ArrayList<Socket> sockets = mNewClientMap.get(mapId);
		
		for (Socket socket : sockets)
			if(!mClientMap.get(mapId).contains(socket))
				mClientMap.get(mapId).add(socket);
			
		sockets.clear();
	}
	
	public void addMessageToQueue(int mapId, int playerId, String message) {
		mMessageQueues.get(mapId).put(playerId, message);
	}
	
	public HashMap<Integer, String> getMessageQueue(int mapId) {
		return mMessageQueues.get(mapId);
	}
	
	public void clearMessageQueue(int mapId) {
//		mMessageQueue.clear();
		mMessageQueues.get(mapId).clear();
	}
	
	public void addToNewPlayerMap(Integer playerId, Socket socket) {
		mNewPlayers.put(playerId, socket);
	}
	
	public HashMap<Integer, Socket> getNewPlayerMap() {
		return mNewPlayers;
	}
	
	public void transferPlayerFromNew(PlayerHolder holder) {
		Socket socket = mNewPlayers.get(holder.getId());
		mPlayerMap.put(socket, holder);
		mNewPlayers.remove(holder.getId());
	}
	
	public void flagPlayerSocketAsNewToArea(PlayerHolder holder) {
		
	}
	
	public void dropPlayer(Socket socket) {
		PlayerHolder player = mPlayerMap.get(socket);
		mCommandList.add(new LeaveCommand(player));
		mPlayerMap.remove(socket);
		mClientMap.get(player.getMapId()).remove(socket);
	}
	
	public void addLeavingPlayerId(int id, int mapId) {
		mPlayersLeavingMap.get(mapId).add(id);
	}
	
	public ArrayList<Integer> getLeavingPlayersFromMap(int mapId) {
		return mPlayersLeavingMap.get(mapId);
	}
	
	public void clearLeavingPlayersFromMap(int mapId) {
		mPlayersLeavingMap.get(mapId).clear();
	}
	
	public HashMap<Integer, GameSnapshot> getSnapshots() {
		return mSnapshots;
	}
	
	public void warpPlayer(PlayerHolder player, int mapId) {
		GameSnapshot snapshot = mSnapshots.get(mapId);
		snapshot.removePlayer(player.getId()); //TODO uhhh
		mSnapshots.get(player.getMapId()).addPlayer(player);
	}
	
	private class InputHandler implements Runnable {

		@Override
		public void run() {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while(true)
				try {
					takeInput(reader);
				} catch (IOException e) {
					e.printStackTrace();
				}
			
		}
		
		private void takeInput(BufferedReader reader) throws IOException {
			String entry = reader.readLine().substring(0, 1);
			if(entry.equals("x"))
				System.exit(0);
			else if(entry.equals("c"))
				Print.log(mPlayerMap.toString());
			else if(entry.equals("v"))
				Print.log(mSnapshots.toString());
			else if(entry.equals("b"))
				Print.log(mClientMap.toString());
			else if(entry.equals("n"))
				Print.log(mNewClientMap.toString());
		}
		
	}
	
}
