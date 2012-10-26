package com.jesse.game.utils;

import com.google.gson.JsonObject;


public interface Gsonable  {

	/**
	 * Booleans custom control what params are and aren't passed
	 * 
	 * @param bools
	 * @return
	 */
	public JsonObject getGson(boolean... bools);
	
}
