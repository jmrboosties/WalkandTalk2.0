package com.jesse.game.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jesse.game.data.Command;
import com.jesse.game.data.GameState;
import com.jesse.game.data.JoinCommand;
import com.jesse.game.data.MoveCommand;
import com.jesse.game.data.PlayerHolder;
import com.jesse.game.objects.Vector2i;
import com.jesse.game.utils.Constants.Direction;
import com.jesse.game.utils.Constants.State;

public class Client {

	private GameState mGameState;
	private GameState mUpdateState;
	
	private PlayerHolder mThisPlayer;
	
	private JsonParser mParser = new JsonParser();
	private Gson mGson = new Gson();
	
	public void run() throws IOException {
		mGameState = new GameState();
		mThisPlayer = new PlayerHolder(4, new Vector2i(17, 15), "hank");
		mGameState.addPlayer(mThisPlayer);
		
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			socket = new Socket("localhost", 7377);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		new Thread(new ServerReceiver(in)).start();
		new Thread(new ServerSender(out)).start();
		
		JoinCommand join = new JoinCommand(mThisPlayer);
		out.println(join.getGson());
		
		boolean running = true;
		
		while(running) {
			gameLoop();
		}
		
		in.close();
		out.close();
		socket.close();
	}
	
	private synchronized void gameLoop() {
//		while(mUpdateState == null) {
//			try {
//				wait();
//			} catch(InterruptedException e) { }
//		}
		if(mUpdateState != null) {
			mGameState.update(mUpdateState);
			mUpdateState = null;
			notifyAll();
		}
	}
	
	private class ServerReceiver implements Runnable {

		private BufferedReader mIn;
		private boolean mRunning = true;
		
		public ServerReceiver(BufferedReader in) {
			mIn = in;
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
				}
				if(serverOutput != null)
					handleGameState(serverOutput);
				
			}
		}
	}
	
	private class ServerSender implements Runnable {

		private PrintWriter mOut;
		private BufferedReader mReader;
		private boolean mSending = true;
		
		public ServerSender(PrintWriter out) {
			mOut = out;
			mReader = new BufferedReader(new InputStreamReader(System.in));
		}
		
		@Override
		public void run() {
			Command command;
			JsonObject json = null;
			try {
				while(mSending) {
					
					command = takeInput(mThisPlayer.getId(), mReader);
					if(command != null) {
						json = command.getGson();
						mOut.println(json);
					}
					
				}
				mOut.close();
				mReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private synchronized void handleGameState(String serverOutput) {
		while(mUpdateState != null) {
			try {
				wait();
			} catch(InterruptedException e) { }
		}
		Print.log("from server: " + serverOutput);
		if(serverOutput.startsWith("{")) {
			JsonObject jObject = (JsonObject) mParser.parse(serverOutput);
			mUpdateState = mGson.fromJson(jObject, GameState.class);
		}
		notifyAll();
	}
	
	private MoveCommand takeInput(int id, BufferedReader reader) throws IOException {
		Direction direction = null;
		String entry = reader.readLine().substring(0, 1);
		if(entry.equals("w"))
			direction = Direction.UP;
		else if(entry.equals("s"))
			direction = Direction.DOWN;
		else if(entry.equals("a"))
			direction = Direction.LEFT;
		else if(entry.equals("d"))
			direction = Direction.RIGHT;
		else if(entry.equals("x")) {
			Print.log(mGameState.toString());
			return null;
		}
			
		
		return new MoveCommand(direction, State.WALK, id);		
	}
	
}
