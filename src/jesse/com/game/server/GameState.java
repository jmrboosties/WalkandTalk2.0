package jesse.com.game.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class GameState implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private HashMap<Integer, PlayerHolder> mPlayers;
	
	public GameState() {
		mPlayers = new HashMap<Integer, PlayerHolder>();
	}
	
	@SuppressWarnings("unchecked")
	public GameState(GameState gameState) {
		mPlayers = (HashMap<Integer, PlayerHolder>) gameState.getPlayers().clone();
	}
	
	public HashMap<Integer, PlayerHolder> getPlayers() {
		return mPlayers;
	}
	
	public GameState next() {
		return new GameState(this);
	}
	
	public String toString() {
		String value = "State: ";
		value += "\n  Players: " + mPlayers.size();
		Set<Entry<Integer, PlayerHolder>> set = mPlayers.entrySet();
		for (Entry<Integer, PlayerHolder> entry : set) {
			value += "\n     " + entry.getValue().toString();
		}
		value += "\n  End.";
		return value;
	}

}
