package com.jesse.game.data;

import com.google.gson.JsonObject;
import com.jesse.game.utils.Print;

public class JoinCommand extends Command {
	
	private static final long serialVersionUID = 1L;

	private PlayerHolder mPlayer;
	
	public JoinCommand(PlayerHolder player) {
		mPlayer = player;
		mPlayerId = mPlayer.getId();
		mCommandType = COMMAND_JOIN;
	}
	
	@Override
	public void execute(PlayerHolder player) {
		player = new PlayerHolder(mPlayer);
		Print.log(player.getName() + " has joined the game!");
	}

	@Override
	public String toString() {
		return mPlayer.getName() + " wants to join!";
	}

	@Override
	public JsonObject getGson(boolean... bs) {
		JsonObject job = new JsonObject();
		job.addProperty("command_type", 0);
		job.add("player", mPlayer.getGson());
		return job;
	}

}
