package com.jesse.game.data;

import com.google.gson.JsonObject;
import com.jesse.game.utils.Print;

public class JoinCommand extends Command {
	
	private static final long serialVersionUID = 1L;

	private PlayerHolder mPlayer;
	
	public JoinCommand(PlayerHolder player) {
		mPlayer = player;
		mPlayerId = mPlayer.getId();
		Print.log("id: " + mPlayerId);
		mCommandType = COMMAND_JOIN;
	}
	
	@Override
	public void execute(PlayerHolder player) {
		player.setState(mPlayer.getState());
		player.coordinates = mPlayer.coordinates;
		player.setName(mPlayer.getName());
		Print.log(player.getName() + " has joined the game!");
		Print.log("player gson" + player.getGson(true, true, true, true).toString());
	}

	@Override
	public String toString() {
		return mPlayer.getName() + " wants to join!";
	}

	@Override
	public JsonObject getGson(boolean... bs) {
		JsonObject playerObj = new JsonObject();
		playerObj.add("mPlayer", mPlayer.getGson(true, true, true, true));
		JsonObject job = new JsonObject();
		job.addProperty("command_type", 0);
		job.add("command", playerObj);
		job.addProperty("mPlayerId", mPlayerId);
		Print.log("result of gson: " + job.toString());
		return job;
	}

}
