package com.jesse.game.server;

public abstract class Command {
	
	protected int mPlayerId;
	
	public abstract void execute(PlayerHolder player);
	
	public int getId() {
		return mPlayerId;
	}

}
