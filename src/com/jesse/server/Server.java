package com.jesse.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.jesse.game.data.Command;
import com.jesse.game.data.GameState;

public class Server {
	
	public Gson gson;
	public JsonParser parser;
	
	private GameState mGameState;
	private ArrayList<Command> mCommandList;
	private ArrayList<Socket> mClientSockets;
	private ArrayList<Socket> mNewClientSockets;
	public boolean debugMode = false;
	
	public Server() {
		mGameState = new GameState();
		mCommandList = new ArrayList<Command>();
		mClientSockets = new ArrayList<Socket>();
		mNewClientSockets = new ArrayList<Socket>();
		parser = new JsonParser();
		gson = new Gson();
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
	
	public GameState getState() {
		return mGameState;
	}
	
	public void setState(GameState state) {
		mGameState = state;
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
	
	public ArrayList<Socket> getClientSockets() {
		return mClientSockets;
	}
	
	public void addNewClientSocket(Socket socket) {
		mNewClientSockets.add(socket);
	}
	
	public ArrayList<Socket> getNewClientSockets() {
		return mNewClientSockets;
	}
	
	public void dumpNewClientsIntoRegular() {
		for (Socket socket : mNewClientSockets) {
			mClientSockets.add(socket);
		}
		mNewClientSockets.clear();
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
			if(entry.equals("x")) {
				System.exit(0);
			}
		}
		
	}
	
}
