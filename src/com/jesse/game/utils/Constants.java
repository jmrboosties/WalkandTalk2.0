package com.jesse.game.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constants {
	
	public static final int SPLASH_STATE_ID = 0;
	public static final int LOADING_STATE_ID = 1;
	public static final int GAME_STATE_ID = 2;

	public enum Direction {
		DOWN, RIGHT, LEFT, UP
	}
	
	public enum State {
		IDLE, RUN, WALK
	}
	
	public static final int MAIN = 0;
	public static final int FIELD = 1;
	
	public static final Map<Integer, String> MAPS = createMaps();

	private static Map<Integer, String> createMaps() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		map.put(MAIN, "littleplace");
		map.put(FIELD, "greenfield");
		
		return Collections.unmodifiableMap(map);
	}
	
}
