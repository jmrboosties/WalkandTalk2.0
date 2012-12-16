package com.jesse.game.data.commands;

import com.google.gson.JsonObject;
import com.jesse.game.data.PlayerHolder;
import com.jesse.game.objects.Vector2i;

public class WarpCommand extends Command {

	private static final long serialVersionUID = 1L;
	
	private Vector2i mCoordinates;
	private int mWarpMap;

	public WarpCommand(PlayerHolder holder, int toMapId) {
		super(holder.getId(), COMMAND_WARP, holder.getMapId());
		mCoordinates = holder.coordinates;
		mWarpMap = toMapId;
	}
	
	@Override
	public JsonObject getGson(boolean... bools) {
		JsonObject commandJson = new JsonObject();
		commandJson.add("mCoordinates", mCoordinates.getGson());
		commandJson.addProperty("mWarpMap", mWarpMap);
		
		JsonObject json = new JsonObject();
		json.addProperty("command_type", COMMAND_JOIN);
		json.addProperty("mPlayerId", mPlayerId);
		json.addProperty("mMapId", mMapId);
		json.add("command", commandJson);
		
		return json;
	}

	@Override
	public void execute(PlayerHolder holder) {
		holder.coordinates.reassign(mCoordinates);
		holder.setMapId(mWarpMap);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
