package com.jesse.game.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.jesse.game.objects.Vector2i;

public class PretendoServer {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		multiServer();
	}
	
	private static void multiServer() throws IOException {
		ServerSocket socket = null;
		boolean listening = true;
		
		try {
			socket = new ServerSocket(7377);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("hey lets begin");
		
		while(listening)
			new MultiServerThread(socket.accept()).start();
		
		socket.close();
	}
	
	private static void position() throws IOException, ClassNotFoundException {
		System.out.println("hey lets begin");
		
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(7377);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Socket clientSocket = null;
		try {
			clientSocket = socket.accept();
			System.out.println("found him");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		ObjectInputStream objectIn = new ObjectInputStream(clientSocket.getInputStream());		
		
		Vector2i inputPosition; 
		String outputLine;
		
		JokeHandler handler = new JokeHandler();
		
		outputLine = "Use wasd to move around.";
		out.println(outputLine);
		
		while((inputPosition = (Vector2i) objectIn.readObject()) != null) {
			Print.log("input line: " + inputPosition.x + ", " + inputPosition.y);
			outputLine = "you moved to " + inputPosition.toString();
			out.println(outputLine);
			if(outputLine.equals("gay"))
				break;
			
//			inputPosition
		}
		
		Print.log("ending");
		
		out.close();
		objectIn.close();
		clientSocket.close();
		socket.close();
	}
	
	private static void basicComm() throws IOException {
		System.out.println("hey lets begin");
		
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(7377);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Socket clientSocket = null;
		try {
			clientSocket = socket.accept();
			System.out.println("found him");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		String inputLine, outputLine;
		
		JokeHandler handler = new JokeHandler();
		
		outputLine = handler.processInput(null);
		out.println(outputLine);
		
		while((inputLine = in.readLine()) != null) {
			Print.log("input line: " + inputLine);
			outputLine = handler.processInput(inputLine);
			out.println(outputLine);
			if(outputLine.equals("gay"))
				break;
		}
		
		Print.log("ending");
		
		out.close();
		in.close();
		clientSocket.close();
		socket.close();
	}
	
}
