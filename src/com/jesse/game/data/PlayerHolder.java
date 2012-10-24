package com.jesse.game.data;

import java.io.Serializable;

import com.jesse.game.objects.Vector2i;
import com.jesse.game.utils.Constants.State;
import com.jesse.game.utils.Gsonable;

public class PlayerHolder implements Serializable, Gsonable {

	private static final long serialVersionUID = 1L;

	public Vector2i coordinates;
	private int mId;
	private String mName;
	private State mState;
	
	public PlayerHolder(int id) {
		this(id, new Vector2i());
	}
	
	public PlayerHolder(int id, Vector2i coords) {
		this(id, coords, "Default");
	}
	
	public PlayerHolder(int id, Vector2i coords, String name) {
		mId = id;
		coordinates = coords;
		mName = name;
	}
	
	public String getName() {
		return mName;
	}
	
	public int getId() {
		return mId;
	}
	
	public void setState(State state) {
		mState = state;
	}
	
	public State getState() {
		State state = mState;
//		mState = State.IDLE;
		return state;
	}
	
	public void resetState() {
		mState = State.IDLE;
	}
	
	public String toString() {
		return mId + ": " + mName + " @ " + coordinates.toString();
	}

	@Override
	public String getGson() {
		// TODO Auto-generated method stub
		return null;
	}
}
