package jesse.com.game.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.TimerTask;

public class MainServerLoop extends TimerTask {

	private Server mServer;
	private long mLoopCount = 0;
	
	public MainServerLoop(Server server) {
		mServer = server;
	}
	
	@Override
	public void run() {
		mLoopCount++;
		
		GameState currentState = mServer.getState();
		GameState newState = currentState.next();
		
		for (Command command : mServer.getCommandQueue()) 
			command.execute(newState.getPlayers().get(command.getId()));
		
		mServer.clearCommandQueue();
		
		try {
			publishStateToClients(newState, currentState);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		mServer.setState(newState);
	}
	
	private void publishStateToClients(GameState newState, GameState oldState) throws IOException {
		ObjectOutputStream objectOut = new ObjectOutputStream(Server.clientSocket.getOutputStream());
		objectOut.writeObject(newState);
		objectOut.reset();
		objectOut.close();
	}

}
