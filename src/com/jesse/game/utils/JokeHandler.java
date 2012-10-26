package com.jesse.game.utils;

public class JokeHandler {

	private static final int WAITING = 0;
	private static final int SENT_REQUEST = 1;
	private static final int SENT_RESPONSE = 2;
	
	private int mState = WAITING;
	
	public String processInput(String input) {
		String output = null;
		
		if(mState == WAITING) {
			output = "Hello! Please type in your name.";
			mState = SENT_REQUEST;
		}
		else if(mState == SENT_REQUEST) {
			output = "Hi " + input + ". Nice to meet you.";
			mState = SENT_RESPONSE;
		}
		else if(mState == SENT_RESPONSE) {
			output = "gay";
			mState = WAITING;
		}
		
		Print.log("output from handler: " + output);
		return output;
	}
	
}
