package jesse.com.game.server;

import java.io.Serializable;

import jesse.com.game.server.Constants.State;

import com.jesse.game.objects.Vector2i;

public class PlayerHolder implements Serializable {

	private static final long serialVersionUID = 1L;

	public Vector2i coordinates;
	public int mId;
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
		mState = State.IDLE;
		return state;
	}
	
	public void resetState() {
		mState = State.IDLE;
	}
	
	public String toString() {
		return mId + ": " + mName + " @ " + coordinates.toString();
	}
}
