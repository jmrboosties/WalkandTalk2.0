package com.jesse.game.utils;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.jesse.game.objects.Vector2i;

public class MultiServerThread extends Thread {

	private Socket mSocket;
	
	public MultiServerThread(Socket socket) {
		super("MultiServerThread");
		mSocket = socket;
	}
	
	public void run() {
		original();		
	}
	
	private void original() {
		try {
			PrintWriter out = new PrintWriter(mSocket.getOutputStream(), true);
			ObjectInputStream objectIn = new ObjectInputStream(mSocket.getInputStream());		
			
			Vector2i inputPosition; 
			String outputLine;
			
			outputLine = "Use wasd to move around.";
			out.println(outputLine);
			
			while((inputPosition = (Vector2i) objectIn.readObject()) != null) {
				Print.log("input line: " + inputPosition.x + ", " + inputPosition.y);
				outputLine = "you moved to " + inputPosition.toString();
				out.println(outputLine);
				if(outputLine.equals("gay"))
					break;
			}
			
			Print.log("ending");
			
			out.close();
			objectIn.close();
			mSocket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
