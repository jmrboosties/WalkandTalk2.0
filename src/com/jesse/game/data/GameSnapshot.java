package com.jesse.game.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.gson.JsonObject;
import com.jesse.game.GameMain;
import com.jesse.game.utils.Gsonable;
import com.jesse.game.utils.Print;

public class GameSnapshot implements Serializable, Gsonable {

	private static final long serialVersionUID = 1L;
	
	private HashMap<Integer, PlayerHolder> mPlayers;
	
	public GameSnapshot() {
		mPlayers = new HashMap<Integer, PlayerHolder>();
	}
		
	public HashMap<Integer, PlayerHolder> getPlayers() {
		return mPlayers;
	}
	
	public GameSnapshot next() {
		GameSnapshot newState = new GameSnapshot();
		
		for (Entry<Integer, PlayerHolder> entry : mPlayers.entrySet())
			newState.getPlayers().put(Integer.valueOf(entry.getKey()), new PlayerHolder(entry.getValue()));
		
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
			if(entry.getValue() != null)
				value += "\n     " + entry.getValue().toString();
			else
				value += "\n      someone is leaving...";
		}
		value += "\n  End.";
		return value;
	}
	
	public boolean equals(Object o) {
		try {
			GameSnapshot compared = (GameSnapshot) o;
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
	public JsonObject getGson(boolean...bs) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void update(GameSnapshot updateState, GameMain game) {
		int key;
		PlayerHolder player;
		for (Entry<Integer, PlayerHolder> entry : updateState.getPlayers().entrySet()) {
			key = entry.getKey();
			player = entry.getValue();
			
			if(player != null) {
				player.setId(key);
				
				if(mPlayers.containsKey(key))
					//Player exists, update
					mPlayers.get(key).update(player);
				else {
					//New friend
					Print.log(player.getName() + " has joined!");
					mPlayers.put(key, player);
//					game.addSystemMessageToQueue(player.getName() + " has joined!");
				}
			}
			else {
				//Player has left
				Print.log(mPlayers.get(key).getName() + " has left!");
				game.addSystemMessageToQueue(mPlayers.get(key).getName() + " has left!");
				mPlayers.put(key, null);
			}
		}
		
		Print.log(toString());
	}
	
	public void removePlayer(int id) {
//		Print.log(mPlayers.get(id).getName() + " has left!");
		mPlayers.remove(id);
	}

}
