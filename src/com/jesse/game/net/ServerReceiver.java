package com.jesse.game.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jesse.game.GameMain;
import com.jesse.game.data.GameSnapshot;
import com.jesse.game.listeners.ConnectionStatusListener;
import com.jesse.game.utils.Print;

public class ServerReceiver implements Runnable {

	private BufferedReader mIn;
	private GameMain mGame;
	
	private boolean mRunning = true;
	private boolean mJoined = false;
	private boolean mSingleRun = false;
	
//	private ConnectionStatusListener mJoinedListener;
	
	private JsonParser mParser = new JsonParser();
	private Gson mGson = new Gson();
	
	public ServerReceiver(BufferedReader in, GameMain game, ConnectionStatusListener listener, boolean singleRun) {
		mIn = in;
		mGame = game;
//		mJoinedListener = listener;
		mSingleRun = singleRun;
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
			if(mSingleRun)
				return;
		}
	}
	
	@SuppressWarnings("unchecked")
	private synchronized void handleGameState(String serverOutput) {
		while(mGame.getUpdateSnapshot() != null) {
			try {
				wait();
			} catch(InterruptedException e) { }
		}
		Print.log("from server: " + serverOutput);
		if(serverOutput.startsWith("{")) {
			JsonObject jObject = (JsonObject) mParser.parse(serverOutput);
			if(!mJoined) {
				JsonPrimitive preBool = jObject.getAsJsonPrimitive("joined");
				if(preBool != null && preBool.getAsBoolean()) {
//					mJoinedListener.onJoined();
					mJoined = true;
				}
			}
			JsonObject messages = jObject.getAsJsonObject("messages");
			if(messages != null) {
				Print.log("messages: " + messages.toString());
				HashMap<String, String> messageMap = mGson.fromJson(messages, HashMap.class);
				mGame.loadMessageQueue(messageMap);
			}
			
			mGame.setUpdateSnapshot(mGson.fromJson(jObject.getAsJsonObject("snapshot"), GameSnapshot.class));
		}
		notifyAll();
	}
	
	public synchronized void stop() {
		mRunning = false;
	}
	
	public synchronized void hasJoined() {
		mRunning = true;
	}
}
