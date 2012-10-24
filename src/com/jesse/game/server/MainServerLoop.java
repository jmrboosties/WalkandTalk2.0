package com.jesse.game.server;

import java.io.IOException;
import java.util.TimerTask;

import com.jesse.game.data.Command;
import com.jesse.game.data.GameState;
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
		
//		Print.log("beginning of loop # " + mLoopCount + ": " + mServer.getState().toString());
		
		GameState currentState = mServer.getState();
		GameState newState = currentState.next();
		
		boolean commandsRun = false;
		for (Command command : mServer.getCommandQueue()) {
			command.execute(newState.getPlayers().get(command.getPlayerId()));
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
//		ObjectOutputStream objectOut = null;
//		for (Socket socket : mServer.getClientSockets()) {
//			objectOut = new ObjectOutputStream(socket.getOutputStream());
//		if(newState.getPlayers().size() > 0)
			Print.log(newState.toString());
//			objectOut.writeObject(newState);
//			objectOut.reset();
//			objectOut.close();
//		}
		
	}

}
