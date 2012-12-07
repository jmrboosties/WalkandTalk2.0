package com.jesse.game.listeners;

public interface OnEnterPressedListener {

	/**
	 * Listener for when user presses enter while a TextField has focus.
	 * 
	 * @param text, the content of the TextField
	 * @return true if whatever you handled was successful, else false.
	 */
	public boolean onEnterPressed(int dialogId, String text);
	
}
