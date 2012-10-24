package com.jesse.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

import com.jesse.game.data.Command;
import com.jesse.game.data.GameState;

public class Server {
	
	private GameState mGameState;
	private ArrayList<Command> mCommandList;
	private ArrayList<Socket> mClientSockets;
	public boolean debugMode = false;
	
	public Server() {
		mGameState = new GameState();
		mCommandList = new ArrayList<Command>();
		mClientSockets = new ArrayList<Socket>();
	}

	public void start() throws IOException {
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
	
	public void addClientSocket(Socket socket) {
		mClientSockets.add(socket);
	}
	
	public ArrayList<Socket> getClientSockets() {
		return mClientSockets;
	}
	
}
