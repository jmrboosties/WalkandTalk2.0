package com.jesse.game;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class DisplayExample {

//	private MouseHandler mMouseHandler;
	
	public void start() {
//		mMouseHandler = new MouseHandler();
		
		
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		while(!Display.isCloseRequested()) {
			
			Display.update();
		}
		
		Display.destroy();
		
	}
	
	public static void main(String[] args) {
		DisplayExample example = new DisplayExample();
		example.start();
	}
	
}
