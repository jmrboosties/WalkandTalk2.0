package com.jesse.game.data;

import com.google.gson.Gson;
import com.jesse.game.utils.Constants.Direction;
import com.jesse.game.utils.Constants.State;
import com.jesse.game.utils.Print;

public class MoveCommand extends Command {
	
	private static final long serialVersionUID = 1L;
	
	private Direction mDirection;
	private State mState;
	
	public MoveCommand(int playerId) {
		this(null, null, playerId);
	}
	
	public MoveCommand(Direction direction, State state, int playerId) {
		mDirection = direction;
		mState = state;
		mPlayerId = playerId;
	}
	
	@Override
	public void execute(PlayerHolder player) {
		//TODO no collision check yet
		
		switch(mDirection) {
		case DOWN :
			player.coordinates.y++;
			break;
		case RIGHT :
			player.coordinates.x++;
			break;
		case LEFT :
			player.coordinates.x--;
			break;
		case UP :
			player.coordinates.y--;
			break;
		}
		
		Print.log("Command result:" + player.toString());
		player.setState(mState);
	}

	@Override
	public String toString() {
		return "Player id " + mPlayerId + " wants to " + mState + " to the " + mDirection;
	}

	@Override
	public String getGson(boolean... bs) {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}	
	
}
