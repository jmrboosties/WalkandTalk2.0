package com.jesse.game.data;

import com.google.gson.JsonObject;

public class MessageCommand extends Command {

	private static final long serialVersionUID = 1L;
	
	private String mMessage;
	
	public MessageCommand(int playerId, String message) {
		mPlayerId = playerId;
		mMessage = message;
	}

	@Override
	public JsonObject getGson(boolean... bools) {
		JsonObject jObj = new JsonObject();
		JsonObject commandJson = new JsonObject();
		commandJson.addProperty("mMessage", mMessage);
		jObj.addProperty("command_type", COMMAND_MESSAGE);
		jObj.addProperty("mPlayerId", mPlayerId);
		jObj.add("command", commandJson);
		return jObj;
	}

	@Override
	public void execute(PlayerHolder holder) { }

	@Override
	public String toString() {
		return "Message from + " + mPlayerId + ":" + mMessage;
	}
	
	public String getMessage() {
		return mMessage;
	}

}
