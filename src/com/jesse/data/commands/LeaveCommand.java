package com.jesse.data.commands;

import com.google.gson.JsonObject;
import com.jesse.data.PlayerHolder;

public class LeaveCommand extends Command {
	
	private static final long serialVersionUID = 1L;
	
	private PlayerHolder mPlayer;
	
	public LeaveCommand(PlayerHolder player) {
		super(player.getId(), COMMAND_LEAVE, player.getMapId());
		mPlayer = player;
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
