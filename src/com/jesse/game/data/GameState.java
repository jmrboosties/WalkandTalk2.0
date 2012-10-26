package com.jesse.game.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jesse.game.utils.Gsonable;
import com.jesse.game.utils.Print;

public class GameState implements Serializable, Gsonable {

	private static final long serialVersionUID = 1L;
	
	private HashMap<Integer, PlayerHolder> mPlayers;
	
	public GameState() {
		mPlayers = new HashMap<Integer, PlayerHolder>();
	}
		
	public HashMap<Integer, PlayerHolder> getPlayers() {
		return mPlayers;
	}
	
	public GameState next() {
		GameState newState = new GameState();
		for (Entry<Integer, PlayerHolder> entry : mPlayers.entrySet()) {
			Print.log("copying");
			newState.getPlayers().put(Integer.valueOf(entry.getKey()), new PlayerHolder(entry.getValue()));
		}
		
		return newState;
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
	
	public boolean equals(Object o) {
		try {
			GameState compared = (GameState) o;
			if(mPlayers.equals(compared.getPlayers()))
				return true;
			else
				return false;
		} catch(ClassCastException e){
			return false;
		}
	}
	
	public int hashCode() {
		return new HashCodeBuilder(19, 23)
			.append(mPlayers)
			.toHashCode();
	}

	@Override
	public String getGson(boolean...bs) {
		// TODO Auto-generated method stub
		return null;
	}

}
