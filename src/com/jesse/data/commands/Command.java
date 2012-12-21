package com.jesse.data.commands;

import java.io.Serializable;

import com.jesse.data.PlayerHolder;
import com.jesse.game.utils.Gsonable;

public abstract class Command implements Serializable, Gsonable {
	
	private static final long serialVersionUID = 1L;
	
	protected int mPlayerId = -1;
	protected int mCommandType = -1;
	protected int mMapId = -1;
	
	public static final int COMMAND_JOIN = 0;
	public static final int COMMAND_MOVE = 1;
	public static final int COMMAND_MESSAGE = 2;
	public static final int COMMAND_LEAVE = 3;
	public static final int COMMAND_WARP = 4;
	
	public Command(int playerId, int commandType, int mapId) {
		mPlayerId = playerId;
		mCommandType = commandType;
		mMapId = mapId;
	}
	
	public abstract void execute(PlayerHolder holder);
	
	public int getPlayerId() {
		return mPlayerId;
	}
	
	public void setPlayerId(int id) {
		mPlayerId = id;
	}
	
	public int getCommandType() {
		return mCommandType;
	}
	
	public void setCommandType(int type) {
		mCommandType = type;
	}
	
	public int getMapId() {
		if(mMapId < 0)
			throw new IllegalStateException("map id is less than 0, value: " + mMapId);
		
		return mMapId;
	}
	
	public void setMapId(int id) {
		mMapId = id;
	}
		
	public abstract String toString();
	
}
