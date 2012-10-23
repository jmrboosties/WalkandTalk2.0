package jesse.com.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

public class Server {
	
	private GameState mGameState;
	private ArrayList<Command> mCommandList;
	public static Socket clientSocket;
	
	public Server() {
		mGameState = new GameState();
		mCommandList = new ArrayList<Command>();
	}

	public void start() throws IOException {		
		ServerSocket socket = new ServerSocket(7377);
		boolean listening = true;
		
		while(listening)
			new ServerThread(socket.accept()).start();
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MainServerLoop(this), 0, 50);
		
		socket.close();
	}
	
	public GameState getState() {
		return mGameState;
	}
	
	public void setState(GameState state) {
		mGameState = state;
	}
	
	public void addCommand(Command command) {
		mCommandList.add(command);
	}
	
	public ArrayList<Command> getCommandQueue() {
		return mCommandList;
	}
	
	public void clearCommandQueue() {
		mCommandList.clear();
	}
	
}
