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
import com.jesse.game.data.MoveCommand;
import com.jesse.game.data.PlayerHolder;
import com.jesse.game.objects.Vector2i;
import com.jesse.game.utils.Constants.Direction;
import com.jesse.game.utils.Constants.State;

public class FreeTestCkass {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		stupid();
//		hyoer();
	}
	
	private static void hyoer() {
		PlayerHolder original = new PlayerHolder(0, new Vector2i(3, 5), "lone fucking behold");
		PlayerHolder copy = new PlayerHolder(original);
		
		Print.log(original.equals(copy) + "");
		
		copy.coordinates.x++;
//		copy.setState(State.RUN);
		
		Print.log(original.equals(copy) + "");
	}
	
	private static void stupid() {
		PlayerHolder original = new PlayerHolder(0, new Vector2i(3, 5), "lone fucking behold");
		PlayerHolder holder = new PlayerHolder(56, new Vector2i(3,4), "im gay");
		PlayerHolder jon = new PlayerHolder(54, new Vector2i(3,4), "im ga");
		PlayerHolder hoder = new PlayerHolder(26, new Vector2i(3,4), "gof");
//		PlayerHolder original1 = new PlayerHolder(10, new Vector2i(3, 5), "lone fucking behold");
//		PlayerHolder holder1 = new PlayerHolder(561, new Vector2i(3,4), "im gay");
//		PlayerHolder jon1 = new PlayerHolder(541, new Vector2i(3,4), "im ga");
//		PlayerHolder hoder1 = new PlayerHolder(261, new Vector2i(3,4), "gof");
		
		GameState oldState = new GameState();		
		oldState.addPlayer(holder);
		oldState.addPlayer(original);
		oldState.addPlayer(hoder);
		oldState.addPlayer(jon);
//		oldState.addPlayer(holder1);
//		oldState.addPlayer(original1);
//		oldState.addPlayer(hoder1);
//		oldState.addPlayer(jon1);
		
		
		GameState newState = oldState.next();
		Print.log(oldState.toString());
		Print.log(newState.toString());
		newState.getPlayers().get(0).coordinates.x++;
		long time = System.nanoTime();
		boolean bool = newState.equals(oldState);
		Print.log(System.nanoTime() - time + "");
		Print.log(bool + "");
		Print.log(newState.toString());
		Print.log(oldState.toString());
		
//		JsonObject jObj;
//		PlayerHolder player;
//		int key;
//		for (Entry<Integer, PlayerHolder> entry : newState.getPlayers().entrySet()) {
//			key = entry.getKey();
//			player = entry.getValue();
//			
//			if(!oldState.getPlayers().containsKey(key)) {
//				//Add a FULL player to the data packet
//			}
//			else if(!player.equals(oldState.getPlayers().get(key))) {
//				//Time to find out what is different. key and name never change.
//				if(!player.coordinates.equals(oldState.getPlayers().get(key).coordinates)) {
//					jObj = (JsonObject) parser.parse(player.getGson(true, false, true, false));
//				}
//			}			
//		}	
	}
	
	private static void gaylord() {
		PlayerHolder original = new PlayerHolder(0, new Vector2i(3, 5), "lone fucking behold");
		PlayerHolder holder = new PlayerHolder(56, new Vector2i(3,4), "im gay");

		GameState state = new GameState();
		state.addPlayer(holder);
		state.addPlayer(original);
		
		Print.log(new Gson().toJson(state));
		
//		String gson = holder.getGson(false, false, true);
//		JsonObject job = (JsonObject) new JsonParser().parse(gson);
//		Print.log(job.toString());
//		PlayerHolder senior = new Gson().fromJson(job, PlayerHolder.class);
		
//		String vecstr = job.getAsJsonObject("coordinates").toString();
//		Print.log(vecstr);
//		Print.log(holder.coordinates.getGson());
//		Vector2i vec = new Gson().fromJson(vecstr, Vector2i.class);
//		Print.log(vec.toString());
//		Print.log(vec.equals(holder.coordinates) + "");
//		PlayerHolder senior = new Gson().fromJson(job, PlayerHolder.class);
//		Print.log(senior.toString());
	}
	
	private static void testMovement() throws IOException, ClassNotFoundException {
		Socket socket = null;
		PrintWriter out = null;
//		ObjectOutputStream objectOut = null;
//		ObjectInputStream objectIn = null;
		BufferedReader in = null;
		
		try {
			
			socket = new Socket("localhost", 7377);
//			objectOut = new ObjectOutputStream(socket.getOutputStream());
			out = new PrintWriter(socket.getOutputStream(), true);
//			objectIn = new ObjectInputStream(socket.getInputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String serverOutput;
		PlayerHolder holder = new PlayerHolder(0, new Vector2i(), "jesse");
		Command command;
		boolean sending = true;
		JsonParser parser = new JsonParser();
		Print.log("entering the while, wish me luck men");
		while(sending) {
			serverOutput = in.readLine();
			if(serverOutput != null)
				handleGameState(serverOutput);
				
			command = takeInput(holder, reader);
			String gson = command.getGson();
			JsonObject jObj = new JsonObject();
			jObj.addProperty("command_type", 1);
			jObj.add("command", parser.parse(gson));
			Print.log("the data packet: " + jObj.toString());
			out.println(jObj.toString());
//			objectOut.writeObject(command);
//			objectOut.reset();
		}
		
//		objectOut.close();
//		objectIn.close();
		in.close();
		out.close();
		reader.close();
		socket.close();
	}
	
	private static void handleGameState(String serverOutput) {
		
	}
	
	private static Vector2i calculatePosition(Vector2i position, BufferedReader reader) throws IOException {
		Print.log("i just got this: " + position.toString());
		String entry = reader.readLine().substring(0, 1);
		if(entry.equals("w"))
			position.y++;
		else if(entry.equals("s"))
			position.y--;
		else if(entry.equals("a"))
			position.x--;
		else if(entry.equals("d"))
			position.x++;
		
		return position;
	}
	
	private static MoveCommand takeInput(PlayerHolder holder, BufferedReader reader) throws IOException {
		Direction direction = null;
		String entry = reader.readLine().substring(0, 1);
		if(entry.equals("w"))
//			holder.coordinates.y++;
			direction = Direction.UP;
		else if(entry.equals("s"))
//			holder.coordinates.y--;
			direction = Direction.DOWN;
		else if(entry.equals("a"))
//			holder.coordinates.x--;
			direction = Direction.LEFT;
		else if(entry.equals("d"))
//			holder.coordinates.x++;
			direction = Direction.RIGHT;
		
		return new MoveCommand(direction, State.WALK, holder.getId());		
	}
	
	private static void basicComm() throws IOException {
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
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String userInput, serverOutput;
		
		while((serverOutput = in.readLine()) != null) {
			Print.log("Server says: " + serverOutput);
			
			if(serverOutput.equals("gay"))
				break;
			
			userInput = reader.readLine();
			if(userInput != null) {
				Print.log("Client: " + userInput);
				out.println(userInput);
			}
		}
		
		Print.log("this is ending");
		
		out.close();
		in.close();
		reader.close();
		socket.close();
	}
	
}
