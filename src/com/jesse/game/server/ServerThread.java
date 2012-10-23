package com.jesse.game.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.jesse.game.data.Command;
import com.jesse.game.utils.Print;

public class ServerThread extends Thread {
	
	private Socket mSocket;
	private Server mServer;
	
	public ServerThread(Server server, Socket socket) {
		super("ServerThread");
		mServer = server;
		mSocket = socket;
		mServer.addClientSocket(socket);
	}
	
	public void run() {
		try {
			ObjectOutputStream objectOut = new ObjectOutputStream(mSocket.getOutputStream());
			objectOut.flush();
			ObjectInputStream objectIn = new ObjectInputStream(mSocket.getInputStream());
			
//			PlayerHolder fromClient;
//			PlayerHolder toClient;
			Command command;
			
			while((command = (Command) objectIn.readObject()) != null) {
				Print.log(command.toString());
//				if(!mServer.getState().getPlayers().containsKey(fromClient.getId()))
//					mServer.getState().getPlayers().put(fromClient.getId(), fromClient);
				
				mServer.addCommand(command);
				//TODO check collision for hackers
				
			}
			
			objectOut.close();
			objectIn.close();
			mSocket.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
