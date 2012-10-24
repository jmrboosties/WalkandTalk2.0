package com.jesse.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.jesse.game.data.Command;
import com.jesse.game.data.GameState;
import com.jesse.game.utils.Print;

public class MainServerLoop extends TimerTask {

	private static Gson gson = new Gson();
	
	private Server mServer;
	private long mLoopCount = 0;
	
	public MainServerLoop(Server server) {
		mServer = server;
	}
	
	@Override
	public void run() {
		mLoopCount++;
		
//		Print.log("beginning of loop # " + mLoopCount + ": " + mServer.getState().toString());
		
		GameState currentState = mServer.getState();
		GameState newState = currentState.next();
		
		boolean commandsRun = false;
		for (Command command : mServer.getCommandQueue()) {
			command.execute(newState.getPlayers().get(command.getPlayerId()));
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
		Print.log(newState.toString());
		PrintWriter out;
		for (Socket socket : mServer.getClientSockets()) {
			out = new PrintWriter(socket.getOutputStream(), true);
			String json = gson.toJson(newState);
			Print.log(json);
			out.println(json);
		}
		
	}

}
