package com.jesse.game.data;

import java.io.Serializable;

import com.jesse.game.utils.Gsonable;

public abstract class Command implements Serializable, Gsonable {
	
	private static final long serialVersionUID = 1L;
	
	protected int mPlayerId;
	
	public abstract void execute(PlayerHolder holder);
	
	public int getPlayerId() {
		return mPlayerId;
	}
		
	public abstract String toString();
	
}
