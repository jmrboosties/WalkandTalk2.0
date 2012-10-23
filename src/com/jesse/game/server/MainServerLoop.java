package com.jesse.game.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TimerTask;

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
		
		GameState currentState = mServer.getState();
		GameState newState = currentState.next();
		
		for (Command command : mServer.getCommandQueue()) 
			command.execute(newState.getPlayers().get(command.getId()));
		
		mServer.clearCommandQueue();
		
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
		ObjectOutputStream objectOut = null;
		for (Socket socket : mServer.getClientSockets()) {
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			Print.log(newState.toString());
			objectOut.writeObject(newState);
			objectOut.reset();
			objectOut.close();
		}
		
	}

}
