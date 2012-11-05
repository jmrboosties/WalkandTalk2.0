package com.jesse.game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import come.jesse.game.states.SplashPage;

public class Game extends StateBasedGame {

	public static final int SCREEN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 576;
	public static final int SCALE = 2;
	
	/**
	 * Launcher
	 * @throws SlickException 
	 */
	
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Game("Pokemon"));
		app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
		app.setTargetFrameRate(240);
//		app.getGraphics().scale(SCALE, SCALE);
		app.start();
	}
	

	public Game(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new SplashPage(1));
	}

}
