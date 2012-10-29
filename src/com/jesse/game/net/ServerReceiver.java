package com.jesse.game.net;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jesse.game.MyNerdoGame;
import com.jesse.game.data.GameState;
import com.jesse.game.utils.Print;

public class ServerReceiver implements Runnable {

	private BufferedReader mIn;
	private MyNerdoGame mGame;
	
	private boolean mRunning = true;
	
	private JsonParser mParser = new JsonParser();
	private Gson mGson = new Gson();
	
	public ServerReceiver(BufferedReader in, MyNerdoGame game) {
		mIn = in;
		mGame = game;
	}
	
	@Override
	public void run() {
		String serverOutput;
		while(mRunning) {
			serverOutput = null;
			try {
				serverOutput = mIn.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
			if(serverOutput != null) {
				Print.log(serverOutput);
				handleGameState(serverOutput);
			}
		}
	}
	
	private synchronized void handleGameState(String serverOutput) {
		while(mGame.getUpdateState() != null) {
			try {
				wait();
			} catch(InterruptedException e) { }
		}
		Print.log("from server: " + serverOutput);
		if(serverOutput.startsWith("{")) {
			JsonObject jObject = (JsonObject) mParser.parse(serverOutput);
			mGame.setUpdateState(mGson.fromJson(jObject, GameState.class));
		}
		notifyAll();
	}
}
