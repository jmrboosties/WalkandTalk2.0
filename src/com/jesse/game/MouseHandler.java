package com.jesse.game;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

public class MouseHandler {

	public MouseHandler() {
		
	}
	
	public Vector2f getMouseCoords() {
		int x = Mouse.getX();
		int y = Mouse.getY();
		
		Vector2f vector = new Vector2f(x, y);
		
		return vector;
	}
	
	public boolean isLeftClicking() {
		return Mouse.isButtonDown(0);
	}
	
	public boolean isRightClicking() {
		return Mouse.isButtonDown(1);
	}
	
}
