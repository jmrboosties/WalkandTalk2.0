package com.jesse.game.data;

import java.io.Serializable;

public abstract class Command implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected int mPlayerId;
	
	public abstract void execute(PlayerHolder playerHolder);
	
	public int getPlayerId() {
		return mPlayerId;
	}
		
	public abstract String toString();
	
	public abstract String getGson();

}
