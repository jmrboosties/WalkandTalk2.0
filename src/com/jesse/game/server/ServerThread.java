package com.jesse.game.server;

import java.io.ObjectInputStream;
import java.net.Socket;

import com.jesse.game.utils.Print;

public class ServerThread extends Thread {
	
	private Socket mSocket;
	
	public ServerThread(Server server, Socket socket) {
		super("ServerThread");
		server.addClientSocket(socket);
		mSocket = socket;
	}
	
	public void run() {
		try {
//			ObjectOutputStream objectOut = new ObjectOutputStream(mSocket.getOutputStream());
//			objectOut.flush();
			ObjectInputStream objectIn = new ObjectInputStream(mSocket.getInputStream());		
			
			PlayerHolder fromClient; 
//			PlayerHolder toClient;
			
			while((fromClient = (PlayerHolder) objectIn.readObject()) != null) {
				Print.log(fromClient.toString());
				//TODO check collision for hackers
				
			}
			
//			objectOut.close();
			objectIn.close();
			mSocket.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
