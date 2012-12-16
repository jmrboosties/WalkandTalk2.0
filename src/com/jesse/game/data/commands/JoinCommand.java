package com.jesse.game.data.commands;

import com.google.gson.JsonObject;
import com.jesse.game.data.PlayerHolder;
import com.jesse.game.utils.Constants;

public class JoinCommand extends Command {
	
	private static final long serialVersionUID = 1L;

	private PlayerHolder mPlayer;
	
	public JoinCommand(PlayerHolder player) {
		super(player.getId(), COMMAND_JOIN, player.getMapId());
		mPlayer = player;
	}
	
	@Override
	public void execute(PlayerHolder player) {
		player.setState(mPlayer.getState());
		player.coordinates = mPlayer.coordinates;
		player.setName(mPlayer.getName());
		player.setMapId(mMapId);
	}

	@Override
	public String toString() {
		return mPlayer.getName() + " wants to join map " + Constants.MAPS.get(mMapId) + "!";
	}

	@Override
	public JsonObject getGson(boolean... bs) {
		JsonObject playerObj = new JsonObject();
		playerObj.add("mPlayer", mPlayer.getGson(true, true, true, true, true));
		JsonObject job = new JsonObject();
		job.addProperty("command_type", COMMAND_JOIN);
		job.addProperty("mPlayerId", mPlayerId);
		job.addProperty("mMapId", mMapId);
		job.add("command", playerObj);
		return job;
	}

}
