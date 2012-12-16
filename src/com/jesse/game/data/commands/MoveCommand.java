package com.jesse.game.data.commands;

import com.google.gson.JsonObject;
import com.jesse.game.data.PlayerHolder;
import com.jesse.game.utils.Constants;
import com.jesse.game.utils.Constants.Direction;
import com.jesse.game.utils.Constants.State;

public class MoveCommand extends Command {
	
	private static final long serialVersionUID = 1L;
	
	private Direction mDirection;
	private State mState;
	
//	public MoveCommand(int playerId) {
//		this(null, null, playerId);
//	}
	
	public MoveCommand(Direction direction, State state, int playerId, int mapId) {
		super(playerId, COMMAND_MOVE, mapId);
		mDirection = direction;
		mState = state;
	}
	
	@Override
	public void execute(PlayerHolder player) {
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
		
		player.setState(mState);
	}

	@Override
	public String toString() {
		return "Player " + mPlayerId + " wants to " + mState + " to the " + mDirection + " on map " + Constants.MAPS.get(mMapId);
	}

	@Override
	public JsonObject getGson(boolean... bs) {
		JsonObject jObj = new JsonObject();
		JsonObject commandJson = new JsonObject();
		commandJson.addProperty("mDirection", mDirection.toString());
		commandJson.addProperty("mState", mState.toString());
		jObj.addProperty("command_type", 1);
		jObj.addProperty("mPlayerId", mPlayerId);
		jObj.addProperty("mMapId", mMapId);
		jObj.add("command", commandJson);
		return jObj;
	}	
	
}
