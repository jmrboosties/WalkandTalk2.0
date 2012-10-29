package com.jesse.game.net;

import java.io.PrintWriter;

import com.google.gson.JsonObject;
import com.jesse.game.data.Command;

public class ServerSender implements Runnable{
	
	private PrintWriter mOut;
	private int mPlayerId = -1;
	private Command mCommand;
	
	public ServerSender(PrintWriter out, Command command, int playerId) {
		mOut = out;
		mCommand = command;
		mPlayerId = playerId;
	}
	
	@Override
	public void run() {
		if(mPlayerId < 0)
			throw new IllegalStateException("Need to set player id first");
		
		if(mCommand != null) {
			JsonObject json = mCommand.getGson();
			mOut.println(json);
		}
	}
	
}
