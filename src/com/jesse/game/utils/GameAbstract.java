package com.jesse.game.utils;

import java.util.HashMap;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.Transition;

import com.jesse.game.GameMain;

public abstract class GameAbstract implements Game, InputListener {
	
	private String mName;
	private HashMap<Integer, GameState> mStates = new HashMap<Integer, GameState>();
	private GameState mCurrentState;
	private GameState mNextState;
	
	/*
	 * Transitions
	 */
	private Transition mLeaveTransition;
	private Transition mEnterTransition;

	public static final int SCREEN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 576;
	public static final int SCALE = 2;
	
	
	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public GameAbstract(String name) {
		mName = name;
		mCurrentState = new BasicGameState() {
			
			@Override
			public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
					throws SlickException { }
			
			@Override
			public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
					throws SlickException { }
			
			@Override
			public void init(GameContainer arg0, StateBasedGame arg1)
					throws SlickException { }
			
			@Override
			public int getID() {
				return -1;
			}
		};
	}
	
	/*
	 * Overridden Interface Methods
	 */

	@Override
	public boolean closeRequested() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getTitle() {
		return mName;
	}


	@Override
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseClicked(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAcceptingInput() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInput(Input arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int arg0, char arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int arg0, char arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void controllerButtonPressed(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void controllerButtonReleased(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void controllerDownPressed(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void controllerDownReleased(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void controllerLeftPressed(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void controllerLeftReleased(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void controllerRightPressed(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void controllerRightReleased(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void controllerUpPressed(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void controllerUpReleased(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Methods
	 */
	
	public int getStateCount() {
		return mStates.keySet().size();
	}
	
	public int getCurrentStateId() {
		return mCurrentState.getID();
	}
	
	public GameState getCurrentState() {
		return mCurrentState;
	}
	
	public void addState(GameState state) {
		mStates.put(new Integer(state.getID()), state);
		
		if (mCurrentState.getID() == -1)
			mCurrentState = state;
		
	}
	
	public GameState getState(int id) {
		return mStates.get(id);
	}
	
	public void enterState(int id) {
		enterState(id, new EmptyTransition(), new EmptyTransition());
	}
	
	public void enterState(int id, Transition leave, Transition enter) {
		if (leave == null)
			leave = new EmptyTransition();
		if (enter == null)
			enter = new EmptyTransition();
		
		mLeaveTransition = leave;
		mEnterTransition = enter;
		
		mNextState = getState(id);
		if(mNextState == null)
			throw new RuntimeException("No state with that id");
		
		mLeaveTransition.init(mCurrentState, mNextState);
	}
	
	/*
	 * Abstract methods
	 */
	
	public abstract void initStatesList(GameContainer arg0) throws SlickException;
	
	/**
	 * Launcher
	 * @throws SlickException 
	 */
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new GameMain("Pokemon"));
		app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
		app.setTargetFrameRate(240);
//		app.getGraphics().scale(SCALE, SCALE);
		app.start();
	}

}
