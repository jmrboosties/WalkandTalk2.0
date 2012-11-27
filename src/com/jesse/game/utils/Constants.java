package com.jesse.game.utils;

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
	
}
