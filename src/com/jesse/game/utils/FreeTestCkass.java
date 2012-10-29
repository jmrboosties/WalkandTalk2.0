package com.jesse.game.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jesse.game.data.GameState;
import com.jesse.game.data.MoveCommand;
import com.jesse.game.data.PlayerHolder;
import com.jesse.game.objects.Vector2i;
import com.jesse.game.utils.Constants.Direction;
import com.jesse.game.utils.Constants.State;

public class FreeTestCkass {

//	private static JsonParser parser = new JsonParser();
//	private static Gson gson = new Gson();
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		stupid();
//		hyoer();
//		timer();
		testMovement();
	}
	
	private static void timer() {
		Vector2i vec = new Vector2i(54, 40);
		Vector2i to = new Vector2i(50, 40);
		
	}
	
	private static void hyoer() {
		PlayerHolder original = new PlayerHolder(0, new Vector2i(3, 5), "lone fucking behold");
		PlayerHolder copy = new PlayerHolder(original);
		
		Print.log(original.equals(copy) + "");
		
		copy.coordinates.x++;
		copy.setState(State.RUN);
		
		Print.log(original.equals(copy) + "");
		
		copy.getGson();
		long time = System.currentTimeMillis();
		copy.getGson();
		Print.log(System.currentTimeMillis() - time + "");
		
	}
	
	private static void stupid() {
		
		PlayerHolder original = new PlayerHolder(0, new Vector2i(3, 5), "lone fucking behold");
		PlayerHolder holder = new PlayerHolder(56, new Vector2i(3,4), "im gay");
		PlayerHolder jon = new PlayerHolder(54, new Vector2i(3,4), "im ga");
		PlayerHolder hoder = new PlayerHolder(26, new Vector2i(60,4), "gof");
		
		GameState oldState = new GameState();
		oldState.addPlayer(holder);
		oldState.addPlayer(hoder);
		
		Gson gson = new Gson();
		gson.toJson(oldState);
		new JsonObject().addProperty("a", "a");
		
		GameState newState = oldState.next();
		newState.getPlayers().get(56).coordinates.x++;
		newState.addPlayer(original);
		newState.addPlayer(jon);
		int i = 0;
		while(i < 10) {
			i++;
			long time = System.currentTimeMillis();
			JsonObject stateJson = new JsonObject();
			JsonObject playersJson = new JsonObject();
	//		stateJson.add("mPlayers", null);
	//		Print.log(stateJson.toString());
			
			JsonObject jObj = null;
			PlayerHolder player;
			int key;
			for (Entry<Integer, PlayerHolder> entry : newState.getPlayers().entrySet()) {
				key = entry.getKey();
				player = entry.getValue();
				if(!oldState.getPlayers().containsKey(key)) {
					jObj = player.getGson();
				}
				else if(!player.equals(oldState.getPlayers().get(key))) {
					//Time to find out what is different. key and name never change.
					if(!player.coordinates.equals(oldState.getPlayers().get(key).coordinates)) {
						jObj = player.getGson(true, false, true, false);
					}
				}
				
				if(jObj != null)
					playersJson.add(String.valueOf(key), jObj);
				
				jObj = null;
			}
			
			stateJson.add("mPlayers", playersJson);
			Print.log(System.currentTimeMillis() - time + "");
			Print.log(stateJson.toString());
			Print.log(new Gson().toJson(newState));
		}
	}
	
	private static void gaylord() {
		PlayerHolder original = new PlayerHolder(0, new Vector2i(3, 5), "lone fucking behold");
		PlayerHolder holder = new PlayerHolder(56, new Vector2i(3,4), "im gay");

		GameState state = new GameState();
		state.addPlayer(holder);
		state.addPlayer(original);
		
//		Print.log(new Gson().toJson(state));
		
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
		new Client().run();
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
