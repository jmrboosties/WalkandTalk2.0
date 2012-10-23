package com.jesse.game.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import jesse.com.game.server.Constants.State;
import jesse.com.game.server.GameState;
import jesse.com.game.server.PlayerHolder;

import com.jesse.game.objects.Vector2i;

public class FreeTestCkass {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		testMovement();
	}
	
	public static State gay() {
		State state = State.IDLE;
		State newState = state;
		newState = State.RUN;
		return state;
	}
	
	private static void testMovement() throws IOException, ClassNotFoundException {
		Socket socket = null;
		ObjectOutputStream objectOut = null;
		ObjectInputStream objectIn = null;
//		BufferedReader in = null;
		
		try {
			
			socket = new Socket("localhost", 7377);
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			objectOut.flush();
			objectIn = new ObjectInputStream(socket.getInputStream());
//			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
//		String serverOutput;
//		Vector2i position = new Vector2i(5, 5);
		PlayerHolder holder = new PlayerHolder(0, new Vector2i(), "jesse");
		boolean sending = true;
		GameState state;
		Print.log("entering the while, wish me luck men");
		while((state = (GameState) objectIn.readObject()) != null) {
//			
//			if(state != null) {
				Print.log(state.toString());
//			}
//			Print.log("Server says: " + serverOutput);
//			
//			if(serverOutput.equals("gay"))
//				break;

//			holder = calculatePosition(holder, reader);
			holder = takeInput(holder, reader);
			Print.log("sending over: " + holder.toString());
			objectOut.writeObject(holder);
			objectOut.reset();
		}
		
		objectOut.close();
		objectIn.close();
//		in.close();
		reader.close();
		socket.close();
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
	
	private static PlayerHolder takeInput(PlayerHolder holder, BufferedReader reader) throws IOException {
				
		String entry = reader.readLine().substring(0, 1);
		if(entry.equals("w"))
			holder.coordinates.y++;
		else if(entry.equals("s"))
			holder.coordinates.y--;
		else if(entry.equals("a"))
			holder.coordinates.x--;
		else if(entry.equals("d"))
			holder.coordinates.x++;
		
		return holder;
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
