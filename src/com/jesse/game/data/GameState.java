package com.jesse.game.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.jesse.game.objects.Vector2i;

public class GameState implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private HashMap<Integer, PlayerHolder> mPlayers;
	
	public GameState() {
		mPlayers = new HashMap<Integer, PlayerHolder>();
		mPlayers.put(0, new PlayerHolder(0, new Vector2i(), "jesse"));
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
	
	public void addPlayer(PlayerHolder player) {
		mPlayers.put(player.getId(), player);
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
