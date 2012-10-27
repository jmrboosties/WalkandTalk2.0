package com.jesse.game.data;

import java.io.Serializable;

import com.jesse.game.utils.Gsonable;

public abstract class Command implements Serializable, Gsonable {
	
	private static final long serialVersionUID = 1L;
	
	protected int mPlayerId = -1;
	protected int mCommandType = -1;
	
	public static final int COMMAND_JOIN = 0;
	public static final int COMMAND_MOVE = 1;
	
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
		
	public abstract String toString();
	
}
