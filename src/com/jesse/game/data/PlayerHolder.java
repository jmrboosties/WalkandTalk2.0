package com.jesse.game.data;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.gson.JsonObject;
import com.jesse.game.objects.Vector2i;
import com.jesse.game.utils.Constants.State;
import com.jesse.game.utils.Constants;
import com.jesse.game.utils.Gsonable;

public class PlayerHolder implements Serializable, Gsonable {

	private static final long serialVersionUID = 1L;

	public static final int COORDINATES = 0;
	public static final int NAME = 1;
	public static final int STATE = 2;
	public static final int ID = 3;
	public static final int MAP_ID = 4;
	
	public Vector2i coordinates;
	private int mMapId = -1;
	private int mId = -1;
	private String mName;
	private State mState;
	
	public PlayerHolder() { }
	
	public PlayerHolder(int id) {
		this(id, new Vector2i());
	}
	
	public PlayerHolder(int id, Vector2i coords) {
		this(id, coords, "Default", -1);
	}
	
	public PlayerHolder(int id, Vector2i coords, String name, int mapId) {
		this(id, coords, name, State.IDLE, mapId);
	}
	
	public PlayerHolder(int id, Vector2i coords, String name, State state, int mapId) {
		mId = id;
		coordinates = coords;
		mName = name;
		mState = state;
		mMapId = mapId;
	}
	
	public PlayerHolder(PlayerHolder original) {
		this(original.getId(), new Vector2i(original.coordinates), original.getName(), original.getState(), original.getMapId());
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public int getId() {
		return mId;
	}
	
	public void setState(State state) {
		mState = state;
	}
	
	public State getState() {
		State state = mState;
		return state;
	}
	
	public void resetState() {
		mState = State.IDLE;
	}
	
	public int getMapId() {
		return mMapId;
	}
	
	public void setMapId(int mapId) {
		mMapId = mapId;
	}
	
	public String toString() {
		return mId + ": " + mName + " @ " + coordinates + " with state " + mState + " on map " + Constants.MAPS.get(mMapId);
	}
	
	public boolean equals(Object o) {
		try {
			PlayerHolder compared = (PlayerHolder) o;
//			if(compared.hashCode() == hashCode())
//				return true;
//			else
//				return false;
			if(coordinates.equals(compared.coordinates)
					&& mName.equals(compared.getName())
					&& mId == compared.getId()
					&& mState.equals(compared.getState())
					&& mMapId == compared.getMapId())
				return true;
			else
				return false;
		} catch(ClassCastException e) {
			return false;
		}
	}
	
	public int hashCode() {
		return new HashCodeBuilder(7, 11)
			.append(mId)
			.append(mState)
			.append(mName)
			.append(coordinates)
			.append(mMapId)
			.toHashCode();		
	}

	@Override
	public JsonObject getGson(boolean... bs) {
		JsonObject json = new JsonObject();
		if(bs.length > 0 && bs.length < 5)
			throw new IllegalArgumentException("Either pass 5 or no booleans.");
		
		if(bs != null && bs.length > 0) {
			if(bs[COORDINATES])
				json.add("coordinates", coordinates.getGson());
			if(bs[NAME])
				json.addProperty("mName", mName);
			if(bs[STATE])
				json.addProperty("mState", mState.toString());
			if(bs[ID])
				json.addProperty("mId", mId);
			if(bs[MAP_ID])
				json.addProperty("mMapId", mMapId);
		}
		else {
			json.add("coordinates", coordinates.getGson());
			json.addProperty("mName", mName);
			json.addProperty("mState", mState.toString());
			json.addProperty("mMapId", mMapId);
			json.addProperty("mId", mId); //TODO remove when you write your own json->object thing, use key
		}
		
		return json;
	}
	
	public void setId(int i) {
		mId = i;
	}
	
	public void update(PlayerHolder holder) {
		if(holder.getId() != mId)
			return;
		
		if(holder.getName() != null)
			mName = holder.getName();
		if(holder.getState() != null)
			mState = holder.getState();
		if(holder.coordinates != null)
			coordinates = holder.coordinates;
		if(holder.getMapId() > 0)
			mMapId = holder.getMapId();
		
	}
	
}
