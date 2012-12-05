package com.jesse.game.data;

import com.google.gson.JsonObject;

public class LeaveCommand extends Command {
	
	private static final long serialVersionUID = 1L;
	
	private PlayerHolder mPlayer;
	
	public LeaveCommand(PlayerHolder player) {
		mPlayerId = player.getId();
		mPlayer = player;
		mCommandType = COMMAND_LEAVE;
	}
	
	@Override
	public JsonObject getGson(boolean... bools) {
		return null;
	}

	@Override
	public void execute(PlayerHolder holder) { }

	@Override
	public String toString() { 
		return mPlayer.getName() + " with id " + mPlayerId + " is leaving.";
	}

}
